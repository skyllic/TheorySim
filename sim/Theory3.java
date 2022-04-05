package sim;

import java.util.ArrayList;
import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory3 extends Theory {





    public double rho1;
    public double rho1dot;
    public double rho2;
    public double rho2dot;
    public double rho3;
    public double rho3dot;

    public double term11;
    public double term12;
    public double term13;
    public double term21;
    public double term22;
    public double term23;
    public double term31;
    public double term32;
    public double term33;

    public String strategyType = "active";
    public double coastingPub = 4.5;
    public double recoveryPub = 1.0;


    public boolean isCoasting;
    

    

    //public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = {11.0, 11.0, 11.0, 
                                        100, 10.1, 100, 
                                        100, 10.5, 10.1, 
                                        10, 10.1, 11.1};
    //public double[] variableWeights = {10.0, 10.0, 10.0, 100, 10.0, 100, 100, 10.0, 10.0, 10, 10.0, 10.0};

    public Theory3[] t3Clones = new Theory3[12];
    

    public Theory3(double pubMark) {
        super(3, pubMark);

        this.name = "Linear Algebra";
       
    

        this.seconds = 0;
        this.tickCount = 0;
        
        this.rho1 = 0;
        this.rho2 = 0;
        this.rho3 = 0;
        this.rho1dot = -Double.MAX_VALUE;
        this.rho2dot = -Double.MAX_VALUE;
        this.rho3dot = -Double.MAX_VALUE;

       
        this.isCoasting = false;
        
        this.variables = new Variable[12];
        this.strategy = new Strategy("T3AI", "AI"); 
  
        

        //Order of variable is b1, b2, b3, c11, c12, c13, c21, c22, c23, c31, c32, c33.
        this.variables[0] = new Variable(1.18099, 10, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(1.308, 10, Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(1.675, 3000, Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(6.3496, 20, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(2.74, 10, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[5] = new Variable(1.965, 1000, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(18.8343, 1000, 2, 0, true, true, false, false, false, new double[2]);
        this.variables[7] = new Variable(3.65, Math.pow(10, 5), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[8] = new Variable(2.27, Math.pow(10, 5), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[9] = new Variable(1248.27, Math.pow(10, 4), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[10] = new Variable(6.81774, Math.pow(10, 3), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[11] = new Variable(2.98, Math.pow(10, 5), 2, 1, true, true, false, false, false, new double[2]);
        
    }
    /**Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary variables such as q, r, 
     * qdot, rdot and rho.
     */
    public void moveTick() {

        this.updateEquation();
        this.rho = this.rho1;
        

        super.moveTick();

        if(this.rho1 > this.maxRho) {
            this.maxRho = this.rho1;
        }
        this.publicationMultiplier = Math.pow(10, 0.147 * (this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {

        this.term11 = this.variables[0].value * 1.1 + this.variables[3].value;
        this.term12 = this.variables[1].value * 1.1 + this.variables[4].value;
        this.term13 = this.variables[2].value * 1.1 + this.variables[5].value;

        this.term21 = this.variables[0].value * 1.1 + this.variables[6].value;
        this.term22 = this.variables[1].value * 1.1 + this.variables[7].value;
        this.term23 = this.variables[2].value * 1.1 + this.variables[8].value;

        this.term31 = this.variables[0].value * 1.1 + this.variables[9].value;
        this.term32 = this.variables[1].value * 1.1 + this.variables[10].value;
        this.term33 = this.variables[2].value * 1.1 + this.variables[11].value;
        
        this.rho1dot = Variable.add(term13, Variable.add(term11, term12)) + Math.log10(Theory.adBonus) + this.totalMultiplier + Math.log10(this.tickFrequency);
        this.rho2dot = Variable.add(term23, Variable.add(term21, term22)) + Math.log10(Theory.adBonus) + this.totalMultiplier + Math.log10(this.tickFrequency);
        this.rho3dot = Variable.add(term33, Variable.add(term31, term32)) + Math.log10(Theory.adBonus) + this.totalMultiplier + Math.log10(this.tickFrequency);

        this.rho1 = Variable.add(this.rho1, this.rho1dot);
        this.rho2 = Variable.add(this.rho2, this.rho2dot);
        this.rho3 = Variable.add(this.rho3, this.rho3dot);
      
    }

     /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * @param variableNumber - the variable number to buy. Note that the variable number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        double variableCost = this.variables[variableNumber].nextCost;

        if(variableNumber == 0 || variableNumber == 3 || variableNumber == 6 || variableNumber == 9) {
            if(this.rho1 >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.rho1 = Variable.subtract(this.rho1, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        } else if(variableNumber == 1 || variableNumber == 4 || variableNumber == 7 || variableNumber == 10) {
            if(this.rho2 >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.rho2 = Variable.subtract(this.rho2, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        } else {
            if(this.rho3 >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.rho3 = Variable.subtract(this.rho3, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        }    
    }

    

    public void runEpoch() {
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }
      
        
        for(int i = 0; i < 1; i++) {
            int[] bestVarIndices = this.runStrategyAI3();
            if(!isCoasting) {
                int varTypeBreak = this.idleUntil(this, bestVarIndices);
                if(varTypeBreak == 0) {
                    this.buyVariable(bestVarIndices[0]);
                    //this.display();
                } else if(varTypeBreak == 1) {
                    this.buyVariable(bestVarIndices[1]);
                    //display();
                } else {
                    this.buyVariable(bestVarIndices[2]);
                    //display();
                }
               
            } else {//is coasting, stop buying any variable.
                
                this.coastUntilPublish();
                

                this.finishCoasting = true;
                return;
            }
        }
    }


    
    public int[] runStrategyAI3() {
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
            if(this.variables[i].isActive == 1) {
                if(i == 0 || i == 3 || i == 6 || i == 9) {
                    while(this.variables[i].cost < this.publicationMark * 0.8) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    
                    }
                }
                if(i == 1 || i == 4 || i == 7 || i == 10) {
                    while(this.variables[i].cost < this.publicationMark * 0.6) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    }
                }
                if(i == 2 || i == 5 || i == 8 || i == 11) {
                    while(this.variables[i].cost < this.publicationMark * 0.4) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    }
                }
            }
            
        }
        
        ArrayList<Double> temp = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            if(this.variables[3*i].isActive == 1) {
                this.adjustWeightings(3*i);
            
                try {
                    temp.add(Math.round(this.variables[3*i].nextCost*1000)/1000.0+(this.variableWeights[3*i]));
                }
                catch(Exception e) {

                }
            } else {
                temp.add(10000.0);
            }
        }
        int bestVarIndex1 = temp.lastIndexOf(Collections.min(temp)) * 3;

        temp = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            if(this.variables[3*i+1].isActive == 1) {
                this.adjustWeightings(3*i+1);
            
                try {
                    temp.add(Math.round(this.variables[3*i+1].nextCost*1000)/1000.0+(this.variableWeights[3*i+1]));
                }
                catch(Exception e) {

                }
            } else {
                temp.add(10000.0);
            }
        }
        int bestVarIndex2 = temp.lastIndexOf(Collections.min(temp)) * 3 + 1;

        temp = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            if(this.variables[3*i+2].isActive == 1) {
                this.adjustWeightings(3*i+2);
            
                try {
                    temp.add(Math.round(this.variables[3*i+2].nextCost*1000)/1000.0+(this.variableWeights[3*i+2]));
                }
                catch(Exception e) {

                }
            } else {
                temp.add(10000.0);
            }
        }
        int bestVarIndex3 = temp.lastIndexOf(Collections.min(temp)) * 3 + 2;
      
        int[] bestVarIndices = {bestVarIndex1, bestVarIndex2, bestVarIndex3};
        
        
        return bestVarIndices;


    }

    public void adjustWeightings(int i) {

        /** 10.9, 10.8, 11.0, 
            100, 10.1, 100, 
            100, 10.5, 10.1, 
            10, 10.1, 11.1*/

            if(this.variables[i].isActive == 1){
                
    
            if(this.strategy.name == "T3Play2") {
                this.variableWeights[0] = 10.9 + (0.028*(this.variables[i].level % 10) - 0.14); //b1
                this.variableWeights[1] = 10.80 + (0.028*(this.variables[i].level % 10) - 0.14); //b2
                this.variableWeights[2] = 11.00 + (0.028*(this.variables[i].level % 10) - 0.14); //b3
                
                this.variableWeights[7] = 10.5; //c22 8x
                this.variableWeights[10] = 10.1; //c32 auto

                //Sandbag on c12 since we don't need to buy them yet.
                if(this.publicationMultiplier < 0.6) {
                    this.variableWeights[4] = 14.1;
                } else {
                    this.variableWeights[4] = 10.1;
                }
                //Coasting is different for theory 3, as there are other currencies we would never coast with.
                if(this.publicationMultiplier > 0.6) {

                    this.variables[0].deactivate();
                    this.variables[3].deactivate();
                    this.variables[6].deactivate();
                    this.variables[9].deactivate();                            
               }
               if(this.publicationMultiplier > 0.6) {
                    this.variableWeights[7] = 11.0; //c22 8x
                    this.variableWeights[10] = 11.0; //c32 8x

                    

                    this.variableWeights[11] = 120.0;//After c12 autobuy, c33 off.   
               }
              
               if(this.publicationMultiplier > 2.4) {
                   this.variableWeights[1] = 10.1; // b2 autobuy
                   this.variableWeights[4] = 10.1;//c12 autobuy
                   this.variableWeights[7] = 13.1; //c22 OFF
                   this.variableWeights[10] = 13.1; //c32 OFF

                   this.variableWeights[2] = 10.1;//b3 autobuy as c23 is bad
                   this.variableWeights[8] = 10.1;//c23
               }

                
              
            } else if(this.strategy.name == "T3PlayX") {

                this.variableWeights[0] = 11.00 + (0.032*(this.variables[i].level % 10) - 0.14); //b1
                this.variableWeights[1] = 10.70 + (0.032*(this.variables[i].level % 10) - 0.14); //b2
                this.variableWeights[2] = 11.00 + (0.032*(this.variables[i].level % 10) - 0.14); //b3
                
                this.variableWeights[7] = 10.5; //c22 8x
                this.variableWeights[10] = 10.1; //c32 auto
                this.variableWeights[11] = 11.1;

                //Sandbag on c12 since we don't need to buy them yet.
                if(this.publicationMultiplier < 0.8) {
                    this.variableWeights[4] = 14.1;
                } else {
                    this.variableWeights[4] = 10.1;
                }
                //Coasting is different for theory 3, as there are other currencies we would never coast with.
                if(this.publicationMultiplier > 0.6) {

                    this.variables[0].deactivate();
                    this.variables[3].deactivate();
                    this.variables[6].deactivate();
                    this.variables[9].deactivate();                            
               }
               if(this.publicationMultiplier > 0.8) {
                    this.variableWeights[7] = 10.8; //c22 8x
                    this.variableWeights[10] = 10.8; //c32 8x

                    
                    this.variableWeights[11] = 13.0;//After c12 autobuy, c33 off.   
               }
               if(this.publicationMultiplier > 2.4) {
                   this.variableWeights[1] = 10.1; // b2 autobuy
                   this.variableWeights[4] = 10.1;//c12 autobuy
                   this.variableWeights[7] = 16.1; //c22 OFF
                   this.variableWeights[10] = 16.1; //c32 OFF

                   this.variableWeights[2] = 10.1;//b3 autobuy as c23 is bad
                   this.variableWeights[8] = 10.1;//c23
               }
            } else if(this.strategy.name == "T3XS") {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;

                this.variableWeights[4] = 12.0;

                this.variableWeights[4] = 10 + Math.min(2, 1 / this.publicationMultiplier);

                this.variableWeights[7] = 10.0;
                this.variableWeights[8] = 10.0;

                this.variableWeights[9] = 10.0;
                this.variableWeights[10] = 10.0;
                this.variableWeights[11] = 10.0;

                if(this.maxRho > this.publicationMark - Math.log10(2)) {
                    this.variables[0].deactivate();
                    this.variables[3].deactivate();
                    this.variables[6].deactivate();
                    this.variables[9].deactivate();

                    this.variables[10].deactivate();
                    this.variables[11].deactivate();

                    this.variables[5].deactivate();

                    this.variableWeights[4] = 10.0;

                    //this.variableWeights[4] = 10.0;
                }

               
            } else if(this.strategy.name == "T3PlayI") {
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;

                this.variableWeights[4] = 12.0;

                this.variableWeights[7] = 10.0;
                this.variableWeights[8] = 10.0;

                this.variableWeights[10] = 10.0;
                this.variableWeights[11] = 10.0;

                if(this.maxRho > this.publicationMark) {
                    this.variableWeights[4] = 10.0;
                    this.variables[11].deactivate();
                }
            } else if(this.strategy.name == "T3OldI") {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.0;
                this.variableWeights[2] = 10.0;

                this.variableWeights[4] = 10.0;

                this.variableWeights[7] = 10.0;
                this.variableWeights[8] = 10.0;

                this.variableWeights[9] = 10.0;
                this.variableWeights[10] = 10.0;
            }
        } 
        if(this.publicationMultiplier > this.coastingPub) {
            /**for(int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate();
            }*/

            this.variables[7].deactivate();
            this.variables[10].deactivate();
            
            this.isCoasting = true;
           
       }

        
        
    }

  


    /**Idles the input theory until its rho exceeds the input rho */
    public int idleUntil(Theory3 theory3, int[] bestVarIndices) {

        

        int varTypeBreak = 0;

        if(this.rho1 >= this.variables[bestVarIndices[0]].nextCost) {
            varTypeBreak = 0;
            return varTypeBreak;
        } else if(this.rho2 >= this.variables[bestVarIndices[1]].nextCost) {
            varTypeBreak = 1;
            return varTypeBreak;
        } else if(this.rho3 >= this.variables[bestVarIndices[2]].nextCost) {
            varTypeBreak = 2;
            return varTypeBreak;
        }

        while(this.rho1 < this.variables[bestVarIndices[0]].nextCost && 
            this.rho2 < this.variables[bestVarIndices[1]].nextCost &&
            this.rho3 < this.variables[bestVarIndices[2]].nextCost) {
            
            theory3.moveTick();

            if(this.rho1 >= this.variables[bestVarIndices[0]].nextCost) {
                varTypeBreak = 0;
                return varTypeBreak;
            } else if(this.rho2 >= this.variables[bestVarIndices[1]].nextCost) {
                varTypeBreak = 1;
                return varTypeBreak;
            } else if(this.rho3 >= this.variables[bestVarIndices[2]].nextCost) {
                varTypeBreak = 2;
                return varTypeBreak;
            }
        }
        return varTypeBreak;
    }

    
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0) + "\t");
        /**for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }*/
        System.out.print(String.format("%.2f", this.term11));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term12));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term13));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term21));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term22));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term23));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term31));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term32));
        System.out.print("\t");
        System.out.print(String.format("%.2f", this.term33));
        System.out.print("\t");
        /**for(int i = 0; i < 12; i++) {
            System.out.print(String.format("%.2f", this.variables[i].value));
            System.out.print("\t");
        }*/
        System.out.print(String.format("%.2f", this.maxRho) + "\t" + 
        this.publicationMultiplier);
        System.out.println("");
        //System.out.println(Arrays.toString(this.variableWeights));
    }



}