
public class Theory6 extends Theory {

    public double r;
    public double q;
    public double rdot;
    public double qdot;

    public double tickSpeed; //second per tick

    public Theory6() {
        this.tickSpeed = 0.1;
    }

    public void moveTick() {
        this.qdot = this.variables[0].value + this.variables[1].value;
        this.rdot = this.variables[2].value + this.variables[3].value - 3;

        this.rhodot = this.variables[4];
    }



}