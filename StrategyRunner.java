import java.util.ArrayList;

public class StrategyRunner<SpecificTheory extends Theory> {


    public Strategy strategy;
    public Theory theory1;
    public Theory theory2;
    public Theory theory3;
    public Theory theory4;
    public Theory theory5;
    public Theory6 theory6;
    public Theory theory7;
    public Theory theory8;



    public Theory[] theory = new Theory[20];

    public Theory theorys;




    public StrategyRunner(String strategyName, String strategyType, int theoryNumber, double pubMark, Theory theory) {
        this.theorys = theory;
       switch(Theory.theoryNumber) {
            case 6:
                this.theory[6] = (Theory6) theory;
                
       }
        this.strategy = new Strategy(strategyName, strategyType);
       
        
        
        
    }

    public void runStrategy() {
        if(this.strategy.type.equalsIgnoreCase("idle")) {

        }
    }

    public int determineNextBuyVariable() {
        if(this.strategy.type.equalsIgnoreCase("idle")) {
            for(Integer variable: this.strategy.autoBuyVariable) {
                
            }
        }
        return 0;
    }
}