import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;

/**
 * A TheoryRunner class containing the main method used to run to theory
 * simulator
 */
public class TheoryRunner {

    public static void main(String[] args) {

        runDetailedSim(300, 10, 600 + Math.log10(1));
        //runIndividualSim(300, 10, 764 + Math.log10(1));

        //findBestStrats(300, 11, 300, 1000, 1);
        //findBestStrats(300, 10, 400, 1000, 1);
        //runDetailedSim(300, 10, 870 + Math.log10(1));
        //runDetailedSim(300, 4, 781 + Math.log10(1));
        // runOvernightComparison(300, 10, 970, 5, "WSP");
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

    public static void findBestStrats(int studentNumber, int theoryNumber, 
    double startRho, double finishRho, double gap) {

        for(double i = startRho; i < finishRho; i = i + gap) {
            runIndividualSim(studentNumber, theoryNumber, i);
        }

    }
    public static void runDetailedSim(int studentNumber, int theoryNumber, double pubMark) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;
        ArrayList<Strategy> strategies = new ArrayList<>();

        Theory.studentNumber = studentNumber;
        if (theoryNumber == 4) {
            strategies.add(new Strategy("T4Sol2", "active"));
            strategies.add(new Strategy("T4Solar", "active"));
            strategies.add(new Strategy("T4Gold", "active"));
            strategies.add(new Strategy("T4C3d", "active"));
            strategies.add(new Strategy("T4C3", "idle"));

            for (Strategy strategy : strategies) {
                Theory4 t4 = new Theory4(pubMark);
                t4.strategy = strategy;

                while (t4.finishCoasting == false) {
                    t4.runEpoch();
                }
                if (printHeaderCounter == 0) {
                    t4.printSummaryHeader();
                    printHeaderCounter = 1;
                }
                t4.printSummary();

            }
        } else if (theoryNumber == 10) {

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
                if (printHeaderCounter == 0) {
                    weierStrass.printSummaryHeader();
                    printHeaderCounter = 1;
                }
                weierStrass.printSummary();

            }
        } else if (theoryNumber == 11) {
            strategies.add(new Strategy("SLPlay", "active"));
            strategies.add(new Strategy("SLst", "idle"));
            strategies.add(new Strategy("SLd", "active"));
            

            for (Strategy strategy : strategies) {
                Sequential_Limit SL = new Sequential_Limit(pubMark);
                SL.strategy = strategy;

                while (SL.finishCoasting == false) {
                    SL.runEpoch();
                }
                if (printHeaderCounter == 0) {
                    SL.printSummaryHeader();
                    printHeaderCounter = 1;
                }
                SL.printSummary();

            }
            double finish = System.currentTimeMillis();

            System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
        }
    }

    public static void runIndividualSim(int studentNumber, int theoryNumber, double pubMark) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;
        ArrayList<Strategy> strategies = new ArrayList<>();

        Theory.studentNumber = studentNumber;
        if (theoryNumber == 4) {
            strategies.add(new Strategy("T4Sol2", "active"));
            strategies.add(new Strategy("T4Solar", "active"));
            strategies.add(new Strategy("T4Gold", "active"));
            strategies.add(new Strategy("T4C3d", "active"));
            strategies.add(new Strategy("T4C3", "idle"));


            double bestTauPerHour = 0;
            Theory4 t4 = new Theory4(pubMark);
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                t4 = new Theory4(pubMark);
                t4.strategy = strategy;

                while (t4.finishCoasting == false) {
                    t4.runEpoch();
                }
                if(t4.getSummary().tauPerHour > bestTauPerHour) {
                    summary = t4.getSummary();
                    bestTauPerHour = t4.getSummary().tauPerHour;
                };

            }
            t4.printSummary(summary);
        } else if (theoryNumber == 10) {

            strategies.add(new Strategy("WSPlay2", "active"));
            strategies.add(new Strategy("WSPlay", "active"));
            strategies.add(new Strategy("WSPd", "active"));
            strategies.add(new Strategy("WSPStC1", "idle"));
            strategies.add(new Strategy("WSP", "idle"));


            double bestTauPerHour = 0;
            WeierStrass weierStrass = new WeierStrass(pubMark);
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                weierStrass = new WeierStrass(pubMark);
                weierStrass.strategy = strategy;

                while (weierStrass.finishCoasting == false) {
                    weierStrass.runEpoch();
                }
                if(weierStrass.getSummary().tauPerHour > bestTauPerHour) {
                    summary = weierStrass.getSummary();
                    bestTauPerHour = weierStrass.getSummary().tauPerHour;
                };

            }
            weierStrass.printSummary(summary);
        } else if (theoryNumber == 11) {
            strategies.add(new Strategy("SLPlay", "active"));
            strategies.add(new Strategy("SLst", "idle"));
            strategies.add(new Strategy("SLd", "active"));
            

            double bestTauPerHour = 0;
            Sequential_Limit SL = new Sequential_Limit(pubMark);
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                SL = new Sequential_Limit(pubMark);
                SL.strategy = strategy;

                while (SL.finishCoasting == false) {
                    SL.runEpoch();
                }
               
                if(SL.getSummary().tauPerHour > bestTauPerHour) {
                    summary = SL.getSummary();
                    bestTauPerHour = SL.getSummary().tauPerHour;
                };
                

            }
            SL.printSummary(summary);
            
            double finish = System.currentTimeMillis();

            //System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
        }
    }
}