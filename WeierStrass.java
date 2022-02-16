import java.util.ArrayList;
import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class WeierStrass extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;
    public double chi;
    public double product;
    public double c; //Minus C at end of Integral
    public double usedMoney;
    public double tauPerHour;

    public String name = "Weierstrass";
    public double tickFrequency; //second per tick
    public boolean isCoasting = false;

    public double[] variableWeights = {10,10,10,10,10};

    public WeierStrass[] t6Clones = new WeierStrass[5];
    

    public WeierStrass(double pubMark) {
        super(10, pubMark);

        this.tickFrequency = 0.1; // seconds per tick
        this.q = Math.log10(1);
        
        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.chi = 0;
        this.variables = new Variable[5];
        this.strategy = new Strategy("", ""); 
        
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(Math.pow(2, 3.38/4), 10, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(Math.pow(2, 3.38*3), 1000, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(Math.pow(2, 3.38), 20, 1, 0, false, false, true, false, false, new double[2]);
        this.variables[3] = new Variable(Math.pow(2, 3.38/1.5), 50, Math.pow(2, 1/50.0), 1, true, true, false, false, true, new double[2]);
        this.variables[4] = new Variable(Math.pow(2, 3.38*10), Math.pow(10, 10), 2, 1, true, true, false, false, false, new double[2]);
        
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
        this.publicationMultiplier = Math.pow(10, 0.15*(this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {
        if(this.variables[0].level > 3069) {
            double e = 0;
        }

        this.rhodot = this.variables[0].value * 1.04 + this.variables[1].value + this.q + 
            Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency) + this.totalMultiplier;//good

        this.chi = this.calculateChi();

        double prod = this.calculateSn();
        //System.out.println(prod);

        this.qdot = this.variables[4].value + prod - Math.log10(Math.abs(Math.sin(this.chi))) + 
            Math.log10(Theory.adBonus) + Math.log10(this.tickFrequency);
        this.q = Variable.add(this.q, this.qdot);
        this.rho = Variable.add(this.rho, this.rhodot);
        //System.out.println(this.rho);

    }

    /**Calculates the chi value used in the equation. This is the actual (non-logged) value.
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

    /**Calculates sn(chi) used in the equation.
     * The products are written in log10 notation, while chi is standard.
     * @return product - the product sn(chi).
     */
    public double calculateSn() {
        this.product = Math.log10(1);




        for(int i = 1; i <= this.variables[2].value; i++) {
            double producant = 1 - Math.pow((this.chi / i / Math.PI), 2);
            producant = Math.abs(producant);
            producant = Math.log10(producant);
            this.product += producant;
        }
        this.product += Math.log10(this.chi);

        return this.product;
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
                this.display();
                
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

                this.variableWeights[0] = 11;
                this.variableWeights[0] = 10.8 + (0.030*(this.variables[i].level % 10) - 0.14);
                this.variableWeights[1] = 9.9;
                this.variableWeights[2] = 9.70;
                this.variableWeights[3] = 13;
                this.variableWeights[4] = 9.9;
        
               if(this.publicationMultiplier > 8.5) {
                    for(int j = 0; j < this.variables.length; j++) {
                        this.variables[j].deactivate(); //autobuy for the variable off.
                        this.isCoasting = true;
                    }
               }
              
            }
        
    }


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(WeierStrass weierstrass, double variableCost) {
        while(weierstrass.rho < variableCost) {
            weierstrass.moveTick();
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
        for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(String.format("%.2f", this.chi) + "\t" + 
        String.format("%.2f", this.variables[2].value) + "\t" + String.format("%.2f", this.product) + "\t" + this.publicationMultiplier);
        System.out.println("");
    }

    public void printSummary() {
        System.out.println(this.name + " at e" + String.format("%.0f", this.publicationMark) + " rho");
        System.out.print("Tau/hr\t" + "PubMulti\t" + "Strategy\t" + "PubTime\t" + "TauGain\n");
        System.out.print(String.format("%.8f", 60*60*Math.log(this.publicationMultiplier) / Math.log(10) / 0.15 / this.seconds));
        System.out.print("\t" + String.format("%.4f", this.publicationMultiplier) + "\t");
        System.out.print("\tWSPAI\t\t");
        System.out.print(String.format("%.4f", this.seconds / 3600.0));
        System.out.print("\t" + String.format("%.4f", this.maxRho - this.publicationMark));
        System.out.println("\n");

    }


}