package sim;
import java.util.ArrayList;

import java.util.Collections;

/**
 * An implementation of Theory 6 (Integral Calculus) from the game Exponential
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

        this.variables = new Variable[8];
        this.strategy = new Strategy("T2AI", "AI");

        //Default all milestones.
        milestoneLevels[0] = 2;
        milestoneLevels[1] = 2;
        milestoneLevels[2] = 3;
        milestoneLevels[3] = 3;
       
      

        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when
        // read top to bottom)
        this.variables[0] = new Variable(2, 10, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(2, 5000, Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(3, 3 * Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[3] = new Variable(4, 8 * Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[4] = new Variable(2, 2 * Math.pow(10, 6), Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[5] = new Variable(2, 3 * Math.pow(10, 9), Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[6] = new Variable(3, 4 * Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false,
                new double[2]);
        this.variables[7] = new Variable(4, 5 * Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true, false, false, false,
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

        this.publicationMultiplier = Math.pow(10, 0.198 * (this.maxRho - this.publicationMark));

    }

    /**
     * Part of the moveTick() method. Updates equation values such as rho, rhodot,
     * q, qdot etc.
     */
    public void updateEquation() {

        this.q1 = Variable.add(this.q1,
                this.variables[0].value + this.q2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        
        if(this.milestoneLevels[0] > 0) {
                this.q2 = Variable.add(this.q2,
                this.variables[1].value + this.q3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            this.q2 = Variable.add(this.q2, 
                this.variables[1].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        }
        if(this.milestoneLevels[0] > 1) {
        this.q3 = Variable.add(this.q3,
                this.variables[2].value + this.q4 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        } else {
            
        }
                this.q4 = Variable.add(this.q4,
                this.variables[3].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.r1 = Variable.add(this.r1,
                this.variables[4].value + this.r2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        
        if(this.milestoneLevels[1] > 0) {
                this.r2 = Variable.add(this.r2,
                this.variables[5].value + this.r3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        }
                if(this.milestoneLevels[1] > 1) {
                this.r3 = Variable.add(this.r3,
                this.variables[6].value + this.r4 + Math.log10(Theory2.adBonus) + 
                Math.log10(this.tickFrequency));
        } 
        if(this.milestoneLevels[1] > 1) {
            this.r4 = Variable.add(this.r4,
                this.variables[7].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
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

            if (this.strategy.name == "T2AI") {
                if (this.publicationMultiplier > 800) {
                    this.variables[3].deactivate();
                    this.variables[7].deactivate();
                }
                if (this.publicationMultiplier > 800) {
                    this.variables[2].deactivate();
                    this.variables[6].deactivate();
                }
                if (this.publicationMultiplier > 800) {
                    this.variables[1].deactivate();
                    this.variables[5].deactivate();
                }
                if (this.publicationMultiplier > 2000) {
                    this.variables[0].deactivate();
                    this.variables[4].deactivate();
                }

                this.variableWeights[i] = 10 + (0.030 * (this.variables[i].level % 10) - 0.16);

               

            } else if(this.strategy.name == "T2") {
                this.variableWeights[i] = 10.0;
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