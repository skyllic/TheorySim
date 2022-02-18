import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;

/**
 * A TheoryRunner class containing the main method used to run to theory
 * simulator
 */
public class TheoryRunner {

    public static void main(String[] args) {

        //runDetailedSim(300, 10, 970);
        runOvernightComparison(300, 10, 970, 5, "WSP");
    }

    public static void runOvernightComparison(int studentNumber, int theoryNumber, double pubMark, double pubMulti, 
        String strategyName) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;

        Theory.studentNumber = studentNumber;
        if (theoryNumber == 10) {
            
            ArrayList<Strategy> strategies = new ArrayList<>();

            strategies.add(new Strategy(strategyName, "idle"));
            

            double startPubTime = 0;
            int counter = 0;

            for (Strategy strategy : strategies) {
                WeierStrass weierStrass = new WeierStrass(pubMark);
                
                weierStrass.strategy = strategy;

                while (weierStrass.finishCoasting == false) {
                    weierStrass.runEpoch();
                    if(weierStrass.publicationMultiplier >= pubMulti && counter == 0) {
                        startPubTime = weierStrass.seconds; // grab the start pub time.
                        System.out.println(startPubTime);
                        counter = 1;
                    }
                    if(weierStrass.seconds >= startPubTime + 3600 * 8.0 && counter == 1) {
                        //finish overnight
                        weierStrass.isCoasting = true;
                        weierStrass.maxTauPerHour = (weierStrass.maxRho - weierStrass.publicationMark) / (weierStrass.seconds / 3600.0);
                        weierStrass.bestPubMulti = weierStrass.publicationMultiplier;
                        weierStrass.bestPubTime = weierStrass.seconds / 3600.0;
                        weierStrass.bestTauGain = weierStrass.maxRho - weierStrass.publicationMark;
                    }
                }
                if(printHeaderCounter == 0) {
                   weierStrass.printSummaryHeader();
                   printHeaderCounter = 1; 
                }
                weierStrass.printSummary();

            }
        }
        double finish = System.currentTimeMillis();

        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
    }

    public static void runDetailedSim(int studentNumber, int theoryNumber, double pubMark) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;

        Theory.studentNumber = studentNumber;
        if (theoryNumber == 10) {
            
            ArrayList<Strategy> strategies = new ArrayList<>();

            strategies.add(new Strategy("WSPlay2", "active"));
            strategies.add(new Strategy("WSPlay", "active"));
            strategies.add(new Strategy("WSPd", "active"));
            strategies.add(new Strategy("WSPStC1", "idle"));
            strategies.add(new Strategy("WSP", "idle"));

            for (Strategy strategy : strategies) {
                WeierStrass weierStrass = new WeierStrass(pubMark);
                weierStrass.strategy = strategy;

                while (weierStrass.finishCoasting == false) {
                    weierStrass.runEpoch();
                }
                if(printHeaderCounter == 0) {
                   weierStrass.printSummaryHeader();
                   printHeaderCounter = 1; 
                }
                weierStrass.printSummary();

            }
        }
        double finish = System.currentTimeMillis();

        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
    }
}