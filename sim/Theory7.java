package sim;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Theory7 extends Theory {

    public double rho1;
    public double rho2;
    public double rho1dot;
    public double rho2dot;

    double rho1dotTerm1 = 0;
    double rho1dotTerm2 = 0;
    double rho1dotTerm5 = 0;
    double rho2dotTerm3 = 0;
    double rho2dotTerm4 = 0;
    double rho2dotTerm5 = 0;

    public boolean isCoasting;
    public double coastingPub = 3.0;

    // public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = { 10.6, 10, 10, 10, 11.0, 10.6, 10.0 };

    public Theory7[] t7Clones = new Theory7[7];

    public Theory7(double pubMark) {
        super(7, pubMark);

        
        this.name = "Numerical Methods";

        this.seconds = 0;
        this.tickCount = 0;

        this.isCoasting = false;

        this.variables = new Variable[7];
        this.strategy = new Strategy("T7AI", "AI");

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(1.51572, 500, Math.pow(2, 0.1), 0, false, true, false, true, false,
                new double[2]);
        this.variables[1] = new Variable(1.275, 10, Math.pow(2, 0.1), 1, false, true, false, false, true,
                new double[2]);
        this.variables[2] = new Variable(8, 40, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(63, Math.pow(10, 5), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(2.82, Math.pow(10, 1), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[5] = new Variable(60, Math.pow(10, 8), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(2.81, 100, 2, 1, true, true, false, false, false, new double[2]);

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();
        this.rho = this.rho1;

        super.moveTick();

        if (this.rho1 > this.maxRho) {
            this.maxRho = this.rho1;
        }
        this.publicationMultiplier = Math.pow(10, 0.152 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

        this.rho1 = Variable.add(this.rho1, this.rho1dot);
        this.rho2 = Variable.add(this.rho2, this.rho2dot);

        this.rho1dotTerm1 = this.variables[1].value * 1.15 + this.variables[2].value;

        this.rho1dotTerm2 = this.variables[3].value + Math.log10(1.5) + 0.5 * this.rho1;

        this.rho1dotTerm5 = Math.min(this.rho1 + Math.log10(100),
                this.variables[6].value + Math.log10(0.5) + 0.5 * (Math.min(this.rho2 - this.rho1, 12.0)));

        this.rho2dotTerm3 = this.variables[4].value;
        this.rho2dotTerm4 = this.variables[5].value + Math.log10(1.5) + 0.5 * this.rho2;
        this.rho2dotTerm5 = Math.min(this.rho2 + Math.log10(100),
                this.variables[6].value + Math.log10(0.5) + 0.5 * Math.min((this.rho1 - this.rho2), 12.0));

        double sumRho1 = Variable.add(rho1dotTerm5, Variable.add(rho1dotTerm1, rho1dotTerm2));
        double sumRho2 = Variable.add(rho2dotTerm3, Variable.add(rho2dotTerm4, rho2dotTerm5));

        this.rho1dot = sumRho1 + this.variables[0].value + Math.log10(Theory.adBonus) + this.totalMultiplier
                + Math.log10(this.tickFrequency);
        this.rho2dot = sumRho2 + this.variables[0].value + Math.log10(Theory.adBonus) + this.totalMultiplier
                + Math.log10(this.tickFrequency);

        

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
        double variableCost = this.variables[variableNumber].nextCost;
        if (this.rho1 >= variableCost) {
            this.variables[variableNumber].level += 1;
            this.variables[variableNumber].update();
            this.rho1 = Variable.subtract(this.rho1, variableCost);

        } else {
            // too poor to buy stuff feelsbadman
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

    @Override
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

            // variableWeights = {10.6,10,10,10,11.0,10.6,10.0};
            if (this.strategy.name == "T7Play") {
                this.variableWeights[0] = 10.6 + (0.018 * (this.variables[0].level % 10) - 0.11);
                this.variables[1].deactivate();
                this.variables[2].deactivate();
                this.variables[3].deactivate();
                if(this.variables[4].cost + 1 < this.rho1) {
                    this.variableWeights[4] = 10.0;
                } else {
                    this.variableWeights[4] = 11.0;
                }
                //this.variableWeights[4] = 11.0;
                this.variableWeights[5] = 10.6;
                this.variableWeights[6] = 10.0;

                

            } else if(this.strategy.name == "T7PlaySpqcey") {
                this.variableWeights[0] = 10.6 + (0.018 * (this.variables[0].level % 10) - 0.11);
                this.variables[1].deactivate();
                this.variables[2].deactivate();
                this.variableWeights[3] = 11.0;
                this.variableWeights[4] = 11.0;
                this.variableWeights[5] = 10.6;
                this.variableWeights[6] = 10.0;

            } else if(this.strategy.name == "T7C456") {
                this.variableWeights[0] = 10.0;
                this.variables[1].deactivate();
                this.variables[2].deactivate();
                this.variables[3].deactivate();
                this.variableWeights[4] = 10.0;
                this.variableWeights[5] = 10.0;
                this.variableWeights[6] = 10.0;
            }

        }

        if (this.publicationMultiplier > this.coastingPub) {
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory7 theory7, double variableCost) {
        while (theory7.rho1 < variableCost) {
            theory7.moveTick();
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

        System.out.print(String.format("%.4f", this.rho1dotTerm5));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.rho2dotTerm3));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.rho2dotTerm4));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.rho2dotTerm5));
        System.out.print("\t");
        System.out.print(String.format("%.4f", this.rho2dotTerm4));
        System.out.print("\t");

        System.out.print(String.format("%.2f", this.maxRho) + "\t" + String.format("%.2f", this.rho1dot) + "\t" +
                this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}