package sim;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

/**
 * A TheoryRunner class containing the main method used to run to theory
 * simulator
 */
public class TheoryRunner {

    public static final int NUMBER_OF_THEORIES = 10;

    public static void main(String[] args) {

        if (args.length == 3) {
            try {
                int studentNumber = Integer.parseInt(args[0]);
                int theoryNumber = Integer.parseInt(args[1]);
                double pubMark = Double.parseDouble(args[2]);
                SimRunner.runDetailedSim(studentNumber, theoryNumber, pubMark, true);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else if (args.length != 0) {
            ArrayList<Summary> summaries = new ArrayList<>();
            
            try {
                int studentNumber = Integer.parseInt(args[0]);
                for (int i = 1; i < args.length; i++) {
                    double pubMark = Double.parseDouble(args[i]);
                    if(i < 9) {
                        summaries.addAll(SimRunner.runDetailedSim(studentNumber, i, pubMark, true));
                    } else {
                        summaries.addAll(SimRunner.runDetailedSim(studentNumber, i + 1, pubMark, true));
                    }
                }
                for(int i = 0; i < summaries.size(); i++) {

                for(Summary summary : summaries) {
                    String idleBest = "";
                    String activeBest = "";
                    if(summary.theoryNumber == i+1) {

                    }
                }
            }
            } catch (Exception e) {

            }
        }

        ArrayList<Double> summaryList = new ArrayList<>();
        //SimRunner.runDetailedSim(100, 1, 400, true);
        //SimRunner.runDetailedSim(150, 1, 500, true);
        //SimRunner.runDetailedSim(200, 1, 550, true);
        //SimRunner.runDetailedSim(300, 1, 625, true);
        //SimRunner.runLongSim(300, 3, 600, 700);

        double i = 150;
        double totalTime = 0;
        while(i < 900) {
            Summary summary = SimRunner.runDetailedSim(300, 13, i, true).get(0);
            i = i + summary.tauGain;
            totalTime += summary.pubTime;
             
        }

        System.out.println(totalTime);
        
      
        
  

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
                    if (weierStrass.publicationMultiplier >= pubMulti && counter == 0) {
                        startPubTime = weierStrass.seconds; // grab the start pub time.
                        System.out.println(startPubTime);
                        counter = 1;
                    }
                    if (weierStrass.seconds >= startPubTime + 3600 * 8.0 && counter == 1) {
                        // finish overnight
                        weierStrass.isCoasting = true;
                        weierStrass.maxTauPerHour = (weierStrass.maxRho - weierStrass.publicationMark)
                                / (weierStrass.seconds / 3600.0);
                        weierStrass.bestPubMulti = weierStrass.publicationMultiplier;
                        weierStrass.bestPubTime = weierStrass.seconds / 3600.0;
                        weierStrass.bestTauGain = weierStrass.maxRho - weierStrass.publicationMark;
                    }
                }
                if (printHeaderCounter == 0) {
                    weierStrass.printSummaryHeader();
                    printHeaderCounter = 1;
                }
                weierStrass.printSummary();

            }
        }
        double finish = System.currentTimeMillis();

        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
    }

    public static void runAllSim(int studentNumber, String t1Tau, String t2Tau, String t3Tau, String t4Tau,
            String t5Tau, String t6Tau, String t7Tau, String t8Tau, String WSPTau, String SLTau) {

        String[] taus = { t1Tau, t2Tau, t3Tau, t4Tau, t5Tau, t6Tau, t7Tau, t8Tau, WSPTau, SLTau };
        double[] tauNumbers = new double[taus.length];
        String[] mantissas = new String[taus.length];
        String[] powers = new String[taus.length];
        try {
            for (int i = 0; i < taus.length; i++) {
                if (taus[i].contains("e")) {
                    String[] temp = taus[i].split("e");
                    mantissas[i] = temp[0];
                    powers[i] = temp[1];
                    tauNumbers[i] = Math.log10(Double.parseDouble(mantissas[i])) +
                            Double.parseDouble(powers[i]);

                } else {
                    tauNumbers[i] = Double.parseDouble(taus[i]);
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

   
}