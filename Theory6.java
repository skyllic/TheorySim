import java.util.ArrayList;
import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory6 extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;
    public double c; //Minus C at end of Integral
    public double usedMoney;

    public double tickFrequency; //second per tick

    public double[] variableWeights = {11,10.0,10.5,9.8,11,10,1000,1000,10};
    //public double[] variableWeights = {10,10.0,10.0,10,10,10,1000,1000,10};

    public Theory6[] t6Clones = new Theory6[9];
    

    public Theory6(double pubMark) {
        super(6, pubMark);

        this.tickFrequency = 0.1; // seconds per tick
        this.q = Math.log10(1);
        this.r = Math.log10(1);
        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.qdot = -Double.MAX_VALUE;
        this.rdot = -Double.MAX_VALUE;
        this.variables = new Variable[9];
        this.strategy = new Strategy("", ""); 
        this.usedMoney = -Double.MAX_VALUE;
        this.c = -Double.MAX_VALUE;
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(3, 15, Math.pow(2, 0.1), 0, false, true, false, true, false, new double[2]);
        this.variables[1] = new Variable(100, 500, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[2] = new Variable(100000, Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[3] = new Variable(Math.pow(10, 10), Math.pow(10, 30), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[4] = new Variable(2, 10, Math.pow(2, 0.1), 1, false, true, false, false, true, new double[2]);
        this.variables[5] = new Variable(5, 100, 2, 1, true, true, false, false, false, new double[2]);
        this.variables[6] = new Variable(1.255, Math.pow(10, 7), Math.pow(2, 0.1), 0, false, true, false, false, false, new double[2]);
        this.variables[7] = new Variable(500000, Math.pow(10, 25), 2, 1, true, true, false, false, false, new double[2]);
        this.variables[8] = new Variable(3.9, 15, 2, 1, true, true, false, false, false, new double[2]);
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
        this.publicationMultiplier = Math.pow(10, 0.196*(this.maxRho-this.publicationMark));
       
    }

    /**Part of the moveTick() method. Updates equation values such as rho, rhodot, q, qdot etc. */
    public void updateEquation() {
        double rhoTerm1 = this.variables[4].value * 1.15 + this.variables[5].value + this.q + this.r;
        double rhoTerm2 = this.variables[6].value + 2 * this.q + Math.log10(0.5);
        double rhoTerm3 = this.variables[7].value + this.r + 3 * this.q + Math.log10(1/3.0);
        double rhoTerm4 = this.variables[8].value + this.q + 2 * this.r + Math.log10(1/2.0);

        
        this.rho = Variable.add(rhoTerm4, Variable.add(rhoTerm3, Variable.add(rhoTerm1, rhoTerm2))) +
            this.totalMultiplier;
        this.rho = Variable.subtract(this.rho, this.c);

        this.qdot = this.variables[0].value + this.variables[1].value + Math.log10(Theory6.adBonus)+Math.log10(this.tickFrequency);
        this.rdot = this.variables[2].value + this.variables[3].value - 3 + Math.log10(Theory6.adBonus)+Math.log10(this.tickFrequency);
        this.q = Variable.add(this.q, this.qdot);
        this.r = Variable.add(this.r, this.rdot);
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

           

            this.usedMoney = Variable.add(this.usedMoney, variableCost);
            this.c = Variable.subtract(this.getIntegral(), this.rho);
            
         
        } else {
            //too poor to buy stuff feelsbadman
        }
        
    }

    /**Performs a deep copy of Theory6 object.
     * All the relevant fields and references of this object and the copied object should be independent
     * @return clone - The deep copied clone of this object
     */
    public Theory6 cloneTheory6() {
        Theory6 t6Clone = new Theory6(this.publicationMark);

        t6Clone.tickFrequency = 1; // seconds per tick
        t6Clone.q = this.q;
        t6Clone.r = this.r;
        t6Clone.seconds = this.seconds;
        t6Clone.tickCount = this.tickCount;
        t6Clone.rho = this.rho;
        t6Clone.rhodot = this.rhodot;
        t6Clone.qdot = this.qdot;
        t6Clone.rdot = this.rdot;
        t6Clone.variables = new Variable[9];
        for(int i = 0; i < this.variables.length; i++) {
            t6Clone.variables[i] = this.variables[i].getClone();
        }
        
        this.strategy = new Strategy("T6AI", "AI"); 
        t6Clone.usedMoney = this.usedMoney;
        t6Clone.c = this.c;

        return t6Clone;
    }

    public void runStrategyAILoop() {
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i].update();
        }
        this.c = Variable.subtract(this.getIntegral(), this.rho);
        for(int i = 0; i < 10; i++) {
            int bestVarIndex = this.runStrategyAI();
            this.idleUntil(this, this.variables[bestVarIndex].nextCost);
            this.buyVariable(bestVarIndex);
            this.display();
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
                temp.add(Math.round(this.variables[i].nextCost*1000)/1000.0+(this.variableWeights[i]));
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
                

                
               if(this.maxRho < this.publicationMark * 0.981) {
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
                this.variableWeights[i] = 10000;
            }
        
    }

    public double getRhodot() {
        this.qdot = this.variables[0].value + this.variables[1].value + Math.log10(adBonus) + Math.log10(this.tickFrequency);
        this.rdot = this.variables[2].value + this.variables[3].value - 3 + Math.log10(adBonus) + Math.log10(this.tickFrequency);
        double term1 = this.variables[4].value * 1.15 + this.variables[5].value + 
                        Variable.add(this.qdot + this.r, this.q + this.rdot);
        double term2 = Math.log10(0.5) + this.variables[6].value + 
                        Variable.add(Math.log10(2) + this.q + this.qdot + this.r, 2*this.q + this.rdot);
        double term3 = Math.log10(1/3.0) + this.variables[7].value + 
                        Variable.add(Math.log10(3) + 2+this.q + this.qdot + this.r, 3*this.q + this.rdot);
        double term4 = Math.log10(1/2.0) + this.variables[8].value + 
                        Variable.add(this.qdot + 2*this.r, Math.log10(2) + this.q + this.r + this.rdot);

        double total = Variable.add(term1, Variable.add(term2, Variable.add(term3, term4)));

        return total;
    }


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(Theory6 theory6, double variableCost) {
        while(theory6.rho < variableCost) {
            theory6.moveTick();
        }
    }

    protected double getIntegral() {
        double rhoTerm1 = this.variables[4].value * 1.15 + this.variables[5].value + this.q + this.r;
        double rhoTerm2 = this.variables[6].value + 2 * this.q + Math.log10(0.5);
        double rhoTerm3 = this.variables[7].value + this.r + 3 * this.q + Math.log10(1/3.0);
        double rhoTerm4 = this.variables[8].value + this.q + 2 * this.r + Math.log10(1/2.0);

        return Variable.add(rhoTerm4, Variable.add(rhoTerm3, Variable.add(rhoTerm1, rhoTerm2))) +
            this.totalMultiplier;
    }
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        double duration = this.seconds;
        int day = (int) Math.floor(this.seconds / 60.0 / 60.0 / 24.0);
        duration = duration - day * 60 * 60 * 24;
        int hour = (int) Math.floor(duration / 60.0 / 60.0);
        duration = duration - hour * 60 * 60;
        int minute = (int) Math.floor(duration / 60.0);
        duration = duration - minute * 60;
        int second = (int) Math.floor(duration);

        System.out.print(day + "d" + hour + "h" + minute + "m" + second + "s" + "\t");

        //System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0 / 24.0) + "\t");
        for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(String.format("%.2f", this.maxRho) + "\t" + 
        String.format("%.2f", this.q) + "\t" + String.format("%.2f", this.r) + "\t" + this.publicationMultiplier);
        System.out.println("");
        //System.out.println(Arrays.toString(this.variableWeights));
    }



}