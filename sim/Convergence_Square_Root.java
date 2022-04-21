package sim;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
 * Idle.
 */
public class Convergence_Square_Root extends Theory {


    public double qdot;
    public double q;

    //public double tauPerHour;

    public boolean isCoasting;
    
    public int counter = 0; 

    public double coastingPub = 6.5;

    public double nTerm = 0;

    

    public double[] variableWeights = { 10, 10, 10, 10, 10};
    

   

    public Convergence_Square_Root(double pubMark) {
        super(12, pubMark);
        this.name = "Convergence to Square Root 2";

        

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.q = 0;
        this.qdot = Double.MAX_VALUE;
        this.isCoasting = false;
    
        

        this.variables = new Variable[5];
        this.strategy = new Strategy("", "");

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(5, 10, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(128, 15, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(16, 1000000, Math.pow(2, 0.1), 1, false, true, false, false, true, new double[2]);
        this.variables[3] = new Variable(256, 50, 1, 0, false, false, true, false, false, new double[2]);
        this.variables[4] = new Variable(Math.pow(10, 5.65), 1000, 2, 1, true, true, false, false, false, new double[2]);
        

    }

    /**
     * Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary
     * variables such as q, r,
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();
        super.moveTick();

        this.publicationMultiplier = Math.pow(10, 0.222 * (this.maxRho - this.publicationMark));
        if(this.tickCount % 100 == 0) {
            //this.display();
        }
        //this.display();

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {
        

       if(this.variables[3].level + this.variables[4].level == 0) {
        this.nTerm = Math.log10(1);
       } else if(this.variables[3].level + this.variables[4].level == 1) {
        this.nTerm = Math.log10(3);
       } else {
           this.nTerm = Math.log10(Math.sqrt(2) + 1) * (this.variables[3].level + this.variables[4].level)
            - Math.log10(2);
       }

       this.qdot = this.variables[2].value + this.variables[4].value * 2.00 + this.nTerm + 
        Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
       this.rhodot = this.variables[0].value * 1.15 + this.variables[1].value + this.q + 
        Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;

        this.q = Variable.add(this.q, this.qdot);
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

        super.buyVariable(variableNumber);

      

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
                while (this.variables[i].cost < this.publicationMark * 0.30) {
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

        

        if (this.variables[i].isActive == 1) {

            if (this.strategy.name == "CSR2") {
                
                //variableWeights = { 10, 10, 10, 10, 10};

            
            } else if(this.strategy.name == "CSR2d") {
                variableWeights[0] = 11.0;
                variableWeights[1] = 10.0;
                variableWeights[2] = 11.0;
                variableWeights[3] = 10.0;
                variableWeights[4] = 10.0;

            } else if(this.strategy.name == "CSRPlay") {
                variableWeights[0] = 10.9 + (0.018 * (this.variables[0].level % 10) - 0.11);
                variableWeights[1] = 10.0;
                variableWeights[2] = 11.0 + (0.018 * (this.variables[0].level % 10) - 0.11);
                variableWeights[3] = 9.8;
                variableWeights[4] = 9.8;
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
    public void idleUntil(Convergence_Square_Root CSR2, double variableCost) {
        while (CSR2.rho < variableCost) {
            CSR2.moveTick();
        }
    }

    

    @Override
    public void display() {
        // System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" +
        // this.tickNumber);
        System.out.print(String.format("%.3f", this.seconds / 60.0 / 60.0) + "\t");
        
         for(int i = 0; i < this.variables.length; i++) {
         System.out.print(this.variables[i].value + "\t");
         }
         System.out.print(this.rho);
         
        /** 
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
                String.format("%.2f", this.q) + "\t" + "\t" + this.publicationMultiplier);*/
        System.out.println("");
        // System.out.println(Arrays.toString(this.variableWeights));
    }

   

}