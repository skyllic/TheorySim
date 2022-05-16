package sim.theory;

import java.util.ArrayList;
import java.util.Collections;

import sim.upgrades.Variable;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Euler_Formula extends Theory {





    public double R;
    public double Rdot;
    public double I;
    public double Idot;
    public double q;
    public double qdot;

 

    public String strategyType = "active";
    public double coastingPub = 160;
    public double recoveryPub = 1.0;


    

    public int[] milestoneLevels = new int[4];
    

    

    //public double[] variableWeights = {1000,1000,10,10,10,10,11.1,10.20};
    public double[] variableWeights = {10.0,
                                        10.0, 10.0, 
                                        10.0, 10.0,
                                         10.0, 10.0,
                                        10.0, 10.0, 10.0 
                                        };
    //public double[] variableWeights = {10.0, 10.0, 10.0, 100, 10.0, 100, 100, 10.0, 10.0, 10, 10.0, 10.0};

    
    

    public Euler_Formula(double pubMark) {
        super(14, pubMark);

        this.name = "Euler's Formula";
       
    
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 5;
        this.milestoneLevels[3] = 2;

        this.seconds = 0;
        this.tickCount = 0;
        
        this.rho = 0;
        this.R = 0;
        this.I = 0;
        this.q = 0;
        this.rhodot = -Double.MAX_VALUE;
        this.Rdot = -Double.MAX_VALUE;
        this.Idot = -Double.MAX_VALUE;
        this.qdot = -Double.MAX_VALUE;
        this.t = 0.001;

       
        this.isCoasting = false;
   

        this.longestIdlePeriod = 0;
        this.currentIdlePeriod = 0;
  
        

        
        
    }
    /**Moves the theory by 1 tick (default is 0.1 seconds). Also updates auxillary variables such as q, r, 
     * qdot, rdot and rho.
     */
    public void moveTick() {

      
        

        this.updateEquation();
        
        this.milestoneSwapCheck();

        super.moveTick();

        if(this.rho > this.maxRho) {
            this.maxRho = this.rho;
        }
        this.publicationMultiplier = Math.pow(10, 0.387/10*4 * (this.maxRho-this.publicationMark));
       
    }

    public void milestoneSwapCheck() {
      if(this.publicationMark < 10 && this.maxRho < 10) {
        this.milestoneLevels[0] = 0;
        this.milestoneLevels[1] = 0;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 0;
      } else if(this.publicationMark < 20 && this.maxRho < 20) {
        this.milestoneLevels[0] = 1;
        this.milestoneLevels[1] = 0;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 0;
      } else if(this.publicationMark < 30 && this.maxRho < 30) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 0;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 0;
      } else if(this.publicationMark < 40 && this.maxRho < 40) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 0;
      } else if(this.publicationMark < 50 && this.maxRho < 50) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 1;
      } else if(this.publicationMark < 70 && this.maxRho < 70) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 0;
        this.milestoneLevels[3] = 2;
      } else if(this.publicationMark < 90 && this.maxRho < 90) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 1;
        this.milestoneLevels[3] = 2;
      } else if(this.publicationMark < 110 && this.maxRho < 110) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 2;
        this.milestoneLevels[3] = 2;
      } else if(this.publicationMark < 130 && this.maxRho < 130) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 3;
        this.milestoneLevels[3] = 2;
      } else if(this.publicationMark < 150 && this.maxRho < 150) {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 4;
        this.milestoneLevels[3] = 2;
      } else {
        this.milestoneLevels[0] = 2;
        this.milestoneLevels[1] = 1;
        this.milestoneLevels[2] = 5;
        this.milestoneLevels[3] = 2;
      }
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {
        //System.out.println(this.rhodot);
        

      double sqrtTerm = Variable.add(Math.log10(Math.abs(this.t)) + 2 * this.q, 2 * this.R);
      if(this.milestoneLevels[0] == 0) {
        sqrtTerm = (Math.log10(Math.abs(this.t)) + 2 * this.q) * 0.5;
      } else if(this.milestoneLevels[0] == 1) {
        sqrtTerm = 0.5 * Variable.add(Math.log10(Math.abs(this.t)) + 2 * this.q, 2 * this.R);
      } else {
        sqrtTerm = 0.5 * Variable.add(2 * this.I, Variable.add(Math.log10(Math.abs(this.t)) + 2 * this.q, 2 * this.R));
      }

      this.Rdot = 2 * (this.variables[3].value + this.variables[4].value + Math.log10(Math.abs(Math.cos(this.t)))) 
      + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
      this.Idot = 2 * (this.variables[5].value + this.variables[6].value + Math.log10(Math.abs(Math.sin(this.t))))
      + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;
      this.qdot = this.variables[1].value + this.variables[2].value + Math.log10(Theory.adBonus)
       + Math.log10(this.tickFrequency) + this.totalMultiplier; 
      if(this.milestoneLevels[1] == 0) {
        this.rhodot = sqrtTerm + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
      } else if(this.milestoneLevels[3] == 0) {
        this.rhodot = sqrtTerm + this.variables[7].value 
        + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
      } else if(this.milestoneLevels[3] == 1) {
        this.rhodot = sqrtTerm +this.variables[7].value + this.variables[8].value
        + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
      } else {
        this.rhodot = sqrtTerm + 
        (1 + 0.1 * this.milestoneLevels[2]) * (this.variables[7].value + this.variables[8].value + this.variables[9].value)
        + Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
      }

      this.rhodot = this.rhodot + this.totalMultiplier;

      if(this.variables[0].level > 4) {
        this.variables[0].level = 4;
        this.variables[0].update();
        this.variables[0].nextCost = 100000;
      }

      this.t = this.t + 0.2 * (1 + this.variables[0].level) * this.tickFrequency * (Theory.adBonus);
      this.R = Variable.add(this.R, this.Rdot);
      this.I = Variable.add(this.I, this.Idot);
      this.q = Variable.add(this.q, this.qdot);
      this.rho = Variable.add(this.rho, this.rhodot);
      
    }

     /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * @param variableNumber - the variable number to buy. Note that the variable number starts at 0, not 1.
     */
    @Override
    public void buyVariable(int variableNumber) {
        
        double variableCost = this.variables[variableNumber].nextCost;

        if(this.variableWeights[variableNumber] < 10.11) {
            this.resetIdlePeriod(variableNumber);
            if(this.publicationMultiplier > 0.1) {
                //System.out.print(this.longestIdlePeriod+ "\t\t" + this.currentIdlePeriod + "\t\t" + this.publicationMultiplier +
             //"\t\t" +  this.variableWeights[variableNumber] + "\t\t" + variableNumber + "\t\t" + this.seconds + "\n");
            }
        }


        

        if(variableNumber == 0 || variableNumber == 1 || variableNumber == 2 || variableNumber == 7) {
            if(this.rho >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.rho = Variable.subtract(this.rho, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        } else if(variableNumber == 3 || variableNumber == 4 || variableNumber == 8) {
            if(this.R >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.R = Variable.subtract(this.R, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        } else {
            if(this.I >= variableCost) {
                this.variables[variableNumber].level += 1;
                this.variables[variableNumber].update();
                this.I = Variable.subtract(this.I, variableCost);
            } else {
                //too poor to buy stuff feelsbadman
            }
        }    
    }

    public void runUntilPublish() {
        while(this.finishCoasting == false) {
            this.runEpoch();
            
            
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
                if(i == 0 || i == 1 || i == 2 || i == 7) {
                    while(this.variables[i].cost < this.publicationMark * 0.6) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    
                    }
                }
                if(i == 3 || i == 4 || i == 8) {
                    while(this.variables[i].cost < this.publicationMark * 0.1) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    }
                }
                if(i == 5 || i == 6 || i == 9) {
                    while(this.variables[i].cost < this.publicationMark * 0.1) {
                        this.variables[i].level += 1;
                        this.variables[i].update();
                    }
                }
            }
            
        }
        
        ArrayList<Double> temp = new ArrayList<>();
        this.adjustWeightings(0);
        this.adjustWeightings(1);
        this.adjustWeightings(2);
        this.adjustWeightings(7);
        temp.add(Math.round(this.variables[0].nextCost*1000)/1000.0+(this.variableWeights[0]));
        temp.add(Math.round(this.variables[1].nextCost*1000)/1000.0+(this.variableWeights[1]));
        temp.add(Math.round(this.variables[2].nextCost*1000)/1000.0+(this.variableWeights[2]));
        temp.add(Math.round(this.variables[7].nextCost*1000)/1000.0+(this.variableWeights[7]));

        
        

        int bestVarIndex1 = temp.lastIndexOf(Collections.min(temp));
        if(bestVarIndex1 == 0) {
            bestVarIndex1 = 0;
        } else if(bestVarIndex1 == 1) {
            bestVarIndex1 = 1;
        } else if(bestVarIndex1 == 2) {
            bestVarIndex1 = 2;
        } else {
            bestVarIndex1 = 7;
        }

        temp = new ArrayList<>();
        this.adjustWeightings(3);
        this.adjustWeightings(4);
        this.adjustWeightings(8);
        temp.add(Math.round(this.variables[3].nextCost*1000)/1000.0+(this.variableWeights[3]));
        temp.add(Math.round(this.variables[4].nextCost*1000)/1000.0+(this.variableWeights[4]));
        temp.add(Math.round(this.variables[8].nextCost*1000)/1000.0+(this.variableWeights[8]));

        int bestVarIndex2 = temp.lastIndexOf(Collections.min(temp));
        if(bestVarIndex2 == 0) {
            bestVarIndex2 = 3;
        } else if(bestVarIndex2 == 1) {
            bestVarIndex2 = 4;
        }  else {
            bestVarIndex2 = 8;
        }

        temp = new ArrayList<>();
        this.adjustWeightings(5);
        this.adjustWeightings(6);
        this.adjustWeightings(9);
        temp.add(Math.round(this.variables[5].nextCost*1000)/1000.0+(this.variableWeights[5]));
        temp.add(Math.round(this.variables[6].nextCost*1000)/1000.0+(this.variableWeights[6]));
        temp.add(Math.round(this.variables[9].nextCost*1000)/1000.0+(this.variableWeights[9]));

        int bestVarIndex3 = temp.lastIndexOf(Collections.min(temp));
        if(bestVarIndex3 == 0) {
            bestVarIndex3 = 5;
        } else if(bestVarIndex3 == 1) {
            bestVarIndex3 = 6;
        }  else {
            bestVarIndex3 = 9;
        }

        
      
        int[] bestVarIndices = {bestVarIndex1, bestVarIndex2, bestVarIndex3};
        
        
        return bestVarIndices;


    }

    public void adjustWeightings(int i) {

        /** 10.9, 10.8, 11.0, 
            100, 10.1, 100, 
            100, 10.5, 10.1, 
            10, 10.1, 11.1*/

            if(this.variables[i].isActive == 1){
                
    
            if(this.strategy.name.equalsIgnoreCase("EF")) {
                
                for(int j = 0; j < this.variableWeights.length; j++) {
                    this.variableWeights[j] = 10.0;
                }
            } else if(this.strategy.name.equalsIgnoreCase("EFd")) {
                this.variableWeights[0] = 10.0;
                this.variableWeights[1] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16);;
                this.variableWeights[2] = 10.0;
                this.variableWeights[3] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16);;
                this.variableWeights[4] = 10.5;
                this.variableWeights[5] = 10.5;
                this.variableWeights[6] = 10.5;
                this.variableWeights[7] = 10.6 + (0.030 * (this.variables[i].level % 10) - 0.16);;
                this.variableWeights[8] = 10.2 + (0.15 * (this.variables[i].level % 10) - 0.75);;
                this.variableWeights[9] = 10.0;
            } else if(this.strategy.name.equalsIgnoreCase("EFBaby")) {
                if(this.variables[0].level >= 4) {
                    this.variableWeights[0] = 10000.0;
                } else {
                    this.variableWeights[0] = 10.0;
                }
                

                this.variableWeights[1] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16)
                    + this.publicationMultiplier * 0;
                this.variableWeights[2] = 10.0;
                this.variableWeights[7] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16);

                if(this.publicationMultiplier < 2.2) {
                    this.variableWeights[3] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16);;
                    this.variableWeights[4] = 10.5;
                    this.variableWeights[8] = 10.3 + (0.15 * (this.variables[i].level % 10) - 0.75);;

                    this.variableWeights[5] = 10.8 + (0.030 * (this.variables[i].level % 10) - 0.16);
                    this.variableWeights[6] = 10.5;
                    this.variableWeights[9] = 10.0;


                } else {
                    this.variableWeights[1] = 12.0;
                    this.variableWeights[2] = 11.0;

                    this.variableWeights[8] = 10.0;
                    this.variableWeights[3] = 20.0;
                    this.variableWeights[4] = 20.0;

                    this.variableWeights[9] = 10.0;
                    this.variableWeights[5] = 20.0;
                    this.variableWeights[6] = 20.0;
                }
            }

        }
        
        
        if (this.publicationMultiplier > this.coastingPubs[13]) {
            for (int j = 0; j < this.variables.length; j++) {
                this.variables[j].deactivate(); // autobuy for the variable off.
                this.isCoasting = true;
            }
        }

        
        
    }

  


    /**Idles the input theory until its rho exceeds the input rho */
    public int idleUntil(Euler_Formula theory3, int[] bestVarIndices) {

        

        int varTypeBreak = 0;

        if(this.rho >= this.variables[bestVarIndices[0]].nextCost) {
            varTypeBreak = 0;
            return varTypeBreak;
        } else if(this.R >= this.variables[bestVarIndices[1]].nextCost) {
            varTypeBreak = 1;
            return varTypeBreak;
        } else if(this.I >= this.variables[bestVarIndices[2]].nextCost) {
            varTypeBreak = 2;
            return varTypeBreak;
        }

        while(this.rho < this.variables[bestVarIndices[0]].nextCost && 
            this.R < this.variables[bestVarIndices[1]].nextCost &&
            this.I < this.variables[bestVarIndices[2]].nextCost) {
            
            theory3.moveTick();

            if(this.rho >= this.variables[bestVarIndices[0]].nextCost) {
                varTypeBreak = 0;
                return varTypeBreak;
            } else if(this.R >= this.variables[bestVarIndices[1]].nextCost) {
                varTypeBreak = 1;
                return varTypeBreak;
            } else if(this.I >= this.variables[bestVarIndices[2]].nextCost) {
                varTypeBreak = 2;
                return varTypeBreak;
            }
        }
        return varTypeBreak;
    }

    
    
    



}