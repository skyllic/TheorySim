package sim;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javafx.stage.Stage;
import sim.data.DataGenerator;
import sim.theory.Euler_Formula;
import sim.theory.ITheory;
import sim.theory.Theory;
import sim.theory.TheoryFactory;
import sim.upgrades.Variable;

/**
 * A TheoryRunner class containing the main method used to run to theory
 * simulator
 */
public class TheoryRunner {

    public static final int NUMBER_OF_THEORIES = 10;

    public static void main(String[] args) throws IOException {

        if (args.length == 3) {
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
        
        //SimRunner.runChainSims(300, 1, 300, 1300, "print=true,strategy=0");
      
        //simAllStrategies();

        SimRunner simRunner = new SimRunner();

        
        //SimRunner.runStrategicSim(300, 12, 10, 45, "strategy=first");
        //double[] dist1 = new double[] {671, 588, 681, 772.8, 984, 1194, 628.8, 535};
        //double[] dist2 = new double[] {631, 631, 629, 727.6, 942, 1115.3, 590.1, 508.5};
        //double[] dist2 = new double[] {641, 608.5, 667, 773.7, 953.6, 1214, 640, 528.5};
      
        //SimRunner.runDistributionComparison(dist1, dist2, 315, 313);
        //Theory theory = new Theory(1, 610);
        
        SimRunner.runChainSims(300, 14, 80,147, "strategy=first");
        //SimRunner.runStrategicSim(300, 14, 0, 50, "print=true,strategy=first");
        //SimRunner.runDetailedSim(300, 1, 600 + Math.log10(6.22),
        // true, "strategy=frst");
       //double totalTime = SimRunner.runChainSims(300, 1, 600, 700, 
       //"print=true,strategy=second")[0];
       
       
  
       
        Summary summary;
        ArrayList<Double> tau_per_hours = new ArrayList<>();

       

        double i = 1000;
        double totalTime1 = 0;
        double totalTime2 = 0;
        double totalTime3 = 0;
        double totalTime4 = 0;
        double totalTime5 = 0;
        double totalTime6 = 0;
        double totalTime7 = 0;
        double totalTime8 = 0;

        //totalTime1 += SimRunner.runLongSim(30, 2, 0, 250, "strategy=T2NoMS");
        /**totalTime1 += SimRunner.runLongSim(210, 1, 200, 519 + Math.log10(1.00));
        totalTime2 += SimRunner.runLongSim(210, 2, 200, 441 + Math.log10(1.28));
        totalTime3 += SimRunner.runLongSim(210, 3, 200, 524 + Math.log10(2.81));
        totalTime4 += SimRunner.runLongSim(210, 4, 200, 532 + Math.log10(1.0));
        totalTime5 += SimRunner.runLongSim(210, 5, 200, 803 + Math.log10(8.72));
        totalTime6 += SimRunner.runLongSim(210, 6, 200, 795 + Math.log10(8.6));
        totalTime7 += SimRunner.runLongSim(210, 7, 200, 494 + Math.log10(5.03));
        totalTime8 += SimRunner.runLongSim(210, 8, 200, 450 + Math.log10(4.72));*/
        
        //Snaeky's distribution.
        /**
        totalTime1 += SimRunner.runLongSim(300, 1, 400, 640 + Math.log10(1.46), "strategy=0");
        totalTime2 += SimRunner.runLongSim(300, 2, 300, 562 + Math.log10(1.28), "strategy=0");
        totalTime3 += SimRunner.runLongSim(300, 3, 400, 655 + Math.log10(1.18), "strategy=0");
        totalTime4 += SimRunner.runLongSim(300, 4, 600, 751 + Math.log10(1.00), "strategy=0");
        totalTime5 += SimRunner.runLongSim(300, 5, 800, 954 + Math.log10(1.68), "strategy=0");
        totalTime6 += SimRunner.runLongSim(300, 6, 800, 1127 + Math.log10(2.5), "strategy=0");
        totalTime7 += SimRunner.runLongSim(300, 7, 400, 604 + Math.log10(5.03), "strategy=0");
        totalTime8 += SimRunner.runLongSim(300, 8, 400, 519 + Math.log10(4.72), "strategy=0");
*/
        /**
        //XLII's distribution.
        totalTime1 += SimRunner.runLongSim(300, 1, 400, 646 + Math.log10(1.23), "strategy=0");
        totalTime2 += SimRunner.runLongSim(300, 2, 300, 586 + Math.log10(1.23), "strategy=0");
        totalTime3 += SimRunner.runLongSim(300, 3, 400, 645 + Math.log10(2.40), "strategy=0");
        totalTime4 += SimRunner.runLongSim(300, 4, 600, 732 + Math.log10(1.90), "strategy=0");
        totalTime5 += SimRunner.runLongSim(300, 5, 800, 945 + Math.log10(4.13), "strategy=0");
        totalTime6 += SimRunner.runLongSim(300, 6, 800, 1105 + Math.log10(7.1), "strategy=0");
        totalTime7 += SimRunner.runLongSim(300, 7, 400, 595 + Math.log10(2.69), "strategy=0");
        totalTime8 += SimRunner.runLongSim(300, 8, 400, 512 + Math.log10(1.80), "strategy=0");*/

        /**
        //Solarion's distribution.
        totalTime1 -= SimRunner.runLongSim(299, 1, 400, 618 + Math.log10(1.00), "strategy=0");
        totalTime2 -= SimRunner.runLongSim(299, 2, 300, 630 + Math.log10(4.34), "strategy=0");
        totalTime3 -= SimRunner.runLongSim(299, 3, 400, 623 + Math.log10(9.89), "strategy=0");
        totalTime4 -= SimRunner.runLongSim(299, 4, 600, 727 + Math.log10(3.71), "strategy=0");
        totalTime5 -= SimRunner.runLongSim(299, 5, 800, 938 + Math.log10(2.89), "strategy=0");
        totalTime6 -= SimRunner.runLongSim(299, 6, 800, 1107 + Math.log10(1.8), "strategy=0");
        totalTime7 -= SimRunner.runLongSim(299, 7, 400, 580 + Math.log10(4.97), "strategy=0");
        totalTime8 -= SimRunner.runLongSim(299, 8, 400, 497 + Math.log10(5.79), "strategy=0");
*/
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
        SimRunner.runDetailedSim(308, 1, 640 + Math.log10(2.45), true , "strategy=all");
        totalTime2 += SimRunner.runLongSim(308, 2, 598 + Math.log10(3.50), "strategy=all");
        totalTime3 += SimRunner.runLongSim(308, 3, 663 + Math.log10(2.12), "strategy=all");
        totalTime4 += SimRunner.runLongSim(308, 4, 767 + Math.log10(1.57), "strategy=all");
        totalTime5 += SimRunner.runLongSim(308, 5, 953 + Math.log10(4.60), "strategy=all");
        totalTime6 += SimRunner.runLongSim(308, 6, 1205 + Math.log10(1.0), "strategy=all");
        totalTime7 += SimRunner.runLongSim(308, 7, 605 + Math.log10(1.04), "strategy=all");
        totalTime8 += SimRunner.runLongSim(308, 8, 519 + Math.log10(1.37), "strategy=all");*/
/**
        totalTime1 += SimRunner.runLongSim(308, 1, 400, 640 + Math.log10(2.45), "strategy=0");
        totalTime2 += SimRunner.runLongSim(308, 2, 300, 598 + Math.log10(3.50), "strategy=0");
        totalTime3 += SimRunner.runLongSim(308, 3, 400, 663 + Math.log10(2.12), "strategy=0");
        totalTime4 += SimRunner.runLongSim(308, 4, 600, 767 + Math.log10(1.57), "strategy=0");
        totalTime5 += SimRunner.runLongSim(308, 5, 800, 953 + Math.log10(4.60), "strategy=0");
        totalTime6 += SimRunner.runLongSim(308, 6, 800, 1205 + Math.log10(1.0), "strategy=0");
        totalTime7 += SimRunner.runLongSim(308, 7, 400, 605 + Math.log10(1.04), "strategy=0");
        totalTime8 += SimRunner.runLongSim(308, 8, 400, 519 + Math.log10(1.37), "strategy=0");
*/
        
         /** 
        //Afuro's distribution.
        totalTime1 -= SimRunner.runLongSim(307, 1, 400, 632 + Math.log10(7.14), "strategy=0");
        totalTime2 -= SimRunner.runLongSim(307, 2, 300, 580 + Math.log10(1.03), "strategy=0");
        totalTime3 -= SimRunner.runLongSim(307, 3, 400, 617 + Math.log10(1.17), "strategy=0");
        totalTime4 -= SimRunner.runLongSim(307, 4, 600, 777 + Math.log10(1.04), "strategy=0");
        totalTime5 -= SimRunner.runLongSim(307, 5, 800, 1000 + Math.log10(1.0), "strategy=0");
        totalTime6 -= SimRunner.runLongSim(307, 6, 800, 1172 + Math.log10(1.6), "strategy=0");
        totalTime7 -= SimRunner.runLongSim(307, 7, 400, 614 + Math.log10(1.70), "strategy=0");
        totalTime8 -= SimRunner.runLongSim(307, 8, 400, 524 + Math.log10(3.76), "strategy=0");
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
        totalTime1 -= SimRunner.runLongSim(310, 1, 400, 663 + Math.log10(1.11), "strategy=0");
        totalTime2 -= SimRunner.runLongSim(310, 2, 300, 581 + Math.log10(4.86), "strategy=0");
        totalTime3 -= SimRunner.runLongSim(310, 3, 400, 666 + Math.log10(6.66), "strategy=0");
        totalTime4 -= SimRunner.runLongSim(310, 4, 600, 763 + Math.log10(2.11), "strategy=0");
        totalTime5 -= SimRunner.runLongSim(310, 5, 800, 983 + Math.log10(6.02), "strategy=0");
        totalTime6 -= SimRunner.runLongSim(310, 6, 800, 1178 + Math.log10(2.5), "strategy=0");
        totalTime7 -= SimRunner.runLongSim(310, 7, 400, 625 + Math.log10(5.17), "strategy=0");
        totalTime8 -= SimRunner.runLongSim(310, 8, 400, 534 + Math.log10(1.33), "strategy=0");*/

        /**
        System.out.println(totalTime1);
        System.out.println(totalTime2);
        System.out.println(totalTime3);
        System.out.println(totalTime4);
        System.out.println(totalTime5);
        System.out.println(totalTime6);
        System.out.println(totalTime7);
        System.out.println(totalTime8);
        double totalTime = totalTime1 + totalTime2 + totalTime3 + totalTime4 + 
            totalTime5 + totalTime6 + totalTime7 + totalTime8;
        System.out.println(totalTime);*/
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

    public static void simAllStrategies() {
        //Playspout's distribution.
        SimRunner.runDetailedSim(308, 1, 640 + Math.log10(2.45), true , "strategy=all");
        SimRunner.runDetailedSim(308, 2, 598 + Math.log10(3.50), true, "strategy=all");
        SimRunner.runDetailedSim(308, 3, 663 + Math.log10(2.12), true, "strategy=all");
        SimRunner.runDetailedSim(308, 4, 767 + Math.log10(1.57), true, "strategy=all");
        SimRunner.runDetailedSim(308, 5, 953 + Math.log10(4.60), true, "strategy=all");
        SimRunner.runDetailedSim(308, 6, 1205 + Math.log10(1.0), true, "strategy=all");
        SimRunner.runDetailedSim(308, 7, 605 + Math.log10(1.04), true, "strategy=all");
        SimRunner.runDetailedSim(308, 8, 519 + Math.log10(1.37), true, "strategy=all");
        SimRunner.runDetailedSim(308, 10, 1037 + Math.log10(1.37), true, "strategy=all");
        SimRunner.runDetailedSim(308, 11, 1045 + Math.log10(1.37), true, "strategy=all");
    }

    public static void simEndgame(String flag) {
        if(flag.contains("T1=")) {

        }
    }
    

    public static void findDataForGraph(int studentNumber, int theoryNumber, double start, double finish, String flag) {
        if(flag.equalsIgnoreCase("variableBuyFrequency")) {
            int variableSumCounter = 0;
            while(true) {
                Variable[] variables = SimRunner.runDetailedSim(studentNumber, theoryNumber, start, false, flag)
                .get(0).variables;

                for(int i = 0; i < variables.length; i++) {
                    if(variables[i].cost > start) {

                    }
                }
            }
        }
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