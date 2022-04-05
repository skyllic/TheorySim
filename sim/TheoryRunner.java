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

        if (args.length == 4) {
            try {
                int studentNumber = Integer.parseInt(args[0]);
                int theoryNumber = Integer.parseInt(args[1]);
                double pubMark = Double.parseDouble(args[2]);
                SimRunner.runDetailedSim(studentNumber, theoryNumber, pubMark, true, "strategy=0");
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
                        summaries.addAll(SimRunner.runDetailedSim(studentNumber, i, pubMark, true, ""));
                    } else {
                        summaries.addAll(SimRunner.runDetailedSim(studentNumber, i + 1, pubMark, true, ""));
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
        /**SimRunner.runDetailedSim(300, 3, 680, true);
        SimRunner.runDetailedSim(300, 3, 682, true);
        SimRunner.runDetailedSim(300, 3, 684, true);
        SimRunner.runDetailedSim(300, 3, 686, true);
        SimRunner.runDetailedSim(300, 3, 688, true);
        SimRunner.runDetailedSim(300, 3, 690, true);
        SimRunner.runDetailedSim(300, 3, 692, true);*/
        //SimRunner.runDetailedSim(150, 1, 500, true);
        //SimRunner.runDetailedSim(200, 1, 550, true);
        //SimRunner.runDetailedSim(300, 13, 900, true, "stra=0");
        //SimRunner.runLongSim(300, 3, 600, 700);
        //SimRunner.runDetailedSim(300, 3, 300, true, "strate=0, ").get(0);
        //SimRunner.runDetailedSim(300, 3, 400, true, "strate=0, ").get(0);
        //SimRunner.runDetailedSim(300, 3, 500, true, "strate=0, ").get(0);
        SimRunner.runDetailedSim(300, 7, 580, true, "strate=0, ").get(0);
        //SimRunner.runDetailedSim(300, 3, 650, true, "strate=0, ").get(0);
        /**SimRunner.runDetailedSim(300, 3, 600, true, "strate=0, ").get(0);
        SimRunner.runDetailedSim(300, 3, 650, true, "strate=0, ").get(0);
        SimRunner.runDetailedSim(300, 3, 700, true, "strate=0, ").get(0);*/
   
        

        double i = 1000;
        double totalTime1 = 0;
        double totalTime2 = 0;
        double totalTime3 = 0;
        double totalTime4 = 0;
        double totalTime5 = 0;
        double totalTime6 = 0;
        double totalTime7 = 0;
        double totalTime8 = 0;
        /**
        //Snaeky's distribution.
        totalTime1 += SimRunner.runLongSim(300, 1, 400, 630 + Math.log10(7.3));
        totalTime2 += SimRunner.runLongSim(300, 2, 300, 562 + Math.log10(1.28));
        totalTime3 += SimRunner.runLongSim(300, 3, 400, 644 + Math.log10(2.81));
        totalTime4 += SimRunner.runLongSim(300, 4, 600, 751 + Math.log10(1.0));
        totalTime5 += SimRunner.runLongSim(300, 5, 800, 950 + Math.log10(8.72));
        totalTime6 += SimRunner.runLongSim(300, 6, 800, 1121 + Math.log10(8.6));
        totalTime7 += SimRunner.runLongSim(300, 7, 400, 604 + Math.log10(5.03));
        totalTime8 += SimRunner.runLongSim(300, 8, 400, 519 + Math.log10(4.72));*/

        /**
        //XLII's distribution.
        totalTime1 -= SimRunner.runLongSim(300, 1, 400, 646 + Math.log10(1.36));
        totalTime2 -= SimRunner.runLongSim(300, 2, 300, 568 + Math.log10(1.22));
        totalTime3 -= SimRunner.runLongSim(300, 3, 400, 645 + Math.log10(2.40));
        totalTime4 -= SimRunner.runLongSim(300, 4, 600, 732 + Math.log10(1.90));
        totalTime5 -= SimRunner.runLongSim(300, 5, 800, 945 + Math.log10(4.13));
        totalTime6 -= SimRunner.runLongSim(300, 6, 800, 1095 + Math.log10(8.0));
        totalTime7 -= SimRunner.runLongSim(300, 7, 400, 595 + Math.log10(2.69));
        totalTime8 -= SimRunner.runLongSim(300, 8, 400, 512 + Math.log10(1.80));*/

        /**
        //Spqcey's distribution.
        totalTime1 -= SimRunner.runLongSim(300, 1, 400, 640 + Math.log10(1.0));
        totalTime2 -= SimRunner.runLongSim(300, 2, 300, 566 + Math.log10(1.0));
        totalTime3 -= SimRunner.runLongSim(300, 3, 400, 649 + Math.log10(1.00));
        totalTime4 -= SimRunner.runLongSim(300, 4, 600, 742 + Math.log10(1.00));
        totalTime5 -= SimRunner.runLongSim(300, 5, 800, 954 + Math.log10(1.00));
        totalTime6 -= SimRunner.runLongSim(300, 6, 800, 1094 + Math.log10(1.0));
        totalTime7 -= SimRunner.runLongSim(300, 7, 400, 603 + Math.log10(1.0));
        totalTime8 -= SimRunner.runLongSim(300, 8, 400, 516 + Math.log10(1.0));*/

        /**
        //Playspout's distribution.
        totalTime1 += SimRunner.runLongSim(305, 1, 400, 640 + Math.log10(2.45));
        totalTime2 += SimRunner.runLongSim(305, 2, 300, 598 + Math.log10(3.50));
        totalTime3 += SimRunner.runLongSim(305, 3, 400, 642 + Math.log10(2.12));
        totalTime4 += SimRunner.runLongSim(305, 4, 600, 767 + Math.log10(1.57));
        totalTime5 += SimRunner.runLongSim(305, 5, 800, 953 + Math.log10(4.60));
        totalTime6 += SimRunner.runLongSim(305, 6, 800, 1201 + Math.log10(4.2));
        totalTime7 += SimRunner.runLongSim(305, 7, 400, 605 + Math.log10(1.04));
        totalTime8 += SimRunner.runLongSim(305, 8, 400, 519 + Math.log10(1.37));

        
        //Afuro's distribution.
        totalTime1 -= SimRunner.runLongSim(305, 1, 400, 632 + Math.log10(7.14));
        totalTime2 -= SimRunner.runLongSim(305, 2, 300, 580 + Math.log10(1.03));
        totalTime3 -= SimRunner.runLongSim(305, 3, 400, 617 + Math.log10(1.17));
        totalTime4 -= SimRunner.runLongSim(305, 4, 600, 777 + Math.log10(1.04));
        totalTime5 -= SimRunner.runLongSim(305, 5, 800, 1000 + Math.log10(1.0));
        totalTime6 -= SimRunner.runLongSim(305, 6, 800, 1172 + Math.log10(1.6));
        totalTime7 -= SimRunner.runLongSim(305, 7, 400, 614 + Math.log10(1.70));
        totalTime8 -= SimRunner.runLongSim(305, 8, 400, 524 + Math.log10(3.76));
        */

        /**
        //Nubest's distribution.
        totalTime1 -= SimRunner.runLongSim(300, 1, 400, 655 + Math.log10(1.0));
        totalTime2 -= SimRunner.runLongSim(300, 2, 300, 565 + Math.log10(2.65));
        totalTime3 -= SimRunner.runLongSim(300, 3, 400, 609 + Math.log10(1.68));
        totalTime4 -= SimRunner.runLongSim(300, 4, 600, 743 + Math.log10(4.53));
        totalTime5 -= SimRunner.runLongSim(300, 5, 800, 980 + Math.log10(1.35));
        totalTime6 -= SimRunner.runLongSim(300, 6, 800, 1127 + Math.log10(1.3));
        totalTime7 -= SimRunner.runLongSim(300, 7, 400, 600 + Math.log10(1.53));
        totalTime8 -= SimRunner.runLongSim(300, 8, 400, 518 + Math.log10(2.65));*/

        //Gen's distribution.
        /**
        totalTime1 -= SimRunner.runLongSim(305, 1, 400, 663 + Math.log10(1.11));
        totalTime2 -= SimRunner.runLongSim(305, 2, 300, 581 + Math.log10(4.86));
        totalTime3 -= SimRunner.runLongSim(305, 3, 400, 659 + Math.log10(1.32));
        totalTime4 -= SimRunner.runLongSim(305, 4, 600, 763 + Math.log10(2.11));
        totalTime5 -= SimRunner.runLongSim(305, 5, 800, 983 + Math.log10(6.02));
        totalTime6 -= SimRunner.runLongSim(305, 6, 800, 1173 + Math.log10(2.5));
        totalTime7 -= SimRunner.runLongSim(305, 7, 400, 619 + Math.log10(5.17));
        totalTime8 -= SimRunner.runLongSim(305, 8, 400, 524 + Math.log10(1.33));*/

        /**
        System.out.println(totalTime1);
        System.out.println(totalTime2);
        System.out.println(totalTime3);
        System.out.println(totalTime4);
        System.out.println(totalTime5);
        System.out.println(totalTime6);
        System.out.println(totalTime7);
        System.out.println(totalTime8);*/
        /**while(i < 12000) {
            Summary summary = SimRunner.runDetailedSim(305, 11, i, true, "strategy=0, ").get(0);
            i = i + summary.tauGain;
            //summaryList.add(summary.pubTime);
            totalTime += summary.pubTime;

            if(totalTime > 24 * 365) {
                break;
            }
             
        }

        for(Double element : summaryList) {
            //System.out.println(element);
        }
        System.out.println(totalTime);
        
      */
        
  

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