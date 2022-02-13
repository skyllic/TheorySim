import java.util.ArrayList;
import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory4 extends Theory {




    public String name = "Polynomials";

    public double qdot;
    public double q;

    public double tauPerHour;


    public boolean isCoasting;

    public double tickFrequency; //second per tick

    public double[] variableWeights = {1000,1000,10,10,10,10,10.0,10.2};
    //public double[] variableWeights = {10,10,10,10,10,10,11,10};

    public Theory4[] t2Clones = new Theory4[8];
    

    public Theory4(double pubMark) {
        super(4, pubMark);

        this.tickFrequency = 0.1; // seconds per tick
       


        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.q = 0;
        this.qdot = Double.MAX_VALUE;
        this.isCoasting = false;
        
        this.variables = new Variable[8];
        this.strategy = new Strategy("T4AI", "AI"); 
  
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(1.305, 5, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(3.75, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(2.468, 2000, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(4.85, Math.pow(10, 4), 3, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(12.5, Math.pow(10, 8), 5, 1, true, true, false, false, false, new double[2]);
        this.variables[5] = new Variable(58, Math.pow(10, 10), 10, 1, true, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(100, 1000, Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[7] = new Variable(1000, Math.pow(10, 4), 2, 1, true, true, false, false, false, new double[2]);
        
    }
    /**Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary variables such as q, r, 
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();

        this.seconds += this.tickFrequency;
        this.tickCount += 1;

        if(this.rho > this.maxRho) {
            this.maxRho = this.rho;
        }
        this.publicationMultiplier = Math.pow(10, 0.165 * (this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {

        double term1 = this.variables[0].value * 1.15 + this.variables[1].value;
        double term2 = this.variables[2].value + this.q;
        double term3 = this.variables[3].value + this.q * 2;
        double term4 = this.variables[4].value + this.q * 3;
        double term5 = this.variables[5].value + this.q * 4;
        

       
        this.q = Variable.subtract((Variable.add(Variable.add(this.q, 0) * 2, Math.log10(16) + this.variables[6].value 
            + this.variables[7].value + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency))) * 0.5, 0);

        this.rhodot = Variable.add(term5, Variable.add(term4, Variable.add(term3, Variable.add(term1, term2))))
            + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
        this.rho = Variable.add(this.rho, this.rhodot);


       

      
    }

     /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * @param variableNumber - the variable number to buy. Note that the variable number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        double variableCost = this.variables[variableNumber].nextCost;
        if(this.rho >= variableCost) {
            this.variables[variableNumber].level += 1;
            this.variables[variableNumber].update();
            this.rho = Variable.subtract(this.rho, variableCost);

           

           
           
            //System.out.println(this.getIntegral() + "\t" + this.c);
         
        } else {
            //too poor to buy stuff feelsbadman
        }
        
    }

    

    public void runStrategyAILoop(double pubMulti) {
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }
      
        
        for(int i = 0; i < 1; i++) {
            int bestVarIndex = this.runStrategyAI();
            if(!isCoasting) {
                this.idleUntil(this, this.variables[bestVarIndex].nextCost);
                this.buyVariable(bestVarIndex);
                
            } else {//is coasting, stop buying any variable.
                this.coastUntilPublish();
                this.printSummary();
                
                this.publicationMultiplier = 500000000.0;
                return;
            }
        }
    }


    @Override
    public int runStrategyAI() {
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
            if(this.variables[i].isActive == 1) {
                while(this.variables[i].cost < this.publicationMark * 0.8) {
                    this.variables[i].level += 1;
                    this.variables[i].update();
                
                }
            }
            
        }
        
        ArrayList<Double> temp = new ArrayList<>();
        
        for(int i = 0; i < this.variables.length; i++) {
            if(this.variables[i].isActive == 1) {
                this.adjustWeightings(i);
                try {
                  temp.add(Math.round(this.variables[i].nextCost*1000)/1000.0+(this.variableWeights[i]));
                }
                catch(Exception e) {
                  
                }
                  //Adjust weightings, current best is 157.443 at 15.0392282
                //current best is 155.283 at 15.039224
            } else {
                temp.add(10000.0);
            }
            
        }
        
        
        int bestVarIndex = temp.lastIndexOf(Collections.min(temp));
        
        
        return bestVarIndex;


    }

    public void adjustWeightings(int i) {

        //variableWeights = {1000,1000,10,10,10,10,10.0,10.2};
    
            if(this.variables[i].isActive == 1) {
                if(this.publicationMultiplier > 2.7) {
                    this.variables[6].deactivate();
                    
                }
                if(this.publicationMultiplier > 3.5) {
                    this.variables[7].deactivate();
                }
                

               if(this.publicationMultiplier > 3.65) {
                    for(int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); //autobuy for the variable off.
                        this.isCoasting = true;
                    }
               }
               //
               if(this.publicationMultiplier < 1) {
                    this.variableWeights[7] = 10.1;
                    this.variableWeights[6] = 11.05 + (0.032*(this.variables[i].level % 10) - 0.16);
               } else {
                    this.variableWeights[7] = 10.1;
                    this.variableWeights[6] = 11.05 + (0.032*(this.variables[i].level % 10) - 0.16);
               }
               

                
              
            }
        
    }

  


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory4 theory4, double variableCost) {
        while(theory4.rho < variableCost) {
            theory4.moveTick();
        }
    }

    public void coastUntilPublish() {
        double tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.165 / this.seconds;
        while(this.tauPerHour <= tauRate) {
            
            this.tauPerHour = tauRate;
            this.moveTick();
            tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.165 / this.seconds;
            
        }
    }
    
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0) + "\t");
        /**for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }*/
        
            System.out.print(String.format("%.4f",this.variables[0].value * 1.15 + this.variables[1].value));
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
        //System.out.println(Arrays.toString(this.variableWeights));
    }

    public void printSummary() {
        System.out.println(this.name + " at e" + String.format("%.0f", this.publicationMark) + " rho with " + Theory.studentNumber + " students");
        System.out.print("Tau/d\t" + "PubMulti\t" + "Strategy\t" + "PubTime\t" + "TauGain\n");
        System.out.print(String.format("%.4f", 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.165 / this.seconds * 24));
        System.out.print("\t" + String.format("%.4f", this.publicationMultiplier) + "\t");
        System.out.print("\tT4C3d\t\t");
        System.out.print(String.format("%.4f", this.seconds / 3600.0));
        System.out.print("\t" + String.format("%.4f", this.maxRho - this.publicationMark));
        System.out.println("\n");

    }


}