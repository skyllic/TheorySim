import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class WeierStrass extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;
    public double chi;
    public double c; //Minus C at end of Integral
    public double usedMoney;

    public double tickFrequency; //second per tick

    public double[] variableWeights = {1,1,1,1,1};

    public WeierStrass[] t6Clones = new WeierStrass[5];
    

    public WeierStrass(double pubMark) {
        super(6, pubMark);

        this.tickFrequency = 0.1; // seconds per tick
        this.q = Math.log10(1);
        
        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.chi = 0;
        this.qdot = -Double.MAX_VALUE;
        this.rdot = -Double.MAX_VALUE;
        this.variables = new Variable[5];
        this.strategy = new Strategy("", ""); 
        this.usedMoney = -Double.MAX_VALUE;
        this.c = -Double.MAX_VALUE;
        

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(Math.pow(2, 3.38/4), 10, Math.pow(2, 0.1), 0, false, true, false, true, false);
        this.variables[1] = new Variable(Math.pow(2, 3.38*4), 1000, 2, 1, true, true, false, false, false);
        this.variables[2] = new Variable(Math.pow(2, 3.38), 20, 1, 1, false, false, true, false, false);
        this.variables[3] = new Variable(Math.pow(2, 3.38/1.5), 50, Math.pow(2, 1/50.0), 1, true, true, false, false, true);
        this.variables[4] = new Variable(Math.pow(2, 3.38*10), Math.pow(10, 10), 2, 1, true, true, false, false, false);
        
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
        this.rhodot = this.variables[0].value * 1.04 + this.variables[1].value + this.q;
        this.chi = Variable.add(Math.PI + this.variables[3].value+this.variables[2].value - 
        Variable.add(this.variables[3].value, this.variables[2].value-Math.log10(27)), 0);

        double prod = this.calculateSn();

        this.qdot = this.variables[4].value + prod - Math.sin(this.chi);


    }

    public double calculateSn() {
        double product = 0;
        for(int i = 0; i < this.variables[2].value; i++) {
            product += Variable.subtract(0, 2*(this.chi - Math.PI - Math.log10(i)));
        }
        product += this.chi;

        return product;
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

    /**Performs a deep copy of Theory6 object.
     * All the relevant fields and references of this object and the copied object should be independent
     * @return clone - The deep copied clone of this object
     */
    public Theory6 cloneTheory6() {
        Theory6 t6Clone = new Theory6(this.publicationMark);

        t6Clone.tickFrequency = 0.1; // seconds per tick
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
            while(this.variables[i].cost < this.publicationMark * 0.8) {
                this.variables[i].level += 1;
                this.variables[i].update();
                
            }
            
        }
        
        ArrayList<Double> temp = new ArrayList<>();
        
        for(int i = 0; i < this.variables.length; i++) {
            temp.add(Math.round(this.variables[i].nextCost*1000)/1000.0+(this.variableWeights[i]));
            
            
        }
        int bestVarIndex = temp.lastIndexOf(Collections.min(temp));
        
        
        return bestVarIndex;


    }


    /**Idles the input theory until its rho exceeds the input rho */
    public void idleUntil(WeierStrass weierstrass, double variableCost) {
        while(weierstrass.rho < variableCost) {
            weierstrass.moveTick();
        }
    }

  
    
    @Override
    public void display() {
        //System.out.println(this.rho + "\t" + this.q + "\t" + this.r + "\t" + this.tickNumber);
        System.out.print(String.format("%.3f",this.seconds / 60.0 / 60.0) + "\t");
        for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(String.format("%.2f", this.maxRho) + "\t" + 
        String.format("%.2f", this.q) + "\t" + String.format("%.2f", this.r) + "\t" + this.publicationMultiplier);
        System.out.println("");
    }



}