package sim.theory;

import java.util.ArrayList;

import java.util.Collections;

import sim.upgrades.Variable;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Theory1 extends Theory {

    public double coastingPub = 6;

    // public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = { 11, 10, 10, 10, 10, 10 };

    public Theory1[] t1Clones = new Theory1[6];

    public int[] milestoneLevels = new int[4];

    public Theory1(double pubMark) {
        super(1, pubMark);

        this.name = "Recurrence Relations";

        this.seconds = 0;
        this.tickCount = 0;

        this.isCoasting = false;

        this.milestoneLevels[0] = 3;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 1;
        this.milestoneLevels[3] = 1;

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();

        this.milestoneSwapCheck();

        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.164 * (this.maxRho - this.publicationMark));

        /**
         * if(this.maxRho - this.variables[5].nextCost < 0.01 &&
         * this.publicationMultiplier > 2.0) {
         * this.isCoasting = true;
         * }
         */

    }

    private void milestoneSwapCheck() {

        if (this.strategy.name.equalsIgnoreCase("T1Baby")) {
            if (this.maxRho < 25 && this.publicationMark < 25) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;

            } else if (this.maxRho < 50 && this.publicationMark < 50) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;

                }

            } else if (this.maxRho < 75 && this.publicationMark < 75) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 1;

                } else if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 2;
                }
            } else if (this.maxRho < 100 && this.publicationMark < 100) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 1;

                } else if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;

                }
            } else if (this.maxRho < 125 && this.publicationMark < 125) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 1;

                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;

                }
            } else if (this.maxRho < 150 && this.publicationMark < 150) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 1;

                    double tempRhodot1 = this.calculateRhodot();

                    this.milestoneLevels[0] = 3;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;

                   
                   
                    

                    double tempRhodot2 = this.calculateRhodot();
                    if (tempRhodot1 > tempRhodot2) {
                        this.milestoneLevels[0] = 2;
                        this.milestoneLevels[1] = 1;
                        this.milestoneLevels[2] = 1;
                        this.milestoneLevels[3] = 1;
                    }

                } else {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;

                }

            } else {
                this.milestoneLevels[0] = 3;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 1;
                this.milestoneLevels[3] = 1;

            }
        } else if (this.strategy.name == "T1NoMS") {
            if (this.maxRho < 25 && this.publicationMark < 25) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;

            } else if (this.maxRho < 50 && this.publicationMark < 50) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;// c4q^2 term.
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;

                }

            } else if (this.maxRho < 75 && this.publicationMark < 75) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;

                } else if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 2;
                }
            } else if (this.maxRho < 100 && this.publicationMark < 100) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;

                } else if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;

                }
            } else if (this.maxRho < 125 && this.publicationMark < 125) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 3;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;

                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;

                }
            } else if (this.maxRho < 150 && this.publicationMark < 150) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;

                } else {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;

                }
            } else if (this.maxRho < 175 && this.publicationMark < 175) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;

                } else {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;

                }

            } else {
                this.milestoneLevels[0] = 3;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 3;

            }
        }
    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    private void updateEquation() {

        double term1 = 0;
        double term2 = 0;
        double term3 = 0;

        term1 = this.variables[2].value * (1 + 0.05 * this.milestoneLevels[0]) + this.variables[3].value;

        if (this.milestoneLevels[1] == 1) {
            term1 = term1 + Math.log10(Variable.add(1, this.rho * Math.log(10) / 100.0));
        } else {

        }

        if (this.milestoneLevels[2] == 1) {
            term2 = this.variables[4].value + this.rho * 0.2;
        } else {
            term2 = 0;
        }
        if (this.milestoneLevels[3] == 1) {
            term3 = this.variables[5].value + this.rho * 0.3;
        } else {
            term3 = 0;
        }

        this.rhodot = Variable.add(term3, Variable.add(term1, term2)) +
                this.variables[0].value + this.variables[1].value +
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;

        this.rho = Variable.add(this.rho, this.rhodot);

    }

    /**
     * Calculates current rhodot. Used for determining current rhodot after a
     * variable is bought.
     * 
     * @return - current rhodot.
     */
    public double calculateRhodot() {
        double term1 = this.variables[2].value * 1.15 + this.variables[3].value +
                Math.log10(Variable.add(1, this.rho * Math.log(10) / 100.0));

        double term2 = this.variables[4].value + this.rho * 0.2;
        double term3 = this.variables[5].value + this.rho * 0.3;

        double rhodot = Variable.add(term3, Variable.add(term1, term2)) +
                this.variables[0].value + this.variables[1].value +
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
        return rhodot;
    }

    /**
     * Buys 1 level of the variable according to the variableNumber input. For
     * example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * 
     * @param variableNumber - the variable number to buy. Note that the variable
     *                       number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        super.buyVariable(variableNumber);

        if (this.variableWeights[variableNumber] < 10.03) {

        } else {
            this.resetIdlePeriod(variableNumber);
        }

    }

    public void runUntilPublish() {
        while (this.finishCoasting == false) {
            this.runEpoch();
        }
    }

    public void runEpoch() {

        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }

        int bestVarIndex = this.findBestVarToBuy();
        if (!isCoasting) {
            double buyDelay = this.calculateBuyDelay(bestVarIndex);
            this.idleUntil(this, this.variables[bestVarIndex].nextCost + buyDelay);

            if (!isCoasting) {
                this.buyVariable(bestVarIndex);
            }

        } else {// is coasting, stop buying any variable.

            this.coastUntilPublish();

            this.finishCoasting = true;
            return;
        }

    }

    public int findBestVarToBuy() {
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
            if (this.variables[i].isActive == 1) {
                while (this.variables[i].cost < this.publicationMark * 0.8) {
                    this.variables[i].level += 1;
                    this.variables[i].update();

                }
            }

        }

        ArrayList<Double> temp = new ArrayList<>();

        for (int i = 0; i < this.variables.length; i++) {
            if (this.variables[i].isActive == 1) {
                this.adjustWeightings(i);
                try {
                    temp.add(Math.round(this.variables[i].nextCost * 1000) / 1000.0 + (this.variableWeights[i]));
                } catch (Exception e) {

                }

            } else {
                temp.add(10000.0);
            }

        }

        int bestVarIndex = temp.lastIndexOf(Collections.min(temp));

        return bestVarIndex;

    }

    public double calculateBuyDelay(int bestVarIndex) {

        double rhoDotOld = this.calculateRhodot();
        this.variables[bestVarIndex].level += 1;
        this.variables[bestVarIndex].update();
        double rhoDotNew = this.calculateRhodot();
        this.variables[bestVarIndex].level -= 1;
        this.variables[bestVarIndex].update();

        double rhoDotDiff = rhoDotNew - rhoDotOld;

        double rhodotMultiplier = Math.pow(10, rhoDotDiff);

        double Multi = 1 / (1 - Math.pow((1 / rhodotMultiplier), 10 / 3.1));
        Multi = Math.log10(Multi);

        if (this.strategy.type == "idle") {
            Multi = 0;
        }

        return Multi;

    }

    public void adjustWeightings(int i) {

        // public double[] variableWeights = {11, 10, 10, 10, 10, 10};
        if (this.strategy.name.equalsIgnoreCase("T1Play")) {

            double rhoDotOld = this.calculateRhodot();
            this.variables[i].level += 1;
            this.variables[i].update();
            double rhoDotNew = this.calculateRhodot();
            this.variables[i].level -= 1;
            this.variables[i].update();

            double rhoDotDiff = rhoDotNew - rhoDotOld;

            double rhodotMultiplier = Math.pow(10, rhoDotDiff);
            double coefficient = rhodotMultiplier * (1.6 - 1) / (1.6 * (rhodotMultiplier - 1));
            coefficient = Math.log10(coefficient);
            this.variableWeights[i] = 10 + 1.2 * coefficient;

            this.variables[2].deactivate();
            this.variables[3].deactivate();
        } else if (this.strategy.name.equalsIgnoreCase("T1Play2")) {

            double rhoDotOld = this.calculateRhodot();
            this.variables[i].level += 1;
            this.variables[i].update();
            double rhoDotNew = this.calculateRhodot();

            this.variables[i].level -= 1;
            this.variables[i].update();

            double rhoDotDiff = rhoDotNew - rhoDotOld;

            double rhodotMultiplier = Math.pow(10, rhoDotDiff);
            double coefficient = rhodotMultiplier * (2.15 - 1) / (2.15 * (rhodotMultiplier - 1));
            coefficient = Math.log10(coefficient);

            if (i == 0) {
                this.variableWeights[i] = 9.9 + 1.45 * coefficient;
            } else if (i == 1) {
                this.variableWeights[i] = 10.1 + 1.1 * coefficient;
            } else if (i == 4) {
                this.variableWeights[i] = 9.9 + 1.65 * coefficient;
            } else if (i == 5) {
                this.variableWeights[i] = 10.0 + 1.0 * coefficient;
            }
            // this.variableWeights[0] = 11.2;
            // this.variableWeights[1] = 10.0;
            // this.variableWeights[4] = 11.1;
            // this.variableWeights[5] = 9.7;

            this.variables[2].deactivate();
            this.variables[3].deactivate();

        } else if (this.strategy.name.equalsIgnoreCase("T1C34")) {
            this.variableWeights[0] = 10.0;
            this.variableWeights[1] = 10.0;
            this.variables[2].deactivate();
            this.variables[3].deactivate();
            this.variableWeights[4] = 10.0;
            this.variableWeights[5] = 10.0;
        } else if (this.strategy.name.equalsIgnoreCase("T1C4")) {
            this.variableWeights[0] = 10.0;
            this.variableWeights[1] = 10.0;
            this.variables[2].deactivate();
            this.variables[3].deactivate();
            this.variables[4].deactivate();
            this.variableWeights[5] = 10.0;
        } else if (this.strategy.name.equalsIgnoreCase("T1Baby")) {
            if (this.maxRho < 25 && this.publicationMark < 25) {
                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;

            } else if (this.maxRho < 50 && this.publicationMark < 50) {
                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;

            } else if (this.maxRho < 75 && this.publicationMark < 75) {

                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
            } else if (this.maxRho < 100 && this.publicationMark < 100) {

                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
            } else if (this.maxRho < 125 && this.publicationMark < 125) {

                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
            } else if (this.maxRho < 150 && this.publicationMark < 150) {

                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
            } else {

                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 11.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
            }
        }

        if (this.publicationMultiplier > 7) {
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory1 theory1, double variableCost) {
        while (theory1.rho < variableCost) {
            theory1.moveTick();

        }
    }

    @Override
    public void display() {
        // System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" +
        // this.tickNumber);
        System.out.print(String.format("%.3f", this.seconds / 60.0 / 60.0) + "\t");
        /**
         * for(int i = 0; i < this.variables.length; i++) {
         * System.out.print(this.variables[i].level + "\t");
         * }
         */

        System.out.print(String.format("%.2f", this.maxRho) + "\t" + String.format("%.2f", this.rhodot) + "\t" +
                this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}