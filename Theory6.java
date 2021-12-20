
/**An implementation of Theory 6 (Integral Calculus) from the game Exponential Idle. */
public class Theory6 extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;

    public double tickSpeed; //second per tick

    public Theory6(double pubMark) {
        super();

        this.tickSpeed = 0.1;
        this.q = Math.log10(1);
        this.r = Math.log10(1);
        this.tickNumber = 0;
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
        //this.variables = new Variable(costScaling, costBase, valueScaling, valueBase,
        //isDoubling, isExponential, isLinear, isFirst, isOffset)
        this.variables[0] = new Variable(3, 15, Math.pow(2, 0.1), 0, false, true, false, true, false);
        this.variables[1] = new Variable(100, 500, 2, 1, true, true, false, false, false);
        this.variables[2] = new Variable(100000, Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true, false, false, false);
        this.variables[3] = new Variable(Math.pow(10, 10), Math.pow(10, 30), 2, 1, true, true, false, false, false);
        this.variables[4] = new Variable(2, 10, Math.pow(2, 0.1), 0, false, true, false, false, true);
        this.variables[5] = new Variable(5, 100, 2, 1, true, true, false, false, false);
        this.variables[6] = new Variable(1.255, Math.pow(10, 7), Math.pow(2, 0.1), 0, false, true, false, false, false);
        this.variables[7] = new Variable(500000, Math.pow(10, 25), 2, 1, true, true, false, false, false);
        this.variables[8] = new Variable(3.9, 15, 2, 1, true, true, false, false, false);
    }

    public void moveTick() {

        double rhoTerm1 = this.variables[4].value * 1.15 + this.variables[5].value + this.q + this.r;
        double rhoTerm2 = this.variables[6].value + 2 * this.q + Math.log10(0.5);
        double rhoTerm3 = this.variables[7].value + this.r + 3 * this.q + Math.log10(1/3.0);
        double rhoTerm4 = this.variables[8].value + this.q + 2 * this.r + Math.log10(1/2.0);

        this.rho = Variable.add(rhoTerm4, Variable.add(rhoTerm3, Variable.add(rhoTerm1, rhoTerm2))) +
            this.totalMultiplier;

        this.qdot = this.variables[0].value + this.variables[1].value + Math.log10(tickSpeed) + Math.log10(this.adBonus);
        this.rdot = this.variables[2].value + this.variables[3].value - 3 + Math.log10(tickSpeed) + Math.log10(this.adBonus);
        this.q = Variable.add(this.q, this.qdot);
        this.r = Variable.add(this.r, this.rdot);
        
        //System.out.println(rhodot1 + "\t" + rhodot2 + "\t" + rhodot3 + "\t" + rhodot4);
    }



}