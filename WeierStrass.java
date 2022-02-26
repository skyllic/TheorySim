import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;

/**
 * An implementation of the first custom theory, Weierstrass Sine Product from
 * the game Exponential Idle.
 */
public class WeierStrass extends Theory {

    // Equation variables
    public double q;
    public double qdot;
    public double chi;
    public double product; // Sn(chi) product as shown in the equation in game.
    public double coastingPub = 10.5;
    public boolean isCoasting;

    // Miscellaneous variables

    public double[] variableWeights = { 10, 10, 10, 10, 10 };

    public WeierStrass(double pubMark) {
        super(10, pubMark);
        this.name = "Weierstrass";

        this.q = Math.log10(1);
        this.chi = 0;
        this.variables = new Variable[5];
        this.strategy = new Strategy("", "");
        this.isCoasting = false;

        // Order of variable is q1, q2, n, c1, c2 (same as in game when read top to
        // bottom).
        this.variables[0] = new Variable(Math.pow(2, 3.38 / 4), 10, Math.pow(2, 0.1), 0, false, true, false, true,
                false, new double[2]);
        this.variables[1] = new Variable(Math.pow(2, 3.38 * 3), 1000, 2, 1, true, true, false, false, false,
                new double[2]);
        this.variables[2] = new Variable(Math.pow(2, 3.38), 20, 1, 0, false, false, true, false, false, new double[2]);
        this.variables[3] = new Variable(Math.pow(2, 3.38 / 1.5), 50, Math.pow(2, 1 / 50.0), 1, true, true, false,
                false, true, new double[2]);
        this.variables[4] = new Variable(Math.pow(2, 3.38 * 10), Math.pow(10, 10), 2, 1, true, true, false, false,
                false, new double[2]);

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, qdot.
     */
    public void moveTick() {

        this.updateEquation(); // theory equation related updates.

        super.moveTick();
        
        this.publicationMultiplier = Math.pow(10, 0.15 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     * <p>
     * Note that Sn(chi) and chi are not updated in this method, as this method is
     * called every tick.
     * Sn(chi) and chi are updated separately when a variable is bought.
     * </p>
     */
    public void updateEquation() {
        // Updates rhodot
        this.rhodot = this.variables[0].value * 1.04 + this.variables[1].value + this.q +
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;

        this.qdot = this.variables[4].value + this.product - Math.log10(Math.abs(Math.sin(this.chi))) +
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
        this.q = Variable.add(this.q, this.qdot);
        this.rho = Variable.add(this.rho, this.rhodot);

    }

    /**
     * Calculates the chi value used in the equation. This is the actual
     * (non-logged) value.
     * 
     * @return chi - the chi value used in the equation.
     */
    public double calculateChi() {
        double numerator = (Math.PI) * Math.pow(10, this.variables[3].value) *
                this.variables[2].value;
        double denominator = Math.pow(10, this.variables[3].value) +
                this.variables[2].value / 27.0;
        double chi = numerator / denominator + 1;

        return chi;
    }

    /**
     * Calculates sn(chi) used in the equation.
     * The products are written in log10 notation, while chi is the actual value
     * (non-logged).
     * 
     * @return product - the product sn(chi).
     */
    public double calculateSn() {
        this.product = Math.log10(1);

        for (int i = 1; i <= this.variables[2].value; i++) {
            double producant = 1 - Math.pow((this.chi / i / Math.PI), 2);
            producant = Math.abs(producant);
            producant = Math.log10(producant);
            this.product += producant;
        }
        this.product += Math.log10(this.chi);

        return this.product;
    }

    /**
     * Buys 1 level of the variable according to the variableNumber input. For
     * example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * <p>
     * Also re-calculates chi and Sn(chi) as they change after a variable is bought.
     * </p>
     * 
     * @param variableNumber - the variable number to buy. Note that the variable
     *                       number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        double variableCost = this.variables[variableNumber].nextCost;
        super.buyVariable(variableNumber); // default behaviour.

        
            // Re-calculates chi and Sn(chi).
            this.chi = this.calculateChi();
            this.product = this.calculateSn();
        
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
    @Override
    public int findBestVarToBuy() {
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
            if (this.variables[i].isActive == 1) {
                // Time spent in the initial recovery stage is negligible. Prevents
                // initialisation errors.
                while (this.variables[i].cost < this.publicationMark * 0.9) {
                    this.variables[i].level += 1;
                    this.variables[i].update();

                }
            }
            this.chi = this.calculateChi();
            this.product = this.calculateSn();
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

    /**
     * Calculates variable weightings used to decide which variable to buy. This
     * method is the core method of
     * each strategy. Variable weightings differ strategy by strategy. Higher
     * variable weightings indicate
     * lower priority. For every 1 increase in variable weightings, its priority is
     * reduced 10 folds.
     * <p>
     * The priority list is calculated by using weighting * cost, where cost is
     * represented in log format.
     * </p>
     * 
     * @param i - dummy variable.
     */
    public void adjustWeightings(int i) {
        if (this.publicationMultiplier > 0.01) {
            this.tickFrequency = 1.0;
        }

        if (this.variables[i].isActive == 1) {

            if (this.strategy.name == "WSPlay") {

                this.variableWeights[0] = 11;
                this.variableWeights[0] = 10.85 + (0.028 * (this.variables[i].level % 10) - 0.16);
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 9.70;
                this.variableWeights[3] = 15;
                this.variableWeights[4] = 10.0;
                if(this.publicationMultiplier > 0.1) {
                    this.variables[3].deactivate();
                }

            } else if (this.strategy.name == "WSPlay2") {
                this.variableWeights[0] = 11;
                this.variableWeights[0] = 10.85 + (0.028 * (this.variables[i].level % 10) - 0.16);
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 9.70;
                this.variableWeights[3] = 15;
                this.variableWeights[4] = 10.0;


                this.variableWeights[0] = 11;
                this.variableWeights[0] = 10.85 + (0.028 * (this.variables[i].level % 10) - 0.16);
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 9.70;
                this.variableWeights[3] = 15;
                this.variableWeights[4] = 10.0;

                if(this.publicationMultiplier > 5) {
                    this.variableWeights[2] = 9.75;
                }



            } else if (this.strategy.name == "WSPd") {
                this.variableWeights[0] = 11.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 11.0;
                this.variableWeights[4] = 10.0;
            } else if (this.strategy.name == "WSPStC1") {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 18.0;
                this.variableWeights[4] = 10.0;
            } else if (this.strategy.name == "WSP") {
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
                
                //System.out.println(this.variables[j].level + "\t" + this.coastStart);
            }
        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(WeierStrass weierstrass, double variableCost) {
        while (weierstrass.rho < variableCost) {
            weierstrass.moveTick();
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
        System.out.print(String.format("%.2f", this.chi) + "\t" +
                String.format("%.2f", this.variables[2].value) + "\t" + String.format("%.2f", this.product) + "\t"
                + this.publicationMultiplier);
        System.out.println("");
    }




}