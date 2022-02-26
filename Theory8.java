import java.util.ArrayList;
import java.util.Collections;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Theory8 extends Theory {

    public double x = -6;
    public double y = 15;
    public double z = 0;
    public double xdot;
    public double ydot;
    public double zdot;

    public double dt = 0.00014;// rossler

    public double xAverage = 1;
    public double xMin = -20;
    public double xMax = 22;
    public double yAverage = -1.5;
    public double yMin = -21;
    public double yMax = 18;
    public double zAverage = 8;
    public double zMin = 0;
    public double zMax = 37;

    public double coastingPub = 3.0;
    public boolean isCoasting;

    // public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = { 10, 10, 10, 10, 10 };

    public Theory8[] t8Clones = new Theory8[5];

    public Theory8(double pubMark) {
        super(8, pubMark);

        this.name = "Chaos Theory";

        this.seconds = 0;
        this.tickCount = 0;

        this.isCoasting = false;

        this.variables = new Variable[5];
        this.strategy = new Strategy("T8AI", "AI");

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(1.5172, 10, Math.pow(2, 0.1), 0, false, true, false, true, false,
                new double[2]);
        this.variables[1] = new Variable(64, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(Math.pow(3, 1.15), 100, 3, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(Math.pow(5, 1.15), 100, 5, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(Math.pow(7, 1.15), 100, 7, 1, true, true, false, false, false, new double[2]);

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {
        if(this.strategy.name == "T8MS" && (this.tickCount) % 340 == 0 ) {
            this.resetStateDefault();
        }

        this.updateEquation();
        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.15 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

        double xLowerBound = (xMin - xAverage) * 5 + xAverage;
        double xUpperBound = (xMax - xAverage) * 5 + xAverage;
        double yLowerBound = (yMin - yAverage) * 5 + yAverage;
        double yUpperBound = (yMax - yAverage) * 5 + yAverage;
        double zLowerBound = (zMin - zAverage) * 5 + zAverage;
        double zUpperBound = (zMax - zAverage) * 5 + zAverage;

        if (this.x < xLowerBound || this.x > xUpperBound ||
                this.y < yLowerBound || this.y > yUpperBound ||
                this.z < zLowerBound || this.z > zUpperBound) {
            this.resetStateDefault();
        }

        this.xdot = -500 * (this.y + this.z);
        this.ydot = 500 * this.x + 50 * this.y;
        this.zdot = 50 + 500 * this.z * (this.x - 14);

        // midpoint
        double midPointX = this.x + this.xdot * dt * 0.5;
        double midPointY = this.y + this.ydot * dt * 0.5;
        double midPointZ = this.z + this.zdot * dt * 0.5;

        this.xdot = -500 * (midPointY + midPointZ);
        this.ydot = 500 * midPointX + 50 * midPointY;
        this.zdot = 50 + 500 * midPointZ * (midPointX - 14);

        // actual
        this.x += this.xdot * dt;
        this.y += this.ydot * dt;
        this.z += this.zdot * dt;

        this.xdot = -500 * (this.y + this.z);
        this.ydot = 500 * this.x + 50 * this.y;
        this.zdot = 50 + 500 * this.z * (this.x - 14);

        double term1 = this.variables[2].value * 1.15 + Math.log10(Math.pow(this.xdot, 2));
        double term2 = this.variables[3].value * 1.15 + Math.log10(Math.pow(this.ydot, 2));
        double term3 = this.variables[4].value * 1.15 + Math.log10(Math.pow(this.zdot, 2));

        this.rhodot = 0.5 * (Variable.add(term3, Variable.add(term1, term2))) +
                this.variables[0].value + this.variables[1].value - Math.log10(100) +
                Math.log10(Theory.adBonus) + this.totalMultiplier + Math.log10(this.tickFrequency);

        this.rho = Variable.add(this.rho, this.rhodot);

    }

    public void resetStateDefault() {
        this.x = -6;
        this.y = 15;
        this.z = 0;
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

            if (this.strategy.name == "T8Play") {

                // this.variableWeights[0] = 11;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.4;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.6;

                this.variableWeights[0] = 10.9 + (0.018 * (this.variables[0].level % 10) - 0.11);

            } else if(this.strategy.name == "T8MS") {
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.4;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.6;

                this.variableWeights[0] = 10.9 + (0.018 * (this.variables[0].level % 10) - 0.11);
            } 
            else if(this.strategy.name == "T8") {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.0;
                this.variableWeights[4] = 10.0;
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