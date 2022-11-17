package sim.theory;

import java.util.ArrayList;
import java.util.Collections;

import sim.upgrades.Variable;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Theory4 extends Theory {

    public double qdot;
    public double q;

    // public double tauPerHour;

    public boolean isCoasting;
    public int q2check = 0;
    public int counter = 0;

    public double coastingPub = 6.5;

    public double[] switchMSRatio = {0.96, 0.50, 0.50, 1, 1};
    

    public int[] milestoneLevels = new int[3];

    public double[] variableWeights = { 1000, 1000, 10, 10, 10, 10, 10.0, 10.2 };
    // public double[] variableWeights = {10,10,10,10,10,10,11,10};

    public Theory4[] t2Clones = new Theory4[8];

    public Theory4(double pubMark) {
        super(4, pubMark);
        this.name = "Polynomials";

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.q = 0;
        this.qdot = Double.MAX_VALUE;
        this.isCoasting = false;

        this.milestoneLevels[0] = 3;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 3;

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(1.305, 5, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(3.75, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(2.468, 2000, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(4.85, Math.pow(10, 4), 3, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(12.5, Math.pow(10, 8), 5, 1, true, true, false, false, false, new double[2]);
        this.variables[5] = new Variable(58, Math.pow(10, 10), 10, 1, true, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(100, 1000, Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[7] = new Variable(1000, Math.pow(10, 4), 2, 1, true, true, false, false, false, new double[2]);

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

        this.publicationMultiplier = Math.pow(10, 0.165 * (this.maxRho - this.publicationMark));

    }

    public void milestoneSwapCheck() {

        if (this.strategy.name.equalsIgnoreCase("T4Baby")) {
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

                if(this.maxRho < this.publicationMark * this.switchMSRatio[0]) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                } else {
                    while(this.variables[7].nextCost < this.rho) {
                        this.buyVariable(7);
                    }
                    while(this.variables[6].nextCost < this.rho) {
                        this.buyVariable(6);
                    }
                    while(this.variables[3].nextCost < this.rho) {
                        this.buyVariable(3);
                    }
                    if (this.tickCount % 100 < 50) {
                        this.milestoneLevels[0] = 3;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 0;
    
                    } else if (this.tickCount % 100 < 100) {
                        this.milestoneLevels[0] = 0;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 3;
    
                    }
                }
               
            } else if (this.maxRho < 125 && this.publicationMark < 125) {
                if(this.maxRho < this.publicationMark * this.switchMSRatio[1]) {
                    this.milestoneLevels[0] = 3;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                } else {
                    while(this.variables[7].nextCost < this.rho) {
                        this.buyVariable(7);
                    }
                    while(this.variables[6].nextCost < this.rho) {
                        this.buyVariable(6);
                    }
                    while(this.variables[3].nextCost < this.rho) {
                        this.buyVariable(3);
                    }
                    if (this.tickCount % 100 < 30 && 
                    !(this.rho - this.variables[5].nextCost < 0 && this.rho - this.variables[5].nextCost > -0.5)) {
                        this.milestoneLevels[0] = 3;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 1;
    
                    } else if (this.tickCount % 100 < 100) {
                        this.milestoneLevels[0] = 1;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 3;
    
                    }
                }
            } else if (this.maxRho < 150 && this.publicationMark < 150) {
                if(this.maxRho < this.publicationMark * this.switchMSRatio[2]) {
                    this.milestoneLevels[0] = 3;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 1;
                } else {
                    while(this.variables[7].nextCost < this.rho) {
                        this.buyVariable(7);
                    }
                    while(this.variables[6].nextCost < this.rho) {
                        this.buyVariable(6);
                    }
                    while(this.variables[3].nextCost < this.rho) {
                        this.buyVariable(3);
                    }
                    if (this.tickCount % 100 < 30 && 
                    !(this.rho - this.variables[5].nextCost < 0 && this.rho - this.variables[5].nextCost > -1.0)) {
                        this.milestoneLevels[0] = 3;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 2;
    
                    } else if (this.tickCount % 100 < 100) {
                        this.milestoneLevels[0] = 2;
                        this.milestoneLevels[1] = 0;
                        this.milestoneLevels[2] = 3;
    
                    }
                }
            } else if (this.maxRho < 175 && this.publicationMark < 175) {
                if (this.tickCount % 100 < 100) {
                    this.milestoneLevels[0] = 3;
                    this.milestoneLevels[1] = 0;
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
        } else if (this.strategy.name == "T4NoMS") {
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
    public void updateEquation() {

        double numerator = Math.log10(Math.pow(2, this.milestoneLevels[2])) + this.variables[6].value
                + this.variables[7].value
                + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
        this.qdot = Math.min(numerator - Variable.add(Math.log10(1), this.q),
                this.q + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + 1);

        this.q = Variable.add(this.q, this.qdot);
        // System.out.println(this.qdot + "\t"+ this.maxRho);

        /**
         * double p = Variable.subtract(Variable.add(this.q, 0) * 2, 0);
         * this.q = Variable.add(Variable.add(0, p),
         * Math.log10(16) + this.variables[6].value + this.variables[7].value +
         * Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency));
         * this.q = this.q * 0.5;
         * this.q = Variable.subtract(this.q, 0);
         */

        double term1 = 0;
        double term2 = 0;
        double term3 = 0;
        double term4 = 0;
        double term5 = 0;

        term1 = this.variables[0].value * (1 + 0.15 * this.milestoneLevels[1]) + this.variables[1].value;
        term2 = this.variables[2].value + this.q;

        if (this.milestoneLevels[0] > 0) {
            term3 = this.variables[3].value + this.q * 2;
        } else {
            term3 = 0;
        }

        if (this.milestoneLevels[0] > 1) {
            term4 = this.variables[4].value + this.q * 3;
        } else {
            term4 = 0;
        }

        if (this.milestoneLevels[0] > 2) {
            term5 = this.variables[5].value + this.q * 4;
        } else {
            term5 = 0;
        }

        // this.q = Variable.subtract((Variable.add(Variable.add(this.q, 0) * 2,
        // Math.log10(16) + this.variables[6].value
        // + this.variables[7].value + Math.log10(Theory.adBonus) +
        // Math.log10(this.tickFrequency))) * 0.5, 0);

        this.rhodot = Variable.add(term5, Variable.add(term4, Variable.add(term3, Variable.add(term1, term2))))
                + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
        this.rho = Variable.add(this.rho, this.rhodot);

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

        if (this.variableWeights[variableNumber] < 10.11) {

        } else {
            this.resetIdlePeriod(variableNumber);
        }
        super.buyVariable(variableNumber);

        if (variableNumber == 7) {
            this.q2check = 1;
            this.counter = 0;
        } else {
            this.q2check = 0;
        }

    }

    public void runUntilPublish() {
        while (this.finishCoasting == false) {
            this.runEpoch();
        }
    }

    /**
     * Finds the best variable to buy according to variable weightings of the
     * strategy. Then waits to
     * buy that variable.
     * <p>
     * If is coasting, then don't buy any variable and just wait until it is
     * appropriate to publish.
     * </p>
     * 
     * @param pubMulti
     */
    public void runEpoch() {
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }

        // Finds the best variable index to buy according to variable weightings.
        int bestVarIndex = this.findBestVarToBuy();

        // If not coasting, waits and buys the recommended variable. If is coasting,
        // then just wait until
        // ready to publish.

        if (!isCoasting) {

            this.idleUntil(this, this.variables[bestVarIndex].nextCost);
            this.buyVariable(bestVarIndex);

        } else {// is coasting, stop buying any variable.

            this.coastUntilPublish();

            // this.printSummary();
            this.finishCoasting = true;

            return;
        }

    }

    /**
     * Finds the best variable index to buy according to variable weightings.
     */

    public int findBestVarToBuy() {
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
            if (this.variables[i].isActive == 1) {
                // Time spent in the initial recovery stage is negligible. Prevents
                // initialisation errors.
                while (this.variables[i].cost < this.publicationMark * 0.50) {
                    this.variables[i].level += 1;
                    this.variables[i].update();

                }
            }

        }

        ArrayList<Double> temp = new ArrayList<>();

        // Calculates variable weightings to decide which variable to buy.
        for (int i = 0; i < this.variables.length; i++) {
            if (this.variables[i].isActive == 1) {
                this.adjustWeightings(i);
                try {
                    temp.add(Math.round(this.variables[i].nextCost * 1000) / 1000.0 + (this.variableWeights[i]));
                } catch (Exception e) {

                }
                // Adjust weightings, current best is 157.443 at 15.0392282
                // current best is 155.283 at 15.039224
            } else {
                temp.add(10000.0);
            }

        }

        int bestVarIndex = temp.lastIndexOf(Collections.min(temp));

        return bestVarIndex;

    }

    public void adjustWeightings(int i) {

        // variableWeights = {1000,1000,10,10,10,10,10.0,10.2};

        if (this.variables[i].isActive == 1) {

            if (this.strategy.name.equalsIgnoreCase("T4C3d")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;
            } else if (this.strategy.name.equalsIgnoreCase("T4PlaySpqcey")) {

                if (this.maxRho < this.publicationMark - 12) {
                    this.variableWeights[0] = 12.0;
                    this.variableWeights[1] = 11.0;
                } else {
                    this.variableWeights[0] = 120.0;
                    this.variableWeights[1] = 110.0;
                }

                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;

                if (this.publicationMultiplier > 3.0) {
                    this.variables[6].deactivate();

                }
                if (this.publicationMultiplier > 3.0) {
                    this.variables[7].deactivate();
                }

                //
                if (this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.1 + (0.028 * (this.variables[i].level % 10) - 0.12);
                } else {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.10 + (0.028 * (this.variables[i].level % 10) - 0.12);
                }

                this.variableWeights[7] = 10.00;

                double q2Offset = 1.0 + Math.pow(this.publicationMultiplier, 0.5);

                if (this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            } else if (this.strategy.name.equalsIgnoreCase("T4SolC")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;

                if (this.publicationMultiplier > 3.0) {
                    this.variables[6].deactivate();

                }
                if (this.publicationMultiplier > 3.0) {
                    this.variables[7].deactivate();
                }

                //
                if (this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.1 + (0.028 * (this.variables[i].level % 10) - 0.12);
                } else {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.10 + (0.028 * (this.variables[i].level % 10) - 0.12);
                }

                this.variableWeights[7] = 10.00;

                double q2Offset = 1.0 + Math.pow(this.publicationMultiplier, 0.5);

                if (this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;

            } else if (this.strategy.name.equalsIgnoreCase("T4Sol2")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;

                if (this.publicationMultiplier > 3.0) {
                    this.variables[6].deactivate();

                }
                if (this.publicationMultiplier > 3.0) {
                    this.variables[7].deactivate();
                }

                //
                if (this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.1 + (0.028 * (this.variables[i].level % 10) - 0.12);
                } else {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.10 + (0.028 * (this.variables[i].level % 10) - 0.12);
                }

                this.variableWeights[7] = 10.00;

                double q2Offset = 1.0 + Math.pow(this.publicationMultiplier, 0.5);

                if (this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            } else if (this.strategy.name.equalsIgnoreCase("T4Solar")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;

                if (this.publicationMultiplier > 3.0) {
                    this.variables[6].deactivate();

                }
                if (this.publicationMultiplier > 3.0) {
                    this.variables[7].deactivate();
                }

                //
                if (this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.1 + (0.028 * (this.variables[i].level % 10) - 0.12);
                } else {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.10 + (0.028 * (this.variables[i].level % 10) - 0.12);
                }

                this.variableWeights[7] = 10.00;

                double q2Offset = 1.0 + Math.pow(this.publicationMultiplier, 0.5);

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            } else if (this.strategy.name.equalsIgnoreCase("T4Gold")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;

                if (this.publicationMultiplier > 3.0) {
                    this.variables[6].deactivate();

                }
                if (this.publicationMultiplier > 3.0) {
                    this.variables[7].deactivate();
                }

                //
                if (this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.1 + (0.028 * (this.variables[i].level % 10) - 0.12);
                } else {
                    this.variableWeights[7] = 10.20;
                    this.variableWeights[6] = 11.10 + (0.028 * (this.variables[i].level % 10) - 0.12);
                }

            } else if (this.strategy.name.equalsIgnoreCase("T4C3")) {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 10.0;
                this.variableWeights[7] = 10.0;
            } else if (this.strategy.name.equalsIgnoreCase("T4Baby")
                    || this.strategy.name.equalsIgnoreCase(("T4NoMS"))) {

                if (this.maxRho < 75 && this.publicationMark < 75) {
                    this.variableWeights[0] = 10.80 + 0.015 * (this.variables[0].level % 10);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 100.8;
                    this.variableWeights[3] = 100.0;
                    this.variableWeights[4] = 100.8;
                    this.variableWeights[5] = 100.0;
                    this.variableWeights[6] = 100.8;
                    this.variableWeights[7] = 100.0;
                
                } else if (this.maxRho < 100 && this.publicationMark < 100) {
                    if(this.maxRho < this.publicationMark * this.switchMSRatio[0]) {
                        this.variableWeights[0] = 10.9;
                        this.variableWeights[1] = 10.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 100.0;
                        this.variableWeights[4] = 100.8;
                        this.variableWeights[5] = 100.0;
                        this.variableWeights[6] = 100.8;
                        this.variableWeights[7] = 100.0;
                    } else {
                        this.variableWeights[0] = 100.8;
                        this.variableWeights[1] = 100.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 10.0;
                        this.variableWeights[4] = 10.0;
                        this.variableWeights[5] = 10.0;
                        this.variableWeights[6] = 10.7 + 0.015 * this.variables[6].level % 10;
                        this.variableWeights[7] = 9.9;
                    }

                } else if (this.maxRho < 125 && this.publicationMark < 125) {
                    if(this.maxRho < this.publicationMark * switchMSRatio[1]) {
                        this.variableWeights[0] = 10.9;
                        this.variableWeights[1] = 10.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 100.0;
                        this.variableWeights[4] = 100.8;
                        this.variableWeights[5] = 100.0;
                        this.variableWeights[6] = 100.8;
                        this.variableWeights[7] = 100.0;
                    } else {
                        this.variableWeights[0] = 100.8;
                        this.variableWeights[1] = 100.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 10.0;
                        this.variableWeights[4] = 10.0;
                        this.variableWeights[5] = 10.0;
                        this.variableWeights[6] = 10.8;
                        this.variableWeights[7] = 9.9;
                    }

                } else if (this.maxRho < 150 && this.publicationMark < 150) {
                    if(this.maxRho < this.publicationMark * switchMSRatio[2]) {
                        this.variableWeights[0] = 10.9;
                        this.variableWeights[1] = 10.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 100.0;
                        this.variableWeights[4] = 100.8;
                        this.variableWeights[5] = 100.0;
                        this.variableWeights[6] = 100.8;
                        this.variableWeights[7] = 100.0;
                    } else {
                        this.variableWeights[0] = 100.8;
                        this.variableWeights[1] = 100.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 10.0;
                        this.variableWeights[4] = 10.0;
                        this.variableWeights[5] = 10.0;
                        this.variableWeights[6] = 10.8;
                        this.variableWeights[7] = 9.9;
                    }

                } else {
                    if(this.maxRho < this.publicationMark * 0.9) {
                        this.variableWeights[0] = 10.9;
                        this.variableWeights[1] = 10.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 100.0;
                        this.variableWeights[4] = 100.8;
                        this.variableWeights[5] = 100.0;
                        this.variableWeights[6] = 100.8;
                        this.variableWeights[7] = 100.0;
                    } else {
                        this.variableWeights[0] = 100.8;
                        this.variableWeights[1] = 100.0;
                        this.variableWeights[2] = 100.8;
                        this.variableWeights[3] = 10.0;
                        this.variableWeights[4] = 10.0;
                        this.variableWeights[5] = 10.0;
                        this.variableWeights[6] = 10.8;
                        this.variableWeights[7] = 9.9;
                    }
                }

            }

            if (this.publicationMultiplier > this.coastingPubs[3]) {
                for (int j = 0; j < this.variables.length; j++) {
                    this.variables[j].deactivate(); // autobuy for the variable off.
                    this.isCoasting = true;
                }
            }

        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory4 theory4, double variableCost) {
        while (theory4.rho < variableCost) {
            theory4.moveTick();
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

        System.out.print(String.format("%.4f", this.variables[0].value * 1.15 + this.variables[1].value));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.variables[2].value + this.q));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.variables[3].value + 2 * this.q));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.variables[4].value + 3 * this.q));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.variables[5].value + 4 * this.q));
        System.out.print("\t");

        System.out.print(String.format("%.2f", this.maxRho) + "\t" +
                String.format("%.2f", this.q) + "\t" + "\t" + this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}