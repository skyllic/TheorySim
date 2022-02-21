import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;

/**
 * A TheoryRunner class containing the main method used to run to theory
 * simulator
 */
public class TheoryRunner {

    public static void main(String[] args) {

        runDetailedSim(300, 10, 984 + Math.log10(1), true);
        // runIndividualSim(300, 10, 764 + Math.log10(1));

         //findBestStrats(300, 10, 1000, 3000, 100);
        // findBestStrats(300, 10, 400, 1000, 1);
        // runDetailedSim(300, 10, 870 + Math.log10(1));
        // runDetailedSim(300, 4, 757 + Math.log10(1));
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

        for (double i = startRho; i < finishRho; i = i + gap) {
            runIndividualSim(studentNumber, theoryNumber, i);
        }

    }

    public static void runDetailedSim(int studentNumber, int theoryNumber, double pubMark,
            boolean coast) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;
        ArrayList<Strategy> strategies = new ArrayList<>();

        Theory.studentNumber = studentNumber;

        if(theoryNumber == 1) {
            strategies.add(new Strategy("T1Play", "active"));

            Theory1 t1 = new Theory1(pubMark);
            
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 3; i++) {
                    t1 = new Theory1(pubMark);
                    t1.strategy = strategy;

                    if (i > 0) {
                        t1.coastingPub = bestPubMulti - 0.30 * i * bestPubMulti;
                    }

                    while (t1.finishCoasting == false) {
                        t1.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        t1.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || t1.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = t1.getSummary().pubMulti;
                        summary = new Summary(t1.maxTauPerHour,
                                t1.bestPubMulti, t1.strategy.name, t1.bestPubTime, t1.bestTauGain,
                                t1.coastStart);
                    }
                }
                t1.printSummary(summary);

            }
        }
        else if (theoryNumber == 2) {
            strategies.add(new Strategy("T2Coast", "idle"));

            Theory2 t2 = new Theory2(pubMark);
            
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 3; i++) {
                    t2 = new Theory2(pubMark);
                    t2.strategy = strategy;

                    if (i > 0) {
                        t2.coastingPub = bestPubMulti - 0.30 * i * bestPubMulti;
                    }

                    while (t2.finishCoasting == false) {
                        t2.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        t2.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || t2.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = t2.getSummary().pubMulti;
                        summary = new Summary(t2.maxTauPerHour,
                                t2.bestPubMulti, t2.strategy.name, t2.bestPubTime, t2.bestTauGain,
                                t2.coastStart);
                    }
                }
                t2.printSummary(summary);

            }
        }
        else if(theoryNumber == 3) {
            strategies.add(new Strategy("T3Play2", "active"));

            Theory3 t3 = new Theory3(pubMark);
            
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 3; i++) {
                    t3 = new Theory3(pubMark);
                    t3.strategy = strategy;

                    if (i > 0) {
                        t3.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
                    }

                    while (t3.finishCoasting == false) {
                        t3.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        t3.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || t3.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = t3.getSummary().pubMulti;
                        summary = new Summary(t3.maxTauPerHour,
                                t3.bestPubMulti, t3.strategy.name, t3.bestPubTime, t3.bestTauGain,
                                t3.coastStart);
                    }
                }
                t3.printSummary(summary);

            }
        }

        else if (theoryNumber == 4) {
            strategies.add(new Strategy("T4SolC", "active"));
            strategies.add(new Strategy("T4Sol2", "active"));
            strategies.add(new Strategy("T4Solar", "active"));
            strategies.add(new Strategy("T4Gold", "active"));
            strategies.add(new Strategy("T4C3d", "active"));
            strategies.add(new Strategy("T4C3", "idle"));

            Theory4 t4 = new Theory4(pubMark);
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 3; i++) {
                    t4 = new Theory4(pubMark);
                    t4.strategy = strategy;

                    if (i > 0) {
                        t4.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
                    }

                    while (t4.finishCoasting == false) {
                        t4.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        t4.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || t4.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = t4.getSummary().pubMulti;
                        summary = new Summary(t4.maxTauPerHour,
                                t4.bestPubMulti, t4.strategy.name, t4.bestPubTime, t4.bestTauGain,
                                t4.coastStart);
                    }
                }
                t4.printSummary(summary);

            }
        } else if (theoryNumber == 10) {

            strategies.add(new Strategy("WSPlay2", "active"));
            strategies.add(new Strategy("WSPlay", "active"));
            strategies.add(new Strategy("WSPd", "active"));
            strategies.add(new Strategy("WSPStC1", "idle"));
            strategies.add(new Strategy("WSP", "idle"));

            WeierStrass weierStrass = new WeierStrass(pubMark);
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 5; i++) {
                    weierStrass = new WeierStrass(pubMark);
                    weierStrass.strategy = strategy;
                    if (i > 0) {
                        weierStrass.coastingPub = bestPubMulti - 0.10 * i * bestPubMulti;
                    }

                    while (weierStrass.finishCoasting == false) {
                        weierStrass.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        weierStrass.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || weierStrass.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = weierStrass.getSummary().pubMulti;
                        summary = new Summary(weierStrass.maxTauPerHour,
                                weierStrass.bestPubMulti, weierStrass.strategy.name, weierStrass.bestPubTime,
                                weierStrass.bestTauGain,
                                weierStrass.coastStart);
                    }
                }
                weierStrass.printSummary(summary);

            }
        } else if (theoryNumber == 11) {
            strategies.add(new Strategy("SLPlay2", "active"));
            strategies.add(new Strategy("SLPlay", "active"));
            strategies.add(new Strategy("SLst", "idle"));
            strategies.add(new Strategy("SLd", "active"));

            Sequential_Limit SL = new Sequential_Limit(pubMark);
            double bestPubMulti = 0;
            Summary summary = new Summary();
            for (Strategy strategy : strategies) {
                for (int i = 0; i < 5; i++) {
                    SL = new Sequential_Limit(pubMark);
                    SL.strategy = strategy;
                    if (i > 0) {
                        SL.coastingPub = bestPubMulti - 0.10 * i * bestPubMulti;
                    }

                    while (SL.finishCoasting == false) {
                        SL.runEpoch();
                    }
                    if (printHeaderCounter == 0) {
                        SL.printSummaryHeader();
                        printHeaderCounter = 1;
                    }
                    if (i == 0 || SL.maxTauPerHour > summary.tauPerHour) {
                        bestPubMulti = SL.getSummary().pubMulti;
                        summary = new Summary(SL.maxTauPerHour,
                                SL.bestPubMulti, SL.strategy.name, SL.bestPubTime, SL.bestTauGain,
                                SL.coastStart);
                    }
                }
                SL.printSummary(summary);

            }
        }
    }

    public static void runIndividualSim(int studentNumber, int theoryNumber, double pubMark) {
        double start = System.currentTimeMillis();

        int printHeaderCounter = 0;
        ArrayList<Strategy> strategies = new ArrayList<>();

        Theory.studentNumber = studentNumber;
        if (theoryNumber == 4) {
            strategies.add(new Strategy("T4SolC", "active"));
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
                if (t4.getSummary().tauPerHour > bestTauPerHour) {
                    summary = t4.getSummary();
                    bestTauPerHour = t4.getSummary().tauPerHour;
                }
                ;

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
                if (weierStrass.getSummary().tauPerHour > bestTauPerHour) {
                    summary = weierStrass.getSummary();
                    bestTauPerHour = weierStrass.getSummary().tauPerHour;
                }
                ;

            }
            weierStrass.printSummary(summary);
        } else if (theoryNumber == 11) {
            strategies.add(new Strategy("SLPlay2", "active"));
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

                if (SL.getSummary().tauPerHour > bestTauPerHour) {
                    summary = SL.getSummary();
                    bestTauPerHour = SL.getSummary().tauPerHour;
                }
                ;

            }
            SL.printSummary(summary);

            double finish = System.currentTimeMillis();

            // System.out.println("elapsed time: " + String.format("%.3f", (finish - start)
            // / 1000.0) + " seconds.");
        }
    }
}