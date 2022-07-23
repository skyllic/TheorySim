package sim.theory;

import java.util.ArrayList;
import java.util.Collections;

import sim.upgrades.Variable;

/**
 * An implementation of Theory 8 (Chaos Theory) from the game Exponential
 * Idle.
 */
public class Theory8 extends Theory {

    public double x = -6;
    public double y = 15;
    public double z = 0;
    public double xdot;
    public double ydot;
    public double zdot;

    public double[] dt = new double[3];
    
    public double preE60SwapTau = 48;

    public double[] xAverage = {0, 0.5, 1};
    public double[] xMin = {-20, -23, -20};
    public double[] xMax = {20, 24, 22};
    public double[] yAverage = {0, 1, -1.5};
    public double[] yMin = {-27, -25, -21};
    public double[] yMax = {27, 27, 18};
    public double[] zAverage = {24.5, 20.5, 8};
    public double[] zMin = {1, 1, 0};
    public double[] zMax = {48, 40, 37};

    public double coastingPub = 3.0;
    public boolean isCoasting;

    public int[] milestoneLevels = new int[4];

    
    //public double[] variableWeights = { 10, 10, 10, 10, 10 };

    public Theory8[] t8Clones = new Theory8[5];

    public Theory8(double pubMark) {
        super(8, pubMark);

        this.name = "Chaos Theory";

        this.seconds = 0;
        this.tickCount = 0;

        this.isCoasting = false;

        

        this.variables = new Variable[5];
        

        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 3;
        this.milestoneLevels[2] = 3;
        this.milestoneLevels[3] = 3;

       
        this.resetStateDefault();

        this.dt[0] = 0.02; // lorentz
        this.dt[1] = 0.002; // chen
        this.dt[2] = 0.00014;// rossler

        // Order of variable is c1, c2, c3, c4, c5
        this.variables[0] = new Variable(1.5172, 10, Math.pow(2, 0.1), 0, false, true, false, true, false,
                new double[2]);
        this.variables[1] = new Variable(64, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(Math.pow(3, 1.15), 100, 3, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(Math.pow(5, 1.15), 100, 5, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(Math.pow(7, 1.15), 100, 7, 1, true, true, false, false, false, new double[2]);

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables.
     */
    public void moveTick() {
        //
        if(this.strategy.name.equalsIgnoreCase("T8SolarS") && (this.tickCount) % 335 == 0 ) {
            this.resetStateDefault();
        } else if(this.strategy.name.equalsIgnoreCase("T8MS2"))  {

            if(this.variables[3].value > this.variables[4].value + 0) {
                if((this.tickCount) % 335 == 0) {
                    this.resetStateDefault();
                }
            } else {
                if((this.tickCount) % 335 == 0) {
                    this.resetStateDefault();
                }
            }

            
        
        } else if(this.strategy.name.equalsIgnoreCase("T8Baby") && (this.tickCount) % 335 == 0 
            && this.maxRho > 40 && this.maxRho < preE60SwapTau
                 && this.publicationMark > 40 && this.publicationMark < preE60SwapTau) {
            this.resetStateDefault();
        }

        this.updateEquation();
        this.milestoneSwapCheck();
        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.15 * (this.maxRho - this.publicationMark));

    }


    public void milestoneSwapCheck() {
        if(this.strategy.name.equalsIgnoreCase("T8Baby")) {
            if(this.maxRho < 20 && this.publicationMark < 20) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 40 && this.publicationMark < 40) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
                
            } else if(this.maxRho < preE60SwapTau && this.publicationMark < preE60SwapTau) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 60 && this.publicationMark< 60) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 2;
            } 
            else if(this.maxRho < 80 && this.publicationMark < 80) {
                this.milestoneLevels[0] = 0;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 3;
            } else if(this.maxRho < 100 && this.publicationMark < 100) {
                this.milestoneLevels[0] = 1;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 0;
                this.milestoneLevels[3] = 3;
            } else if(this.maxRho < 120 && this.publicationMark < 120) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 0;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 140 && this.publicationMark < 140) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 1;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 160 && this.publicationMark < 160) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 2;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 180 && this.publicationMark < 180) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 3;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 0;
            } else if(this.maxRho < 200 && this.publicationMark < 200) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 3;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 1;
            } else if(this.maxRho < 220 && this.publicationMark < 220) {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 3;
                this.milestoneLevels[2] = 3;
                this.milestoneLevels[3] = 2;
            } else {
                this.milestoneLevels[0] = 2;
                this.milestoneLevels[1] = 3;
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

        double xLowerBound = (xMin[this.milestoneLevels[0]] - xAverage[this.milestoneLevels[0]]) 
            * 5 + xAverage[this.milestoneLevels[0]];
        double xUpperBound = (xMax[this.milestoneLevels[0]] - xAverage[this.milestoneLevels[0]])
             * 5 + xAverage[this.milestoneLevels[0]];
        double yLowerBound = (yMin[this.milestoneLevels[0]] - yAverage[this.milestoneLevels[0]])
             * 5 + yAverage[this.milestoneLevels[0]];
        double yUpperBound = (yMax[this.milestoneLevels[0]] - yAverage[this.milestoneLevels[0]])
             * 5 + yAverage[this.milestoneLevels[0]];
        double zLowerBound = (zMin[this.milestoneLevels[0]] - zAverage[this.milestoneLevels[0]])
             * 5 + zAverage[this.milestoneLevels[0]];
        double zUpperBound = (zMax[this.milestoneLevels[0]] - zAverage[this.milestoneLevels[0]])
             * 5 + zAverage[this.milestoneLevels[0]];

        if (this.x < xLowerBound || this.x > xUpperBound ||
                this.y < yLowerBound || this.y > yUpperBound ||
                this.z < zLowerBound || this.z > zUpperBound) {
            this.resetStateDefault();
        }

        if(this.milestoneLevels[0] == 2) {
            this.xdot = -500 * (this.y + this.z);
            this.ydot = 500 * this.x + 50 * this.y;
            this.zdot = 50 + 500 * this.z * (this.x - 14);
        } else if(this.milestoneLevels[0] == 1) {
            this.xdot = 400 * (this.y - this.x);
            this.ydot = -120 * this.x - 10 * this.x * this.z + 280 * this.y;
            this.zdot = 10 * this.x * this.y - 30 * this.z;
        } else {
            this.xdot = 10 * (this.y - this.x);
            this.ydot = this.x * (28 - this.z) - this.y;
            this.zdot = this.x * this.y - this.z * 8.0/3.0;
        }
       
        double midPointX = 0;
        double midPointY = 0;
        double midPointZ = 0;
        if(this.milestoneLevels[0] == 2) {
            // midpoint
            midPointX = this.x + this.xdot * dt[2] * 0.5;
            midPointY = this.y + this.ydot * dt[2] * 0.5;
            midPointZ = this.z + this.zdot * dt[2] * 0.5;
        } else if(this.milestoneLevels[0] == 1) {
            midPointX = this.x + this.xdot * dt[1] * 0.5;
            midPointY = this.y + this.ydot * dt[1] * 0.5;
            midPointZ = this.z + this.zdot * dt[1] * 0.5;
        } else {
            midPointX = this.x + this.xdot * dt[0] * 0.5;
            midPointY = this.y + this.ydot * dt[0] * 0.5;
            midPointZ = this.z + this.zdot * dt[0] * 0.5;
        }
        


        if(this.milestoneLevels[0] == 2) {
            this.xdot = -500 * (midPointY + midPointZ);
            this.ydot = 500 * midPointX + 50 * midPointY;
            this.zdot = 50 + 500 * midPointZ * (midPointX - 14);
        } else if(this.milestoneLevels[0] == 1) {
            this.xdot = 400 * (midPointY - midPointX);
            this.ydot = -120 * midPointX - 10 * midPointX * midPointZ+ 280 * midPointY;
            this.zdot = 10 * midPointX * midPointY - 30 * midPointZ;
        } else {
            this.xdot = 10 * (midPointY - midPointX);
            this.ydot = midPointX * (28 - midPointZ) - midPointY;
            this.zdot = midPointX* midPointY - midPointZ * 8.0/3.0;
        }
        
        if(this.milestoneLevels[0] == 2) {
            // actual
            this.x += this.xdot * dt[2];
            this.y += this.ydot * dt[2];
            this.z += this.zdot * dt[2];
        } else if(this.milestoneLevels[0] == 1) {
            this.x += this.xdot * dt[1];
            this.y += this.ydot * dt[1];
            this.z += this.zdot * dt[1];
        } else {
            this.x += this.xdot * dt[0];
            this.y += this.ydot * dt[0];
            this.z += this.zdot * dt[0];
        }

        

        if(this.milestoneLevels[0] == 2) {
            this.xdot = -500 * (this.y + this.z);
            this.ydot = 500 * this.x + 50 * this.y;
            this.zdot = 50 + 500 * this.z * (this.x - 14);
        } else if(this.milestoneLevels[0] == 1) {
            this.xdot = 400 * (this.y - this.x);
            this.ydot = -120 * this.x - 10 * this.x * this.z + 280 * this.y;
            this.zdot = 10 * this.x * this.y - 30 * this.z;
        } else {
            this.xdot = 10 * (this.y - this.x);
            this.ydot = this.x * (28 - this.z) - this.y;
            this.zdot = this.x * this.y - this.z * 8.0/3.0;
        }
        

        double term1 = this.variables[2].value * (1 + 0.05 * this.milestoneLevels[1]) 
            + Math.log10(Math.pow(this.xdot, 2));
        double term2 = this.variables[3].value * (1 + 0.05 * this.milestoneLevels[2]) 
            + Math.log10(Math.pow(this.ydot, 2));
        double term3 = this.variables[4].value * (1 + 0.05 * this.milestoneLevels[3]) 
            + Math.log10(Math.pow(this.zdot, 2));

        this.rhodot = 0.5 * (Variable.add(term3, Variable.add(term1, term2))) +
                this.variables[0].value + this.variables[1].value - Math.log10(100) +
                Math.log10(Theory.adBonus) + this.totalMultiplier + Math.log10(this.tickFrequency);

        this.rho = Variable.add(this.rho, this.rhodot);

    }

    public void resetStateDefault() {
        if(this.milestoneLevels[0] == 2) {
            this.x = -6;
            this.y = 15;
            this.z = 0;
        } else if(this.milestoneLevels[0] == 1) {
            this.x = -10.6;
            this.y = -4.4;
            this.z = 28.6;
        } else {
            this.x = -6;
            this.y = -8;
            this.z = 26;
        }

        this.resetIdlePeriod(2);
        
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

        if(this.variableWeights[variableNumber] < 10.11) {

        } else {
            this.resetIdlePeriod(variableNumber);
        }

        super.buyVariable(variableNumber);

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
            

        } else {// is coasting, stop buying any variable.
            // this.idleUntil(this, this.publicationMark + Math.log(2.5) / Math.log(10) /
            // 0.152);
            this.coastUntilPublish();
            // this.display();
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

            if (this.strategy.name.equalsIgnoreCase("T8Play")) {

                // this.variableWeights[0] = 11;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.4;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.6;

                this.variableWeights[0] = 10.9 + (0.018 * (this.variables[0].level % 10) - 0.11);

            } else if(this.strategy.name.equalsIgnoreCase("T8SolarS")) {
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.4;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.6;

                this.variableWeights[0] = 10.9 + (0.018 * (this.variables[0].level % 10) - 0.11);
            } 
            else if(this.strategy.name.equalsIgnoreCase("T8MS2")) {
                // this.variableWeights[0] = 11;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.34;
                this.variableWeights[3] = 10.06;
                this.variableWeights[4] = 10.4;

                this.variableWeights[0] = 10.73 + (0.029 * (this.variables[0].level % 10));
            }
            else if(this.strategy.name.equalsIgnoreCase("T8")) {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
            }
            else if(this.strategy.name.equalsIgnoreCase("T8Baby")) {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;

                if(this.maxRho < 20 && this.publicationMark < 20) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 10.0;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 10.0; 
                } else if(this.maxRho < 40 && this.publicationMark < 40) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 10.0;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 10.0; 

                } else if(this.maxRho < 47.8 && this.publicationMark < 47.8) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 10.4;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 10.6; 
                }
                else if(this.maxRho < 60 && this.publicationMark < 60) {
                    this.variableWeights[0] = 10.68 + (0.030 * (this.variables[0].level % 10));
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 13.0;
                    this.variableWeights[3] = 13.0;
                    this.variableWeights[4] = 10.0; 
                } else if(this.maxRho < 80 && this.publicationMark < 80) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 12.0;
                    this.variableWeights[3] = 12.0;
                    this.variableWeights[4] = 10.0; 

                } else if(this.maxRho < 100 && this.publicationMark < 100) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 12.0;
                    this.variableWeights[3] = 12.0;
                    this.variableWeights[4] = 10.0; 

                } else if(this.maxRho < 160 && this.publicationMark < 160) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 12.0;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 12.0; 

                } else if(this.maxRho < 220 && this.publicationMark < 220) {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 10.4;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 12.0; 
            

                } else {
                    this.variableWeights[0] = 10.9 + (0.030 * (this.variables[0].level % 10) - 0.11);
                    this.variableWeights[1] = 10.0;
                    this.variableWeights[2] = 10.4;
                    this.variableWeights[3] = 10.0;
                    this.variableWeights[4] = 10.6;
                }
            }

        }
        if(this.strategy.name.equalsIgnoreCase("T8baby")) {
            if(this.publicationMultiplier < 60) {
                if (this.readyToCoast(4.35)) {
                    for (int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); // autobuy for the variable off.
                        this.isCoasting = true;
                    }
                } 
            } else {
                if (this.readyToCoast(2.54)) {
                    for (int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); // autobuy for the variable off.
                        this.isCoasting = true;
                    }
                } 
            }
            

        }
        else if (this.readyToCoast(2.54)) {
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

    }

    

    

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory8 theory8, double variableCost) {
        while (theory8.rho < variableCost) {
            theory8.moveTick();
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

        System.out.print(String.format("%.4f", this.xdot));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.ydot));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.zdot));
        System.out.print("\t");

        System.out.print(String.format("%.2f", this.maxRho) + "\t" + String.format("%.2f", this.rhodot) + "\t" +
                this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}