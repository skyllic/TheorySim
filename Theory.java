

/**A class representing a theory. 
 * @param rho - the current rho in log format.
 * @param rhodot - the current rhodot in log format. For theory with multiple rho types, this field represents the rho that contributes to tau directly
 * @param seconds - the current elapsed time in seconds. Resets on publish. Does not reset on variable bought. Is affected by adaptive ticks.
 * @param tickCount - the current number of ticks. Default is 10 ticks/second. Resets on publish. Does not reset on variable bought.
 *  When adaptive ticks are applied, the tickCount rate doesn't change. 
 * @param totalMultiplier - total multiplier. It's the number shown in the publication screen (in log format).
 *  This implicitly includes bonuses from students. For custom theories, this DOES NOT include bonuses from students.
 * @param publicationMultiplier - current publication multiplier for this publication. Can be less than 1. 
 *  A publication multiplier less than 1 implies that the theory hasn't recovered back to its previous 
 *  publication mark yet. 
 * @param publicationMark - the rho at which publication multiplier = 1, represented in log format.
 * @param research9Level - current research 9 level. Maximum in the game is 3R9, that is level 3.
 * @param studentNumber - current number of students.
 */ 
public class Theory implements Simmable {

    public String name;
    public Variable[] variables;
    public double rho;
    public double rhodot;
    public double maxRho;
    public double seconds; // elapsed time in seconds
    public int tickCount; // the number of elapsed ticks
    public double tauPerHour; // current log10(tau) per hour of the theory
    public double tickFrequency; //second per tick (default 0.1 seconds/tick)
    public boolean isCoasting = false;
    public boolean finishCoasting = false;
    public double coastingCoefficient = 1.15;
    public int coastingNumber = 0;
    public double maxTauPerHour;
    public double bestPubMulti;
    public double bestPubTime;
    public double bestTauGain;
    public double bestCoastingNumber = 0;
    public int activeFrequency = 10;

    public double tauPerHourActive;
    public double tauPerHourIdle;
    public double pubMultiActive;
    public double pubMultiIdle;
    public String strategyActive;
    public String strategyIdle;
    public double pubTimeActive;
    public double pubTimeIdle;
    public double tauGainActive;
    public double tauGainIdle;

    public final double totalMultiplier; //e.g. for T6 = T6^0.196 / 50
    public double publicationMultiplier; // current multiplier. e.g. for T6 usually publishes at about 10-30 multi
    public final double publicationMark; // Rho at which you can publish e.g. 2.75e965 etc
    public static int research9Level = 3; // default 3 for lategame purposes.
    public static int studentNumber = 300; // default 300 for endgame purposes.

    public final static double adBonus = 1.5;
    public static int theoryNumber; // First Custom Theory = theory10.
    public double tauEfficiency; // Defined as maxRho divided by tickNumber.

    public Strategy strategy;

    /**
     * 
     * @param theoryNumber - theory number to generate. Default theories are 1-8. First CT is theory 10. 
     * @param pubMark - the last pub mark of the theory in log format. e.g. if last pub mark was at 2e600 then 
     *  pubMark = log10(2) + 600. For CT pubmarks, use the RHO rather than tau. 
     */
    public Theory(int theoryNumber, double pubMark) {
       this.publicationMark = pubMark;
       Theory.theoryNumber = theoryNumber;

        this.seconds = 0;
        this.tickCount = 0;
        this.rho = 0;
        this.rhodot = 0;
        this.tickFrequency = 0.1; // seconds per tick

       //Sets total multiplier for each theory. Each theory has its own formula. 
       if(Theory.theoryNumber == 1) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
            + 0.164 * this.publicationMark - Math.log10(3);
        } else if(Theory.theoryNumber == 2){
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.198 * this.publicationMark - Math.log10(100);
        } else if(Theory.theoryNumber == 3) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.147 * this.publicationMark + Math.log10(3);
        } else if(Theory.theoryNumber == 4) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.165 * this.publicationMark - Math.log10(4);
        } else if(Theory.theoryNumber == 5) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.159 * this.publicationMark;
        } else if(Theory.theoryNumber == 6) {
           this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
            + 0.196 * this.publicationMark - Math.log10(50);
        } else if(Theory.theoryNumber == 7) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.152 * this.publicationMark;
        } else if(Theory.theoryNumber == 8) {
            this.totalMultiplier = Theory.research9Level * (Math.log10(Theory.studentNumber) - Math.log10(20)) 
                + 0.15 * this.publicationMark;
        }
        else if(Theory.theoryNumber == 10) {
            this.totalMultiplier = 0.15 * this.publicationMark;
        }
        else if(Theory.theoryNumber == 11) {
            this.totalMultiplier = 0.15 * this.publicationMark;
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

 
    /**Intended to be overridden by corresponding theory. */
    
    public void moveTick() {
        this.seconds += this.tickFrequency;
        this.tickCount += 1;

        if (this.rho > this.maxRho) {
            this.maxRho = this.rho;
        }
        if ((this.maxRho - this.publicationMark) / (this.seconds / 3600.0) > this.maxTauPerHour) {
            this.maxTauPerHour = (this.maxRho - this.publicationMark) / (this.seconds / 3600.0);
            this.bestPubMulti = this.publicationMultiplier;
            this.bestPubTime = this.seconds / 3600.0;
            this.bestTauGain = this.maxRho - this.publicationMark;
        }    
    }

    
    /**Buys 1 level of the variable according to the variableNumber input. For example, an input of 3
     * means buy 1 level of variable[3] (the 4th variable in the array). The nth variable in the array is read 
     * top-down in-game. 
     * Both the values of the variables and rho are updated in this method, so there is no need to further 
     * update the variable parameters. 
     * <p>Some theories may wish to override this method as there may be more than one type of rhos. </p>
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

    

    /**Displays intermittent theory parameters for testing purposes. Not intended for UI output to users. */
    public void display() {

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
    
    public int findBestVarToBuy() {
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
    
    public void printSummaryHeader() {
        System.out.println(this.name + " at e" + String.format("%.1f", this.publicationMark) + " rho" + 
            ", " + Theory.studentNumber + " students" );
        System.out.print("Tau/hr\t\t" + "PubMulti\t\t" + "Strategy\t\t" + "PubTime\t\t" + "TauGain\n");
    }

    public void printSummary() {
        System.out.print(String.format("%.5f",
                this.maxTauPerHour));
        System.out.print("\t\t" + String.format("%.4f", this.bestPubMulti) + "\t\t\t");
        System.out.print(String.format("%s", this.strategy.name) + "\t\t\t");
        System.out.print(String.format("%.4f", this.bestPubTime));
        System.out.print("\t\t" + String.format("%.4f", this.bestTauGain));
        System.out.println("");

    }

    public void printSummary(Summary summary) {
        System.out.print(String.format("%.4f",
                summary.tauPerHour));
        System.out.print("\t\t" + String.format("%.4f", summary.pubMulti) + "\t\t\t");
        System.out.print(String.format("%s", summary.strategy) + "\t\t\t");
        System.out.print(String.format("%.4f", summary.pubTime));
        System.out.print("\t\t" + String.format("%.4f", summary.tauGain));
        System.out.print("\t\t" + String.format("%.1f", this.publicationMark));
        System.out.println("");

    }

    public Summary getSummary() {
        Summary summary = new Summary(this.maxTauPerHour, this.bestPubMulti, this.strategy.name,
         this.bestPubTime, this.bestTauGain);

         return summary;
    }
   








    



}