package sim.theory;

import java.util.ArrayList;
import java.util.Collections;

import sim.upgrades.Variable;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Theory5 extends Theory {

    public double qdot;
    public double q;
    public double qMax;

    public boolean c2c3New = false;
    public boolean isCoasting;

    public double coastingPub = 6.0;

    public double[] variableWeights = { 10, 10, 10, 10, 10 };
    // public double[] variableWeights = {10,10,10,10,10,10,11,10};

    public Theory5(double pubMark) {
        super(5, pubMark);

        this.name = "Logistic Function";

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.q = 0;
        this.qdot = Double.MAX_VALUE;
        this.isCoasting = false;

        this.variables = new Variable[5];
        

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(1.61328, 10, Math.pow(2, 0.1), 0, false, true, false, true, false,
                new double[2]);
        this.variables[1] = new Variable(64, 15, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(1.18099, Math.pow(10, 6), 2, 1, false, true, false, false, true,
                new double[2]);
        this.variables[3] = new Variable(4.53725, 75, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(88550700, Math.pow(10, 3), 2, 1, true, true, false, false, false,
                new double[2]);

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();

        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.159 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

        if (this.variables[4].value * 1.1 < this.q - this.variables[3].value) {
            this.q = this.variables[4].value * 1.1 + this.variables[3].value;
        }

        this.qdot = this.variables[2].value - this.variables[3].value + this.q +
                Variable.subtract(this.variables[4].value * 1.1, this.q - this.variables[3].value) +
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);

        this.q = Variable.add(this.q, Math.max(this.qdot, -Double.MAX_VALUE));
        this.q = Math.min(q, this.variables[4].value * 1.1 + this.variables[3].value);
        this.rhodot = this.variables[0].value * 1.15 + this.variables[1].value + this.q +
                Math.log10(this.tickFrequency) + Math.log10(Theory.adBonus) + this.totalMultiplier;
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

        if(this.variableWeights[variableNumber] < 10.11) {

        } else {
            this.resetIdlePeriod(variableNumber);
        }

        double variableCost = this.variables[variableNumber].nextCost;
        if (this.rho >= variableCost) {

            if (variableNumber == 3 || variableNumber == 4 && this.variables[3].level > 880) {
                this.c2c3New = true;

            }

            this.variables[variableNumber].level += 1;
            this.variables[variableNumber].update();
            this.rho = Variable.subtract(this.rho, variableCost);

            if (variableNumber == 3 || variableNumber == 4) {
                this.c2c3New = true;

            }
            if (variableNumber == 0 || variableNumber == 1) {
                this.c2c3New = false;
            }

            // System.out.println(this.getIntegral() + "\t" + this.c);

        } else {
            // too poor to buy stuff feelsbadman
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
                while (this.variables[i].cost < this.publicationMark * 0.9) {
                    this.variables[i].level += 1;
                    this.variables[i].update();
                    this.q = this.variables[4].value * 1.1 + this.variables[3].value;
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

        // variableWeights = {1000,1000,10,10,10,10,10.0,10.2};

        if (this.variables[i].isActive == 1) {

            if (this.strategy.name.equalsIgnoreCase("T5Play")) {

                //double q1DefaultWeight = 10.8;
                variableWeights[1] = 10.0;
                variableWeights[2] = 12.0;
                variableWeights[3] = 10.0;
                variableWeights[4] = 10.0;

                double c1Adjustment = 0;
                double q1Adjustment = 0;

                this.qMax = this.variables[4].value * 1.1 + this.variables[3].value;

                if (this.c2c3New == true) {
                    c1Adjustment = -0.1;
                } else {
                    c1Adjustment = 0;
                }

                if (this.publicationMark * 0.97 > this.maxRho) {
                    variableWeights[2] = 11.0 + c1Adjustment;
                    variableWeights[3] = 12.0;
                }
                if (this.publicationMark * 0.98 > this.maxRho) {
                    q1Adjustment = -0.0;
                    variableWeights[2] = 11.0 + c1Adjustment;
                    variableWeights[3] = 10.0;

                }
                if (this.publicationMark * 0.99 > this.maxRho) {
                    q1Adjustment = -0.0;
                    variableWeights[2] = 12.0 + c1Adjustment;
                    variableWeights[3] = 10.0;
                    variableWeights[4] = 9.95;
                }
                if (this.publicationMark * 1.0 > this.maxRho) {
                    q1Adjustment = 0.0;
                    variableWeights[2] = 12.0 + c1Adjustment;
                    variableWeights[3] = 10.0;
                    variableWeights[4] = 9.95;
                }
                if (this.publicationMark * 1.0 < this.maxRho) {
                    q1Adjustment = -0.0;
                    variableWeights[2] = 13.0 + c1Adjustment;
                    variableWeights[3] = 10.0;
                    variableWeights[4] = 9.95;
                }
                variableWeights[0] = 10.8 + (0.018 * (this.variables[0].level % 10) - 0.11) + q1Adjustment;

            } else if (this.strategy.name.equalsIgnoreCase("T5")) {

                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;

            } else if(this.strategy.name.equalsIgnoreCase("T5PlayI")) {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
                if(this.publicationMultiplier > 0.1) {
                    this.variableWeights[2] = 100.0;
                }
            }

        }

        if (this.publicationMultiplier > this.coastingPubs[4]) {
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory5 theory5, double variableCost) {
        while (theory5.rho < variableCost) {
            theory5.moveTick();
        }
    }

    @Override
    public void display() {
        // System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" +
        // this.tickNumber);
        System.out.print(String.format("%.3f", this.seconds / 60.0 / 60.0) + "\t");
        for (int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }

        System.out.print(String.format("%.2f", this.maxRho) + "\t" +
                String.format("%.2f", this.q) + "\t" + "\t" + this.publicationMultiplier);
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

}