
/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory6 extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;
    public double c; //Minus C at end of Integral
    public double usedMoney;

    public double tickFrequency; //second per tick

    public Theory6(double pubMark) {
        super(6, pubMark);

        this.tickFrequency = 0.1; // seconds per tick
        this.q = Math.log10(1);
        this.r = Math.log10(1);
        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.research9Level = 3; //default 3R9
        this.adBonus = 1.5; //default on
        this.studentNumber = 288;
        this.publicationMark = pubMark;
        this.totalMultiplier = this.research9Level * (Math.log10(this.studentNumber) - Math.log10(20)) 
            + 0.196 * this.publicationMark - Math.log10(50);
        this.qdot = -Double.MAX_VALUE;
        this.rdot = -Double.MAX_VALUE;
        this.variables = new Variable[9];
        this.strategy = new Strategy("", ""); 
        this.usedMoney = -Double.MAX_VALUE;
        this.c = -Double.MAX_VALUE;

        //Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5 (same as in game when read top to bottom)
        this.variables[0] = new Variable(3, 15, Math.pow(2, 0.1), 0, false, true, false, true, false);
        this.variables[1] = new Variable(100, 500, 2, 1, true, true, false, false, false);
        this.variables[2] = new Variable(100000, Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false);
        this.variables[3] = new Variable(Math.pow(10, 10), Math.pow(10, 30), 2, 1, true, true, false, false, false);
        this.variables[4] = new Variable(2, 10, Math.pow(2, 0.1), 1, false, true, false, false, true);
        this.variables[5] = new Variable(5, 100, 2, 1, true, true, false, false, false);
        this.variables[6] = new Variable(1.255, Math.pow(10, 7), Math.pow(2, 0.1), 0, false, true, false, false, false);
        this.variables[7] = new Variable(500000, Math.pow(10, 25), 2, 1, true, true, false, false, false);
        this.variables[8] = new Variable(3.9, 15, 2, 1, true, true, false, false, false);
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
        if(this.rho > this.publicationMark) {
            this.tauEfficiency = (this.maxRho - this.publicationMark) / this.seconds;
        } else {
            this.tauEfficiency = this.seconds / (this.maxRho - this.publicationMark);
        }
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

        this.qdot = this.variables[0].value + this.variables[1].value + Math.log10(tickFrequency) + Math.log10(this.adBonus);
        this.rdot = this.variables[2].value + this.variables[3].value - 3 + Math.log10(tickFrequency) + Math.log10(this.adBonus);
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
            //System.out.println(this.getIntegral() + "\t" + this.c);
         
        } else {
            //too poor to buy stuff feelsbadman
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
        System.out.print(this.seconds / 3600 + "\t");
        for(int i = 0; i < this.variables.length; i++) {
            System.out.print(this.variables[i].level + "\t");
        }
        System.out.print(this.rho + "\t" + this.q + "\t" + this.r);
        System.out.println("");
    }



}