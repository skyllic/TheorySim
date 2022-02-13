import java.util.ArrayList;

import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Sequential_Limit extends Theory {





    public double rho1;
    public double rho2;
    public double rho3;
    public double rho1dot;
    public double rho2dot;
    public double rho3dot;
    public double inverseGamma;

    public String name;

    public double tauPerHour;
   


    public boolean isCoasting;

    public double tickFrequency; //second per tick

    //public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = {10,10,10,10};

    public Sequential_Limit[] SL_Clones = new Sequential_Limit[4];
    

    public Sequential_Limit(double pubMark) {
        super(11, pubMark);

        this.tickFrequency = 10.0; // seconds per tick
       


        this.seconds = 0;
        this.tickCount = 0;

        this.name = "Sequential Limits";
        
        
        this.isCoasting = false;
        
        this.variables = new Variable[4];
        this.strategy = new Strategy("SLAI", "AI"); 
  
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(Math.pow(10, 0.369), 1.0, Math.pow(3.5, 3.5/3.0), 0, false, true, false, true, false, new double[] {3.5, 3});
        this.variables[1] = new Variable(10, 175, 2, 1, true, true, false, false, true, new double[2]);
        this.variables[2] = new Variable(Math.pow(10, 0.649), 500, Math.pow(6.5, 6.5/4.0), 1, false, true, false, false, false, new double[] {6.5, 4});
        this.variables[3] = new Variable(Math.pow(10, 0.926), 1000, 2, 1, true, true, false, false, false, new double[2]);
        
        
        
    }
    /**Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary variables such as q, r, 
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();

        this.seconds += this.tickFrequency;
        this.tickCount += 1;

        if(this.rho1 > this.maxRho) {
            this.maxRho = this.rho1;
        }
        this.publicationMultiplier = Math.pow(10, 0.15 * (this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {

        
        this.rho3dot = (this.variables[2].value * 1.04 + this.variables[3].value * 1.04) + 
        Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
        this.rho2dot = (this.variables[0].value + this.variables[1].value + 
        Math.log10(1.96) * (-this.rho3 * Math.log(10))) + 
        Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);


       
       this.inverseGamma = this.getInverseGamma();

       this.rho1dot = (this.rho2 * 1.06 / 2 + this.inverseGamma) + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency)
        + this.publicationMark * 0.15;
         
        



        this.rho1 = Variable.add(this.rho1, this.rho1dot);
        this.rho2 = Variable.add(this.rho2, this.rho2dot);
        this.rho3 = Variable.add(this.rho3, this.rho3dot);



      
    }

    public double getInverseGamma() {
        double inverseGamma;
        
        
            double term1 = -Math.log(Math.log(2 * Math.PI) + this.rho3 * Math.log(10));
            double term2 = Math.log(2 * Math.PI);
            double term3 = this.rho3 * Math.log(10);
            double term4 = -Math.log(Math.PI);
            
            inverseGamma = (term1 + term2 + term3 + term4 - 1) * Math.log10(Math.E);
            
        
        return inverseGamma;
    }

     /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * @param variableNumber - the variable number to buy. Note that the variable number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        double variableCost = this.variables[variableNumber].nextCost;
        if(this.rho1 >= variableCost) {
            this.variables[variableNumber].level += 1;
            this.variables[variableNumber].update();
            this.rho1 = Variable.subtract(this.rho1, variableCost);

           

           
           
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
                //this.display();
                
            } else {//is coasting, stop buying any variable.
                this.coastUntilPublish();
                //this.idleUntil(this, this.publicationMark + Math.log(7.5) / Math.log(10) / 0.15);
                //this.display();
                //System.out.println(60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.15 / this.seconds);
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
                while(this.variables[i].cost < this.publicationMark * 0.01) {
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

        double a1Penalty = 0;
        double a2Penalty = 0;

       
    
            if(this.variables[i].isActive == 1) {

                if(this.publicationMultiplier > 0.1) {
                    a1Penalty = 0.05;
                    a2Penalty = 0.05;
                    
                    
                }
              
                if(this.publicationMultiplier > 1) {
                    a1Penalty = 0.1;
                    a2Penalty = 0.1;
                    
                    
                }
                if(this.publicationMultiplier > 2) {
                    a1Penalty = 0.10;
                    a2Penalty = 0.10;
                    
                    
                }
                if(this.publicationMultiplier > 3) {
                    a1Penalty = 0.20;
                    a2Penalty = 0.20;
                    
                    
                }

                if(this.variables[0].level % 3 == 0) {
                    //strongest
                    this.variableWeights[0] = 10.05 + a1Penalty;
                } else if(this.variables[0].level % 3 == 1) {
                    this.variableWeights[0] = 10.25 + a1Penalty;
                } else if(this.variables[0].level % 3 == 2) {
                    //weakest
                    this.variableWeights[0] = 10.35 + a1Penalty;
                }
                this.variableWeights[1] = 10 + a2Penalty; 

                if(this.variables[2].level % 4 == 0) {
                    //strongest
                    this.variableWeights[2] = 9.9;
                } else if(this.variables[2].level % 4 == 1) {
                    this.variableWeights[2] = 10.1;
                } else if(this.variables[2].level % 4 == 2) {
                    this.variableWeights[2] = 10.3;
                } else if(this.variables[2].level % 4 == 3) {
                    //weakest
                    this.variableWeights[2] = 10.50;
                }

                
                if(this.publicationMultiplier > 4.0) {
                    this.variables[0].deactivate();
                    this.variables[1].deactivate();
                    
                }


                if(this.publicationMultiplier > 5.5) {
                    this.variables[2].deactivate();
                    this.variables[3].deactivate();
                }
        
               if(this.publicationMultiplier > 5.5) {
                    for(int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); //autobuy for the variable off.
                        this.isCoasting = true;
                    }
               }
               
               

                
              
            }
        
    }

  


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Sequential_Limit theorySL, double variableCost) {
        while(theorySL.rho1 < variableCost) {
            theorySL.moveTick();
        }
    }

    public void coastUntilPublish() {
        double tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.15 / this.seconds;
        while(this.tauPerHour <= tauRate) {
            
            this.tauPerHour = tauRate;
            this.moveTick();
            tauRate = 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.15 / this.seconds;
            
        }
    }

    
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0) + "\t");
        /**for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }*/
        
            System.out.print(String.format("%.4f",this.rho1));
            System.out.print("\t");
            System.out.print(String.format("%.4f", this.rho2));
            System.out.print("\t");
            System.out.print(String.format("%.4f", this.rho3));
            System.out.print("\t");
            System.out.print(String.format("%.4f", this.inverseGamma));
            System.out.print("\t");
           
        
        System.out.print(String.format("%.2f", this.maxRho) + "\t" + 
         this.publicationMultiplier);
        System.out.println("");
        //System.out.println(Arrays.toString(this.variableWeights));
    }

    public void printSummary() {
        System.out.println(this.name + " at e" + String.format("%.0f", this.publicationMark) + " rho");
        System.out.print("Tau/hr\t" + "PubMulti\t" + "Strategy\t" + "PubTime\t" + "TauGain\n");
        System.out.print(String.format("%.8f", 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.15 / this.seconds));
        System.out.print("\t" + String.format("%.4f", this.publicationMultiplier) + "\t");
        System.out.print("\tSLAI\t\t");
        System.out.print(String.format("%.4f", this.seconds / 3600.0));
        System.out.print("\t" + String.format("%.4f", this.maxRho - this.publicationMark));
        System.out.println("\n");

    }



}