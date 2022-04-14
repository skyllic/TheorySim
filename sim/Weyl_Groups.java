package sim;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Weyl_Groups extends Theory {


    public double qdot;
    public double q;

    //public double tauPerHour;

    public boolean isCoasting;
    public int  q2check = 0;
    public int counter = 0; 

    public double coastingPub = 6.5;

    

    public double[] variableWeights = { 1000, 1000, 10, 10, 10, 10, 10.0, 10.2 };
    // public double[] variableWeights = {10,10,10,10,10,10,11,10};

    public Theory4[] t2Clones = new Theory4[8];

    public Weyl_Groups(double pubMark) {
        super(14, pubMark);
        this.name = "Weyl_Groups";

        

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.q = 0;
        this.qdot = Double.MAX_VALUE;
        this.isCoasting = false;
    
        

        this.variables = new Variable[8];
        this.strategy = new Strategy("", "");

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(1.305, 5, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(3.75, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(2.468, 2000, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(4.85, Math.pow(10, 4), 3, 1, true, true, false, false, false, new double[2]);
        

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();
        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.165 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

       

        double numerator = Math.log10(8) + this.variables[6].value + this.variables[7].value
            + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
        this.qdot = Math.min(numerator - Variable.add(Math.log10(1), this.q), 
            this.q + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + 1);

        this.q = Variable.add(this.q, this.qdot);
        //System.out.println(this.qdot + "\t"+ this.maxRho);


        /**double p = Variable.subtract(Variable.add(this.q, 0) * 2, 0);
        this.q = Variable.add(Variable.add(0, p), 
            Math.log10(16) + this.variables[6].value + this.variables[7].value + 
                Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency));
        this.q = this.q * 0.5;
        this.q = Variable.subtract(this.q, 0);*/

        double term1 = this.variables[0].value * 1.15 + this.variables[1].value;
        double term2 = this.variables[2].value + this.q;
        double term3 = this.variables[3].value + this.q * 2;
        double term4 = this.variables[4].value + this.q * 3;
        double term5 = this.variables[5].value + this.q * 4;

        
        

        //this.q = Variable.subtract((Variable.add(Variable.add(this.q, 0) * 2, Math.log10(16) + this.variables[6].value
          //      + this.variables[7].value + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency))) * 0.5, 0);

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

        super.buyVariable(variableNumber);

        if(variableNumber == 7) {
            this.q2check = 1;
            this.counter = 0;
        } else {
            this.q2check = 0;
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
                while (this.variables[i].cost < this.publicationMark * 0.95) {
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

            if (this.strategy.name == "T4C3d") {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 11.0;
                this.variableWeights[7] = 10.0;
            } else if(this.strategy.name == "T4PlaySpqcey") {
                

                if(this.maxRho < this.publicationMark - 12) {
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

                if(this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            }
            else if(this.strategy.name == "T4SolC") {
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

                if(this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            
            } else if(this.strategy.name == "T4Sol2") {
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

                if(this.publicationMultiplier < 1) {
                    q2Offset = 1.0;
                }

                q2Offset = Math.log10(q2Offset);
                this.variableWeights[7] = 10.00 + q2Offset;
            }
            else if(this.strategy.name == "T4Solar") {
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
            } 
            else if (this.strategy.name == "T4Gold") {
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

            } else if(this.strategy.name == "T4C3") {
                this.variables[0].deactivate();
                this.variables[1].deactivate();
                this.variableWeights[2] = 10.0;
                this.variables[3].deactivate();
                this.variables[4].deactivate();
                this.variables[5].deactivate();
                this.variableWeights[6] = 10.0;
                this.variableWeights[7] = 10.0;
            }

            if (this.publicationMultiplier > this.coastingPub) {
                for (int j = 0; j < this.variables.length; j++) {
                    this.variables[j].deactivate(); // autobuy for the variable off.
                    this.isCoasting = true;
                }
            }

        }

    }

    /** Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Weyl_Groups weyl_Groups, double variableCost) {
        while (weyl_Groups.rho < variableCost) {
            weyl_Groups.moveTick();
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