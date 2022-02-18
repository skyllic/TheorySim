import java.util.ArrayList;

import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory2 extends Theory {

    public double q1;
    public double q2;
    public double q3;
    public double q4;
    public double r1;
    public double r2;
    public double r3;
    public double r4;

    public String name = "Differential Calculus";
    public double tauPerHour;

    public double rdot;
    public double qdot;
    public double c; //Minus C at end of Integral
    public double usedMoney;
    public boolean isCoasting;

    public double tickFrequency; //second per tick

    public double[] variableWeights = {10,10,10,10,10,10,10,10};

    public Theory2[] t2Clones = new Theory2[8];
    

    public Theory2(double pubMark) {
        super(2, pubMark);

        this.tickFrequency = 1000000000.0; // seconds per tick
       

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
        this.usedMoney = -Double.MAX_VALUE;
        this.c = -Double.MAX_VALUE;
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(2, 10, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(2, 5000, Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(3, 3*Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(4, 8*Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(2, 2*Math.pow(10, 6), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[5] = new Variable(2, 3*Math.pow(10,9), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(3, 4*Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[7] = new Variable(4, 5*Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        
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
        this.publicationMultiplier = Math.pow(10, 0.198*(this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {

        
        this.q1 = Variable.add(this.q1, this.variables[0].value + this.q2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.q2 = Variable.add(this.q2, this.variables[1].value + this.q3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.q3 = Variable.add(this.q3, this.variables[2].value + this.q4 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.q4 = Variable.add(this.q4, this.variables[3].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.r1 = Variable.add(this.r1, this.variables[4].value + this.r2 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.r2 = Variable.add(this.r2, this.variables[5].value + this.r3 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.r3 = Variable.add(this.r3, this.variables[6].value + this.r4 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));
        this.r4 = Variable.add(this.r4, this.variables[7].value + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency));

        this.rhodot = this.q1 * 1.15 + this.r1 * 1.15 + Math.log10(Theory2.adBonus) + Math.log10(this.tickFrequency) + (this.totalMultiplier);

      
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
            int bestVarIndex = this.findBestVarToBuy();
            if(!isCoasting) {
                this.idleUntil(this, this.variables[bestVarIndex].nextCost);
                this.buyVariable(bestVarIndex);
                //this.display();
            } else {//is coasting, stop buying any variable.
                this.coastUntilPublish();
                this.printSummary();
                
                this.publicationMultiplier = 500000000.0;
                return;
            }
        }
    }


    @Override
    public int findBestVarToBuy() {
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
    
            if(this.variables[i].isActive == 1) {
                if(this.publicationMultiplier > 400) {
                    this.variables[3].deactivate();
                    this.variables[7].deactivate();
                }
                if(this.publicationMultiplier > 660) {
                    this.variables[2].deactivate();
                    this.variables[6].deactivate();
                }
                if(this.publicationMultiplier > 660) {
                    this.variables[1].deactivate();
                    this.variables[5].deactivate();
                }
                if(this.publicationMultiplier > 1000) {
                    this.variables[0].deactivate();
                    this.variables[4].deactivate();
                }
               
               

               if(this.publicationMultiplier > 1000) {
                    for(int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); //autobuy for the variable off.
                        this.isCoasting = true;
                    }
               }
               //162.698, 0.240645
               this.variableWeights[i] = 10 + (0.032*(this.variables[i].level % 10) - 0.16);

                
               /**if(this.maxRho < this.publicationMark * 0.981) {
                    this.variableWeights[4] = 11;
                    this.variableWeights[5] = 10;
                    this.variableWeights[8] = 10000;
               } else if(this.maxRho < this.publicationMark * 0.988) {
                    this.variableWeights[4] = 11;
                    this.variableWeights[5] = 10;
                    this.variableWeights[8] = 10;
               } else {
                    this.variableWeights[4] = 10000;
                    this.variableWeights[5] = 10000;
                    this.variableWeights[8] = 10;
               }
               //current best is 155.147 at 15.039224
               this.variableWeights[0] = 11 + (0.018*(this.variables[0].level % 10) - 0.11);
               
               if(this.publicationMultiplier > 10) {
                    this.variableWeights[1] = 11;
                    this.variableWeights[0] = 12;
               }
              



            } else {
                this.variableWeights[i] = 10000;*/
            }
        
    }

  


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory2 theory2, double variableCost) {
        while(theory2.rho < variableCost) {
            theory2.moveTick();
        }
    }

    public void coastUntilPublish() {
        double tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.198 / this.seconds;
        while(this.tauPerHour <= tauRate) {
            System.out.println(tauRate);
            this.tauPerHour = tauRate;
            this.moveTick();
            tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.198 / this.seconds;
            
        }
    }

    
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0 / 24.0) + "\t");
        for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(String.format("%.2f", this.maxRho) + "\t" + 
        String.format("%.2f", this.q1) + "\t" + String.format("%.2f", this.r1) + "\t" + this.publicationMultiplier);
        System.out.println("");
        //System.out.println(Arrays.toString(this.variableWeights));
    }


    public void printSummary() {
        System.out.println(this.name + " at e" + String.format("%.0f", this.publicationMark) + " rho with " + Theory.studentNumber + " students");
        System.out.print("Tau/d\t" + "PubMulti\t" + "Strategy\t" + "PubTime\t\t" + "TauGain\n");
        System.out.print(String.format("%.4f", 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.198 / this.seconds * 24));
        System.out.print("\t" + String.format("%.2f", this.publicationMultiplier) + "\t");
        System.out.print("\tT2\t\t");
        System.out.print(String.format("%.4f", this.seconds / 3600.0));
        System.out.print("\t" + String.format("%.4f", this.maxRho - this.publicationMark));
        System.out.println("\n");

    }


}