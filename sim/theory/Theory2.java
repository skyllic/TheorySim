package sim.theory;

import java.util.ArrayList;

import java.util.Collections;

import sim.upgrades.Variable;

/**
 * An implementation of Theory 2 (Differential Calculus) from the game
 * Exponential
 * Idle.
 */
public class Theory2 extends Theory {

    public double q1;
    public double q2;
    public double q3;
    public double q4;
    public double r1;
    public double r2;
    public double r3;
    public double r4;
    public double rdot;
    public double qdot;

    public int[] milestoneLevels = new int[4];

    public double coastingPub = 800;

    public double[] variableWeights = { 10, 10, 10, 10, 10, 10, 10, 10 };

    public Theory2(double pubMark) {
        super(2, pubMark);
        this.name = "Differential Calculus";

        this.q1 = 0;
        this.q2 = 0;
        this.q3 = 0;
        this.q4 = 0;
        this.r1 = 0;
        this.r2 = 0;
        this.r3 = 0;
        this.r4 = 0;

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.isCoasting = false;

 

        // Default all milestones.
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 2;
        this.milestoneLevels[2] = 3;
        this.milestoneLevels[3] = 3;

        

    }

    /**
     * Moves the theory by 1 tick (default is 1.0 second for every theory except theory 2, 
     * where it is 10 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();

        this.milestoneSwapCheck();

        super.moveTick();

        
        
        this.publicationMultiplier = Math.pow(10, 0.198 * (this.maxRho - this.publicationMark));

    }

    public void milestoneSwapCheck() {
        
        if(this.strategy.name.equalsIgnoreCase("T2MS") && !this.isCoasting) {
            if(this.maxRho < 25 && this.publicationMark < 25) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 50 && this.publicationMark < 50) {
                if(this.tickCount % 100 < 49 && (this.maxRho < 25 + Math.log10(3))) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 49 && (this.maxRho >= 25 + Math.log10(3))) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                }else if(this.tickCount % 100 < 50) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 99 && (this.maxRho < 25 + Math.log10(4))) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 99 && (this.maxRho >= 25 + Math.log10(4))) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 2;
                    this.milestoneLevels[3] = 0;
                }
                
            } else if(this.maxRho < 75 && this.publicationMark < 75) {
                if(this.tickCount % 100 < 45 && (this.maxRho < 50 + Math.log10(8))) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 45 && (this.maxRho >= 50 + Math.log10(8))) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                }else if(this.tickCount % 100 < 55) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 2;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 95 && (this.maxRho < 50 + Math.log10(5))) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 95 && (this.maxRho >= 50 + Math.log10(5))) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 2;
                    this.milestoneLevels[3] = 0;
                }
            } else if(this.maxRho < 100 && this.publicationMark < 100) {
                if(this.tickCount % 100 < 45) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 50) {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 0;
                } else if(this.tickCount % 100 < 95) {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 0;
                }
            } else if(this.maxRho < 125 && this.publicationMark < 125) {
                if(this.tickCount % 100 < 86) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 0;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 1;
                } 
            } else if(this.maxRho < 150 && this.publicationMark < 150) {
                if(this.tickCount % 100 < 74) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 1;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 2;
                } 
            } else if(this.maxRho < 175 && this.publicationMark < 175) {
                if(this.tickCount % 100 < 80) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 2;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 0;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 3;
                } 
            } else if(this.maxRho < 200 && this.publicationMark < 200) {
                if(this.tickCount % 100 < 80) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 0;
                } else {
                    this.milestoneLevels[0] = 1;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 3;
                } 
            } else if(this.maxRho < 225 && this.publicationMark < 225) {
                if(this.tickCount % 100 < 75) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 1;
                } else {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 0;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 3;
                } 
            } else if(this.maxRho < 250 && this.publicationMark < 250) {
                if(this.tickCount % 100 < 66) {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 2;
                } else {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 1;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 3;
                } 
            } else {
                    this.milestoneLevels[0] = 2;
                    this.milestoneLevels[1] = 2;
                    this.milestoneLevels[2] = 3;
                    this.milestoneLevels[3] = 3;
            }
        } else if(this.strategy.name == "T2NoMS") {
            if(this.maxRho < 25 && this.publicationMark < 25) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;

            } else if(this.maxRho < 50 && this.publicationMark < 50) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;

            } else if(this.maxRho < 75 && this.publicationMark < 75) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 100 && this.publicationMark < 100) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 125 && this.publicationMark < 125) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 150 && this.publicationMark < 150) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 1;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 175 && this.publicationMark < 175) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 2;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 200 && this.publicationMark < 200) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 225 && this.publicationMark < 225) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 1;
            } else if(this.maxRho < 250 && this.publicationMark < 250) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 2;
            } else {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            }
        }
    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

        this.q1 = Variable.add(this.q1,
                this.variables[0].value + this.q2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));

        if (this.milestoneLevels[0] > 0) {
            this.q2 = Variable.add(this.q2,
                    this.variables[1].value + this.q3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            this.q2 = Variable.add(this.q2,
                    this.variables[1].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        }

        if (this.milestoneLevels[0] > 1) {
            this.q3 = Variable.add(this.q3,
                    this.variables[2].value + this.q4 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else if (this.milestoneLevels[0] == 1) {
            this.q3 = Variable.add(this.q3,
                    this.variables[2].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            // Do nothing with q3.
        }
        if (this.milestoneLevels[0] > 1) {
            this.q4 = Variable.add(this.q4,
                    this.variables[3].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            // Do nothing with q4.
        }

        this.r1 = Variable.add(this.r1,
                this.variables[4].value + this.r2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));

        if (this.milestoneLevels[1] > 0) {
            this.r2 = Variable.add(this.r2,
                    this.variables[5].value + this.r3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            this.r2 = Variable.add(this.r2,
                    this.variables[5].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        }

        if (this.milestoneLevels[1] > 1) {
            this.r3 = Variable.add(this.r3,
                    this.variables[6].value + this.r4 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else if (this.milestoneLevels[1] == 1) {
            this.r3 = Variable.add(this.r3,
                    this.variables[6].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            // Do nothing with r3.
        }

        if (this.milestoneLevels[1] > 1) {
            this.r4 = Variable.add(this.r4,
                    this.variables[7].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            // Do nothing with r4.
        }

        this.rhodot = this.q1 * (1 + 0.05 * this.milestoneLevels[2]) + this.r1 * (1 + 0.05 * this.milestoneLevels[3]) +
                Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency)
                + (this.totalMultiplier);

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
        super.buyVariable(variableNumber);

        if(this.variableWeights[variableNumber] < 10.11) {

        } else {
            this.resetIdlePeriod(variableNumber);
        }

    }

    public void runUntilPublish() {
        while(this.finishCoasting == false) {
            this.runEpoch();
        }
    }

    public void runEpoch() {
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }
        int bestVarIndex = this.findBestVarToBuy();

        if (!isCoasting) {
            this.idleUntil(this, this.variables[bestVarIndex].nextCost);
            this.buyVariable(bestVarIndex);
            // this.display();
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
                while (this.variables[i].cost < this.publicationMark * 0.5) {
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

        if (this.variables[i].isActive == 1) {

            if (this.strategy.name.equalsIgnoreCase("T2AI")) {
                

                this.variableWeights[i] = 10 + (0.031 * (this.variables[i].level % 10));

                if(this.readyToCoast(1100)) {
                    this.variables[3].deactivate();
                    this.variables[7].deactivate();
                    
                }
                if(this.readyToCoast(2250)) {
                    this.variables[2].deactivate();
                    this.variables[6].deactivate();
                }
                if(this.readyToCoast(2900)) {
                    this.variables[1].deactivate();
                    this.variables[5].deactivate();
                }
                

            } else if (this.strategy.name.equalsIgnoreCase("T2")) {
                this.variableWeights[i] = 10.0;
            } else if(this.strategy.name.equalsIgnoreCase("T2MS")) {
                    this.variableWeights[i] = 10.0;
                    if(this.variables[i].level == 0) {
                        this.variableWeights[i] = 9.2;
                    }
                }
            } else if(this.strategy.name.equalsIgnoreCase("T2NoMS")) {
                this.variableWeights[i] = 10.0;

                
            }

        

        if (this.readyToCoast(2000)) {

            this.switchToCoastMS();
          
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

}

    private void switchToCoastMS() {

        if(this.maxRho < 25 && this.publicationMark < 25) {
            
        } else if(this.maxRho < 50 && this.publicationMark < 50) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 1;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 50) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 1;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 99) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 1;
                this.milestoneLevels[3] = 0;
            }
            
        } else if(this.maxRho < 75 && this.publicationMark < 75) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 2;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 55) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 2;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 95) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 2;
                this.milestoneLevels[3] = 0;
            }
        } else if(this.maxRho < 100 && this.publicationMark < 100) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 50) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.tickCount % 100 < 95) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            }
        } else if(this.maxRho < 125 && this.publicationMark < 125) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 1;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 1;
            } 
        } else if(this.maxRho < 150 && this.publicationMark < 150) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 2;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 2;
            } 
        } else if(this.maxRho < 175 && this.publicationMark < 175) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } else {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } 
        } else if(this.maxRho < 200 && this.publicationMark < 200) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } else {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } 
        } else if(this.maxRho < 225 && this.publicationMark < 225) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } else {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } 
        } else if(this.maxRho < 250 && this.publicationMark < 250) {
            if(this.tickCount % 100 < 100) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } else {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
            } 
        } else {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 3;
        }

    }
    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory2 theory2, double variableCost) {
        while (theory2.rho < variableCost) {
            theory2.moveTick();
        }
    }

    @Override
    public void display() {
        // System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" +
        // this.tickNumber);
        System.out.print(String.format("%.3f", this.seconds / 60.0 / 60.0 / 24.0) + "\t");
        for (int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(String.format("%.2f", this.maxRho) + "\t" +
                String.format("%.2f", this.q1) + "\t" + String.format("%.2f", this.r1) + "\t"
                + this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}