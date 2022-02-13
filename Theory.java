

/**A class representing a theory. 
 * @param rho - the current rho in log format
 * @param rhodot - the current rhodot in log format. For theory with multiple rho types, this field represents the rho that contributes to tau directly
 * @param seconds - the current tick number. Default is 10 ticks/second. Resets on publish. Does not reset on variable bought
 * @param totalMultiplier - total multiplier. It's the number shown in the publication screen (in log format). This implicitly includes bonuses from students
 * @param publicationMultiplier - current publication multiplier for this publication
 * @param publicationMark - the rho at which publication multiplier = 1, represented in log format
 * @param research9Level - current research 9 level. Maximum in the game is 3R9, that is level 3
 * @param studentNumber - current number of students
 */ 
public class Theory implements Simmable {

    public Variable[] variables;
    public double rho;
    public double rhodot;
    public double maxRho;
    public double seconds; // elapsed time in seconds
    public int tickCount; // the number of elapsed ticks
    public final double totalMultiplier; //e.g. for T6 = T6^0.196 / 50
    public double publicationMultiplier; // current multiplier. e.g. for T6 usually publishes at about 10-30 multi
    public final double publicationMark; // Rho at which you can publish e.g. 2.75e965 etc
    public final static int research9Level = 3;
    public final static int studentNumber = 300;
    public final static double adBonus = 1.5;
    public static int theoryNumber;
    public double tauEfficiency; // Defined as maxRho divided by tickNumber

    public Strategy strategy;


    public Theory(int theoryNumber, double pubMark) {
       this.publicationMark = pubMark;
       Theory.theoryNumber = theoryNumber;
       if(Theory.theoryNumber == 1) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
            + 0.164 * this.publicationMark - Math.log10(3);
        } else if(Theory.theoryNumber == 6) {
           this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
            + 0.196 * this.publicationMark - Math.log10(50);
       } else if(Theory.theoryNumber == 2){
        this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
        + 0.198 * this.publicationMark - Math.log10(100);
       } else if(Theory.theoryNumber == 4) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.165 * this.publicationMark - Math.log10(4);
       }
        else if(Theory.theoryNumber == 7) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.152 * this.publicationMark;
        }
        else if(Theory.theoryNumber == 8) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.15 * this.publicationMark;
        }
        else if(Theory.theoryNumber == 3) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.147 * this.publicationMark + Math.log10(3);
        }
        else if(Theory.theoryNumber == 11) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.15 * this.publicationMark;
        }
       else {
           this.totalMultiplier = 1;
       }
    }

    public static <SpecificTheory extends Theory> SpecificTheory createTheory(int theoryNumber, double pubMark) {
        Theory.theoryNumber = theoryNumber;
        switch(theoryNumber) {
            case 1:
                //To be implemented
            case 2:
            return (SpecificTheory) new Theory2(pubMark);
            case 3:
                //To be implemented
            case 4:
                //To be implemented
            case 5:
                //To be implemented
            case 6:
                 return (SpecificTheory) new Theory6(pubMark);
            case 7:
                //To be implemented
            case 8:
                //To be implemented
        }
        return null;
    }

 
    public void moveTick() {
        // TODO Auto-generated method stub
        
    }

    
    /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array).
     * Both the values of the variables and rho are updated in this method.
     * @param variableNumber - the variable number to buy. Note that the variable number starts at 0, not 1.
     */
    public void buyVariable(int variableNumber) {
        double variableCost = this.variables[variableNumber].nextCost;
        if(this.rho >= variableCost) {
            this.variables[variableNumber].level += 1;
            this.variables[variableNumber].update();
            this.rho = Variable.subtract(this.rho, variableCost);
        } else {
            //too poor to buy stuff feelsbadman
        }
        
    }

    public void display() {

    }

    /**Coasts with autobuy off until publish. The publishing time is decided my taking the maximum of 
     * log(maxRho)/tickNumber. In this case, since rho is already calculated in log format, it's simply the 
     * maximum of maxRho/tickNumber.
     */
    public void waitUntilPublish() {
        double efficiency = this.tauEfficiency;
        int counter = 0;
        while(counter < 5) {
            efficiency = this.tauEfficiency;
            moveTick();
            counter++;
        }
       
        while(this.tauEfficiency >= efficiency) {
            efficiency = this.tauEfficiency;
            moveTick();
        }
        return; // publish
    }

    /**Runs the corresponding strategy attached with the theory.
     * 
     * @param name
     * @param type
     */
    public void runStrategy(String name, String type) {
        this.strategy = new Strategy(name, type);

        while(this.seconds < 60 * 60 * 24 * 30) {
            this.runStrategyOneLoop();
        }

        
    }
    
    public int runStrategyAI() {
        this.strategy = new Strategy("T" + Theory.theoryNumber, "AI");
        for(Variable variable : this.variables) {

        }
        return 1;
        
    }

   
    /**Idles  */
    protected void runStrategyOneLoop() {
        double leastExpensiveCost = Double.MAX_VALUE;
        int leastExpensiveVariableIndex = 0;
        
        // Determines which variable to buy according to their costs. Buys the variable with the 
        // least expensive cost.
        for(Integer variableNumber : this.strategy.autoBuyVariable) {
            if(this.variables[variableNumber].nextCost < leastExpensiveCost) {
                leastExpensiveCost = this.variables[variableNumber].nextCost;
                leastExpensiveVariableIndex = variableNumber;
            }
        
        }

        while(this.rho < leastExpensiveCost) {
            this.moveTick();
            if(this.tickCount % (10 * 60 * 60) == 0) {
                this.display();
            }
        }
        
        this.buyVariable(leastExpensiveVariableIndex);
    }
    @Override
    public void copy() {
        // TODO Auto-generated method stub
        
    }
   








    



}