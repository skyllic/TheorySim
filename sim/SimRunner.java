package sim;
import java.util.ArrayList;

public class SimRunner {

  public SimRunner() {

  }

  public static ArrayList<Summary> runDetailedSim(int studentNumber, int theoryNumber, double pubMark,
      boolean print) {
    //double start = System.currentTimeMillis();

    int printHeaderCounter = 0;
    ArrayList<Strategy> strategies = new ArrayList<>();
    ArrayList<Summary> summaries = new ArrayList<>();

    Theory.studentNumber = studentNumber;

    if (theoryNumber == 1) {
      strategies.add(new Strategy("T1Play", "active"));
      strategies.add(new Strategy("T1Play2", "active"));
      strategies.add(new Strategy("T1C34", "idle"));
      strategies.add(new Strategy("T1C4", "idle"));

      Theory1 t1 = new Theory1(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 10; i++) {
          t1 = new Theory1(pubMark);
          t1.strategy = strategy;

          if (i > 0) {
            t1.coastingPub = bestPubMulti - 0.09 * i * bestPubMulti;
          }

          while (t1.finishCoasting == false) {
            t1.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t1.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t1.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t1.getSummary().pubMulti;
            summary = new Summary(1, t1.maxTauPerHour,
                t1.bestPubMulti, t1.strategy.name, t1.strategy.type, t1.bestPubTime, t1.bestTauGain,
                t1.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t1.printSummary(summary);
        }
      }
    } else if (theoryNumber == 2) {
      strategies.add(new Strategy("T2Coast", "idle"));
      strategies.add(new Strategy("T2MS", "active"));

      Theory2 t2 = new Theory2(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 6; i++) {
          t2 = new Theory2(pubMark);
          t2.strategy = strategy;

          if (i > 0) {
            t2.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t2.finishCoasting == false) {
            t2.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t2.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t2.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t2.getSummary().pubMulti;
            summary = new Summary(2, t2.maxTauPerHour,
                t2.bestPubMulti, t2.strategy.name, t2.strategy.type, t2.bestPubTime, t2.bestTauGain,
                t2.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t2.printSummary(summary);
        }
      }
    } else if (theoryNumber == 3) {
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
          if (printHeaderCounter == 0 && print == true) {
            t3.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t3.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t3.getSummary().pubMulti;
            summary = new Summary(3, t3.maxTauPerHour,
                t3.bestPubMulti, t3.strategy.name, t3.strategy.type, t3.bestPubTime, t3.bestTauGain,
                t3.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t3.printSummary(summary);
        }
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
          if (printHeaderCounter == 0 && print == true) {
            t4.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t4.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t4.getSummary().pubMulti;
            summary = new Summary(4, t4.maxTauPerHour,
                t4.bestPubMulti, t4.strategy.name, t4.strategy.type, t4.bestPubTime, t4.bestTauGain,
                t4.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t4.printSummary(summary);
        }
      }

    } else if (theoryNumber == 5) {
      strategies.add(new Strategy("T5Play", "active"));
      strategies.add(new Strategy("T5", "idle"));
      strategies.add(new Strategy("T5PlayI", "idle"));

      Theory5 t5 = new Theory5(pubMark);
      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t5 = new Theory5(pubMark);
          t5.strategy = strategy;

          if (i > 0) {
            t5.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t5.finishCoasting == false) {
            t5.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t5.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t5.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t5.getSummary().pubMulti;
            summary = new Summary(5, t5.maxTauPerHour,
                t5.bestPubMulti, t5.strategy.name, t5.strategy.type, t5.bestPubTime, t5.bestTauGain,
                t5.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t5.printSummary(summary);
        }
      }

    } else if (theoryNumber == 6) {
      strategies.add(new Strategy("T6Play", "active"));
      strategies.add(new Strategy("T6C5d", "active"));
      strategies.add(new Strategy("T6C125d", "active"));
      strategies.add(new Strategy("T6C5", "idle"));
      strategies.add(new Strategy("T6C125", "idle"));

      Theory6 t6 = new Theory6(pubMark);
      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t6 = new Theory6(pubMark);
          t6.strategy = strategy;

          if (i > 0) {
            t6.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t6.finishCoasting == false) {
            t6.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t6.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t6.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t6.getSummary().pubMulti;
            summary = new Summary(6, t6.maxTauPerHour,
                t6.bestPubMulti, t6.strategy.name, t6.strategy.type, t6.bestPubTime, t6.bestTauGain,
                t6.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t6.printSummary(summary);
        }
      }
    } else if (theoryNumber == 7) {
      strategies.add(new Strategy("T7Play", "active"));
      strategies.add(new Strategy("T7C456", "idle"));

      Theory7 t7 = new Theory7(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t7 = new Theory7(pubMark);
          t7.strategy = strategy;

          if (i > 0) {
            t7.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t7.finishCoasting == false) {
            t7.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t7.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t7.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t7.getSummary().pubMulti;
            summary = new Summary(7, t7.maxTauPerHour,
                t7.bestPubMulti, t7.strategy.name, t7.strategy.type, t7.bestPubTime, t7.bestTauGain,
                t7.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t7.printSummary(summary);
        }

      }
    } else if (theoryNumber == 8) {
      strategies.add(new Strategy("T8Play", "active"));
      strategies.add(new Strategy("T8MS", "active"));
      strategies.add(new Strategy("T8", "idle"));

      Theory8 t8 = new Theory8(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t8 = new Theory8(pubMark);
          t8.strategy = strategy;

          if (i > 0) {
            t8.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t8.finishCoasting == false) {
            t8.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            t8.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t8.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t8.getSummary().pubMulti;
            summary = new Summary(8, t8.maxTauPerHour,
                t8.bestPubMulti, t8.strategy.name, t8.strategy.type, t8.bestPubTime, t8.bestTauGain,
                t8.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          t8.printSummary(summary);
        }

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
          if (printHeaderCounter == 0 && print == true) {
            weierStrass.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || weierStrass.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = weierStrass.getSummary().pubMulti;
            summary = new Summary(10, weierStrass.maxTauPerHour,
                weierStrass.bestPubMulti, weierStrass.strategy.name, weierStrass.strategy.type,
                 weierStrass.bestPubTime,
                weierStrass.bestTauGain,
                weierStrass.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          weierStrass.printSummary(summary);
        }

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
          if (printHeaderCounter == 0 && print == true) {
            SL.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || SL.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = SL.getSummary().pubMulti;
            summary = new Summary(11, SL.maxTauPerHour,
                SL.bestPubMulti, SL.strategy.name, SL.strategy.type, SL.bestPubTime, SL.bestTauGain,
                SL.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          SL.printSummary(summary);
        }
      }
    } else if(theoryNumber == 12) {
      strategies.add(new Strategy("CSRPlay", "active"));
      strategies.add(new Strategy("CSR2d", "active"));
      strategies.add(new Strategy("CSR2", "idle"));
      
      

      Convergence_Square_Root CSR2 = new Convergence_Square_Root(pubMark);
      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          CSR2 = new Convergence_Square_Root(pubMark);
          CSR2.strategy = strategy;
          if (i > 0) {
            CSR2.coastingPub = bestPubMulti - 0.10 * i * bestPubMulti;
          }

          while (CSR2.finishCoasting == false) {
            CSR2.runEpoch();
          }
          if (printHeaderCounter == 0 && print == true) {
            CSR2.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || CSR2.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = CSR2.getSummary().pubMulti;
            summary = new Summary(11, CSR2.maxTauPerHour,
                CSR2.bestPubMulti, CSR2.strategy.name, CSR2.strategy.type, CSR2.bestPubTime, CSR2.bestTauGain,
                CSR2.coastStart);
          }
        }
        summaries.add(summary);
        if (print == true) {
          CSR2.printSummary(summary);
        }
      }
    }

    //double finish = System.currentTimeMillis();
    //double seconds = (finish - start) / 1000.0;
    // System.out.println("Elapsed time: " + seconds + " seconds.");
    return summaries;
  }

  public static void printSummaryHeader() {
    System.out.print("Tau/d\t\t" + "PubMulti\t\t" + "Strategy\t\t" + "PubTime\t\t" + "TauGain\t\t"
        + "CoastM\n");
  }

  public static void printSummary(Summary summary) {

    System.out.print(String.format("%.4f",
        summary.tauPerHour * 24.0));
    System.out.print("\t\t" + String.format("%.2f", summary.pubMulti) + "\t\t\t");
    System.out.print(String.format("%s", summary.strategy) + "\t\t\t");
    System.out.print(String.format("%.3f", summary.pubTime));
    System.out.print("\t\t" + String.format("%.4f", summary.tauGain));
    System.out.print("\t\t" + String.format("%.4f", summary.coastStart));
    // System.out.print("\t\t" + String.format("%.1f", this.publicationMark));
    System.out.println("");

  }

public static void printSummaries(ArrayList<Summary> summaries, String flag) {

    printSummaryHeader();

    if(flag == "all") {
    for(int i = 0; i< summaries.size(); i++) {
        printSummary(summaries.get(i));
    }
    return;
  } else if(flag == "best") {




    double idleBest = 0;
    double activeBest = 0;
    int currentTheoryNumber = 1;

    Summary bestSummaryActive = new Summary();
    Summary bestSummaryIdle = new Summary();

    for(Summary summary : summaries) {
      if(summary.strategyType == "active") {
        if(summary.tauPerHour > activeBest) {
          bestSummaryActive = summary;
        }
      } else if(summary.strategyType == "idle") {
        if(summary.tauPerHour > idleBest) {
          bestSummaryIdle = summary;
        }
      }

      printSummary(bestSummaryActive);
      printSummary(bestSummaryIdle);

    }




    int i = 0;
    ArrayList<Summary> summary1 = new ArrayList<>();
    ArrayList<Summary> summary2 = new ArrayList<>();
    ArrayList<Summary> summary3 = new ArrayList<>();
    ArrayList<Summary> summary4 = new ArrayList<>();
    ArrayList<Summary> summary5 = new ArrayList<>();
    ArrayList<Summary> summary6 = new ArrayList<>();
    ArrayList<Summary> summary7 = new ArrayList<>();
    ArrayList<Summary> summary8 = new ArrayList<>();
    ArrayList<Summary> summary10 = new ArrayList<>();
    ArrayList<Summary> summary11 = new ArrayList<>();

    for(Summary summary : summaries) {
      switch(summary.theoryNumber) {
        case 1 : summary1.add(summary);
        break;
        case 2 : summary2.add(summary);
        break;
        case 3 : summary3.add(summary);
        break;
        case 4 : summary4.add(summary);
        break;
        case 5 : summary5.add(summary);
        break;
        case 6 : summary6.add(summary);
        break;
        case 7 : summary7.add(summary);
        break;
        case 8 : summary8.add(summary);
        break;
        case 10 : summary10.add(summary);
        break;
        case 11 : summary11.add(summary);
        break;
      }
    }

    ArrayList<ArrayList<Summary>> summ = new ArrayList<ArrayList<Summary>>();
    summ.add(summary1);
    summ.add(summary2);
    summ.add(summary3);
    summ.add(summary4);
    summ.add(summary5);
    summ.add(summary6);
    summ.add(summary7);
    summ.add(summary8);
    summ.add(summary10);
    summ.add(summary11);

    for(ArrayList<Summary> summaryArray : summ) {

    }


  }
}

  public static ArrayList<Summary> runAllSim(int studentNumber, String t1Tau, String t2Tau, String t3Tau, String t4Tau,
      String t5Tau, String t6Tau, String t7Tau, String t8Tau, String WSPTau, String SLTau) {

    ArrayList<Summary> summaries = new ArrayList<>();

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

    for (int i = 0; i < tauNumbers.length; i++) {
      int CTOffset = 1;
      if (i > 7) {
        ArrayList<Summary> tempSummaries = runDetailedSim(studentNumber, i + 1 + CTOffset, tauNumbers[i], false);
        summaries.addAll(tempSummaries);
      } else {
        ArrayList<Summary> tempSummaries = runDetailedSim(studentNumber, i + 1, tauNumbers[i], false);
        summaries.addAll(tempSummaries);
      }
    }

    printSummaries(summaries, "nice");
    return summaries;
  }

  public static void runIndividualSim(int studentNumber, int theoryNumber, double pubMark,
      boolean print) {
    double start = System.currentTimeMillis();

    int printHeaderCounter = 0;
    ArrayList<Strategy> strategies = new ArrayList<>();

    Theory.studentNumber = studentNumber;

    if (theoryNumber == 1) {
      strategies.add(new Strategy("T1Play", "active"));
      strategies.add(new Strategy("T1Play2", "active"));
      strategies.add(new Strategy("T1C34", "idle"));
      strategies.add(new Strategy("T1C4", "idle"));

      Theory1 t1 = new Theory1(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t1 = new Theory1(pubMark);
          t1.strategy = strategy;

          if (i > 0) {
            t1.coastingPub = bestPubMulti - 0.17 * i * bestPubMulti;
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
            summary = new Summary(1, t1.maxTauPerHour,
                t1.bestPubMulti, t1.strategy.name, t1.strategy.type, t1.bestPubTime, t1.bestTauGain,
                t1.coastStart);
          }
        }
        t1.printSummary(summary);

      }
    } else if (theoryNumber == 2) {
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
            summary = new Summary(2, t2.maxTauPerHour,
                t2.bestPubMulti, t2.strategy.name, t2.strategy.type, t2.bestPubTime, t2.bestTauGain,
                t2.coastStart);
          }
        }
        t2.printSummary(summary);

      }
    } else if (theoryNumber == 3) {
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
            summary = new Summary(3, t3.maxTauPerHour,
                t3.bestPubMulti, t3.strategy.name, t3.strategy.type, t3.bestPubTime, t3.bestTauGain,
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
            summary = new Summary(4, t4.maxTauPerHour,
                t4.bestPubMulti, t4.strategy.name, t4.strategy.type, t4.bestPubTime, t4.bestTauGain,
                t4.coastStart);
          }
        }
        t4.printSummary(summary);
      }

    } else if (theoryNumber == 5) {
      strategies.add(new Strategy("T5Play", "active"));
      strategies.add(new Strategy("T5", "idle"));

      Theory5 t5 = new Theory5(pubMark);
      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t5 = new Theory5(pubMark);
          t5.strategy = strategy;

          if (i > 0) {
            t5.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t5.finishCoasting == false) {
            t5.runEpoch();
          }
          if (printHeaderCounter == 0) {
            t5.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t5.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t5.getSummary().pubMulti;
            summary = new Summary(5, t5.maxTauPerHour,
                t5.bestPubMulti, t5.strategy.name, t5.strategy.type, t5.bestPubTime, t5.bestTauGain,
                t5.coastStart);
          }
        }
        t5.printSummary(summary);
      }

    } else if (theoryNumber == 6) {
      strategies.add(new Strategy("T6Play", "active"));
      strategies.add(new Strategy("T6C5", "idle"));
      strategies.add(new Strategy("T6C125", "idle"));

      Theory6 t6 = new Theory6(pubMark);
      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t6 = new Theory6(pubMark);
          t6.strategy = strategy;

          if (i > 0) {
            t6.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t6.finishCoasting == false) {
            t6.runEpoch();
          }
          if (printHeaderCounter == 0) {
            t6.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t6.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t6.getSummary().pubMulti;
            summary = new Summary(6, t6.maxTauPerHour,
                t6.bestPubMulti, t6.strategy.name, t6.strategy.type, t6.bestPubTime, t6.bestTauGain,
                t6.coastStart);
          }
        }
        t6.printSummary(summary);
      }
    } else if (theoryNumber == 7) {
      strategies.add(new Strategy("T7Play", "active"));
      strategies.add(new Strategy("T7C456", "idle"));

      Theory7 t7 = new Theory7(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t7 = new Theory7(pubMark);
          t7.strategy = strategy;

          if (i > 0) {
            t7.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t7.finishCoasting == false) {
            t7.runEpoch();
          }
          if (printHeaderCounter == 0) {
            t7.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t7.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t7.getSummary().pubMulti;
            summary = new Summary(7, t7.maxTauPerHour,
                t7.bestPubMulti, t7.strategy.name, t7.strategy.type, t7.bestPubTime, t7.bestTauGain,
                t7.coastStart);
          }
        }
        t7.printSummary(summary);

      }
    } else if (theoryNumber == 8) {
      strategies.add(new Strategy("T8Play", "active"));
      strategies.add(new Strategy("T8", "idle"));

      Theory8 t8 = new Theory8(pubMark);

      double bestPubMulti = 0;
      Summary summary = new Summary();
      for (Strategy strategy : strategies) {
        for (int i = 0; i < 5; i++) {
          t8 = new Theory8(pubMark);
          t8.strategy = strategy;

          if (i > 0) {
            t8.coastingPub = bestPubMulti - 0.15 * i * bestPubMulti;
          }

          while (t8.finishCoasting == false) {
            t8.runEpoch();
          }
          if (printHeaderCounter == 0) {
            t8.printSummaryHeader();
            printHeaderCounter = 1;
          }
          if (i == 0 || t8.maxTauPerHour > summary.tauPerHour) {
            bestPubMulti = t8.getSummary().pubMulti;
            summary = new Summary(8, t8.maxTauPerHour,
                t8.bestPubMulti, t8.strategy.name, t8.strategy.type, t8.bestPubTime, t8.bestTauGain,
                t8.coastStart);
          }
        }
        t8.printSummary(summary);

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
            summary = new Summary(10, weierStrass.maxTauPerHour,
                weierStrass.bestPubMulti, weierStrass.strategy.name, weierStrass.strategy.type,
                weierStrass.bestPubTime,
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
            summary = new Summary(11, SL.maxTauPerHour,
                SL.bestPubMulti, SL.strategy.name, SL.strategy.type, 
                SL.bestPubTime, SL.bestTauGain,
                SL.coastStart);
          }
        }
        SL.printSummary(summary);

      }
    }

    double finish = System.currentTimeMillis();
    double seconds = (finish - start) / 1000.0;
    System.out.println("Elapsed time: " + seconds + " seconds.");
  }

}