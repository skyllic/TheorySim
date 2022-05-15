package sim;

import java.util.ArrayList;

import UI.Printer;
import sim.strategy.*;
import sim.theory.ITheory;
import sim.theory.Theory;
import sim.theory.Theory1;
import sim.theory.TheoryFactory;

/**
 * A class for Running different types of sim. E.g. individual theory sims,
 * combined sims etc.
 */
public class SimRunner {

  public static ArrayList<Strategy> strategies = new ArrayList<Strategy>();

  public SimRunner() {

    SimRunner.addAllKnownStrategies();
  }

  /** A collection of all known strategies the sim supports. */
  public static void addAllKnownStrategies() {
    SimRunner.strategies.add(new Strategy("T1Play", 1, "active"));
    SimRunner.strategies.add(new Strategy("T1Play2", 1, "active"));
    SimRunner.strategies.add(new Strategy("T1Baby", 1, "active"));
    SimRunner.strategies.add(new Strategy("T1C34", 1, "idle"));
    SimRunner.strategies.add(new Strategy("T1C4", 1, "idle"));

    SimRunner.strategies.add(new Strategy("T2AI", 2, "active"));
    SimRunner.strategies.add(new Strategy("T2MS", 2, "active"));
    SimRunner.strategies.add(new Strategy("T2NoMS", 2, "idle"));
    SimRunner.strategies.add(new Strategy("T2", 2, "idle"));

    SimRunner.strategies.add(new Strategy("T3Play2", 3, "active"));
    SimRunner.strategies.add(new Strategy("T3PlayX", 3, "active"));
    SimRunner.strategies.add(new Strategy("T3PlayI", 3, "idle"));
    SimRunner.strategies.add(new Strategy("T3XS", 3, "active"));
    SimRunner.strategies.add(new Strategy("T3OldI", 3, "idle"));

    SimRunner.strategies.add(new Strategy("T4PlaySpqcey", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4SolC", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Sol2", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Solar", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Gold", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4C3d", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4C3", 4, "idle"));
    SimRunner.strategies.add(new Strategy("T4Baby", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4NoMS", 4, "active"));

    SimRunner.strategies.add(new Strategy("T5Play", 5, "active"));
    SimRunner.strategies.add(new Strategy("T5", 5, "idle"));
    SimRunner.strategies.add(new Strategy("T5PlayI", 5, "idle"));

    SimRunner.strategies.add(new Strategy("T6Play", 6, "active"));
    SimRunner.strategies.add(new Strategy("T6C5d", 6, "active"));
    SimRunner.strategies.add(new Strategy("T6C125d", 6, "active"));
    SimRunner.strategies.add(new Strategy("T6C5", 6, "idle"));
    SimRunner.strategies.add(new Strategy("T6C125", 6, "idle"));
    SimRunner.strategies.add(new Strategy("T6Baby", 6, "active"));

    SimRunner.strategies.add(new Strategy("T7PlaySpqcey", 7, "active"));
    SimRunner.strategies.add(new Strategy("T7Play", 7, "active"));
    SimRunner.strategies.add(new Strategy("T7C456", 7, "idle"));

    SimRunner.strategies.add(new Strategy("T8MS2", 8, "active"));
    SimRunner.strategies.add(new Strategy("T8MS", 8, "active"));
    SimRunner.strategies.add(new Strategy("T8Play", 8, "active"));
    SimRunner.strategies.add(new Strategy("T8", 8, "idle"));
    SimRunner.strategies.add(new Strategy("T8Baby", 8, "active"));

    SimRunner.strategies.add(new Strategy("WSPlay2", 10, "active"));
    SimRunner.strategies.add(new Strategy("WSPlay", 10, "active"));
    SimRunner.strategies.add(new Strategy("WSPd", 10, "active"));
    SimRunner.strategies.add(new Strategy("WSPStC1", 10, "idle"));
    SimRunner.strategies.add(new Strategy("WSP", 10, "idle"));

    SimRunner.strategies.add(new Strategy("SLPlay2", 11, "active"));
    SimRunner.strategies.add(new Strategy("SLPlay", 11, "active"));
    SimRunner.strategies.add(new Strategy("SLst", 11, "idle"));
    SimRunner.strategies.add(new Strategy("SLd", 11, "active"));

    SimRunner.strategies.add(new Strategy("CSR2MS", 12, "active"));
    SimRunner.strategies.add(new Strategy("CSR2MST", 12, "active"));
    SimRunner.strategies.add(new Strategy("CSRPlay", 12, "active"));
    SimRunner.strategies.add(new Strategy("CSR2", 12, "idle"));
    SimRunner.strategies.add(new Strategy("CSR2d", 12, "active"));

    SimRunner.strategies.add(new Strategy("BTPlay", 13, "active"));
    SimRunner.strategies.add(new Strategy("BTd", 13, "active"));
    SimRunner.strategies.add(new Strategy("BT", 13, "idle"));

    SimRunner.strategies.add(new Strategy("EFBaby", 14,"active"));
    SimRunner.strategies.add(new Strategy("EF", 14,"idle"));
    SimRunner.strategies.add(new Strategy("EFd", 14,"active"));
    
  }

  /**
   * Simulates a theory from starting rho to end rho. E.g. if start rho is 400,
   * end rho is 1000, then
   * the method will start from e400 rho and publish at known optimal multipliers
   * until its rho reaches end rho.
   * </br>
   * If during a publication the rho reaches the end rho, the method will not stop
   * until the the theory reaches
   * optimal publication multiplier and publishes. E.g. if final rho is 1000, and
   * current theory rho is at
   * 997, with supposed optimal publication at 1003, the theory will continue
   * until it reaches 1003.
   * 
   * @param studentNumber - Number of students. Dynamically adjusts to the
   *                      appropriate level of Research 9.
   * @param theoryNumber  - Theory number. Custom theory numbers start at 10,
   *                      ordered by release dates.
   * @param startPub      - Starting rho in log format.
   * @param endPub        - Goal rho in log format.
   * @return - Total time required in hours.
   */
  public static double[] runChainSims(int studentNumber, int theoryNumber, double startPub, double endPub, String flag) {

    double totalTime = 0;
    double currentPub = startPub;
    ArrayList<Summary> summaries = new ArrayList<Summary>();
    while (currentPub < endPub) {
      if (flag.contains("print=true")) {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, true, flag);
      } else {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, false, flag);
      }

      currentPub = currentPub + summaries.get(0).tauGain;
      totalTime = totalTime + summaries.get(0).pubTime;
    }

    
    double normalisedTime = totalTime / (currentPub - startPub) * (endPub - startPub);
    double[] result = {totalTime, currentPub, normalisedTime};

    System.out.println(normalisedTime);

    return result;
  }

  public static void runDistributionComparison(double[] dist1, double[] dist2, int studentNumber1, int studentNumber2) {

    double[] totalTime1 = new double[8];
    double[] totalTime2 = new double[8];
    double sumTime1 = 0;
    double sumTime2 = 0;
    for(int i = 0; i < dist1.length; i++) {
      totalTime1[i] = SimRunner.runChainSims(studentNumber2, i+1, 0, dist1[i], "print=true,strategy=first")[0];
      totalTime2[i] = SimRunner.runChainSims(studentNumber1, i+1, 0, dist2[i], "print=true,strategy=first")[0];
    } 

    for(int i = 0; i < totalTime1.length; i++) {
      System.out.println(totalTime1[i] + "\t" + totalTime2[i]);
    }

    for(int i = 0; i < totalTime2.length; i++) {
      sumTime1 += totalTime1[i];
      sumTime2 += totalTime2[i];
    }

    System.out.println(sumTime1 - sumTime2);

    
  }

  public static void runIntervalSim(int studentNumber, int theoryNumber, double startPub, double endPub, double interval, String flag) {
    double totalTime = 0;
    double currentPub = startPub;
    ArrayList<Summary> summaries = new ArrayList<Summary>();
    ArrayList<Double> data = new ArrayList<Double>();
    while (currentPub < endPub) {
      if (flag.contains("print=true")) {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, true, flag);
      } else {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, false, flag);
      }

      currentPub = currentPub + interval;
      totalTime = totalTime + summaries.get(0).pubTime;

      if(flag.contains("taudot")) {
        data.add(summaries.get(0).tauPerHour);
      } else if(flag.contains("pubmulti")) {
        data.add(summaries.get(0).pubMulti);
      }
    }

    for(int i = 0; i < data.size(); i++) {
      System.out.println(data.get(i));
    }
    

    
  }

  public static void runStrategicSim(int studentNumber, int theoryNumber, double start, double end, String flag) {
    double bestK1 = 0;
    double bestK2 = 0;
    double bestK3 = 0;
    double bestK4 = 0;
    double bestK5 = 0;
    double bestK6 = 0;
    double bestK7 = 0;
    double bestK8 = 0;
  
    double bestTotalTime = 2222222220.0;
    
    for(double i = 10.0; i < 11; i = i + 0.05) {
      for(double j = 10.0; j < 11; j = j + 0.05) {
        Theory.K_value[0] = i;
        Theory.K_value[1] = j;
        double[] result = new double[2];
        
        double normalisedTime = SimRunner.runChainSims(studentNumber, theoryNumber, start, end, flag)[2];
        
        if(normalisedTime < bestTotalTime) {
          bestTotalTime = normalisedTime;
          bestK1 = Theory.K_value[0];
          bestK2 = Theory.K_value[1];
        }
      }
    }

    System.out.println(bestTotalTime);
    System.out.println(bestK1);
    System.out.println(bestK2);
  }

  /**
   * This method is used to run a specific strategy from one theory. It will not
   * run other strategies not specified.
   * If a non-existent strategy is specified, it will run sims for all known
   * strategies for that particular theory.
   */
  public static void runSpecificSim(int studentNumber, int theoryNumber, double pubMark, String flag) {
    if (flag.contains("print=false")) {
      runDetailedSim(studentNumber, theoryNumber, pubMark, false, flag);
    } else {
      runDetailedSim(studentNumber, theoryNumber, pubMark, true, flag);
    }

  }

  public static void appendStrategies(int theoryNumber, String flag) {
    for (int i = 0; i < SimRunner.strategies.size(); i++) {
      if (SimRunner.strategies.get(i).theoryNumber == theoryNumber) {
        if (flag.contains(SimRunner.strategies.get(i).name.toLowerCase())) {

        }
      }
    }
  }

  private static ArrayList<Strategy> addWorkingStrategies(int theoryNumber) {
    ArrayList<Strategy> workingStrategies = new ArrayList<>();
    for (int i = 0; i < SimRunner.strategies.size(); i++) {
      if (SimRunner.strategies.get(i).theoryNumber == theoryNumber) {
        if (SimRunner.strategies.get(i).name.toLowerCase().contains("baooy")) {

        } else {
          workingStrategies.add(SimRunner.strategies.get(i));
        }

      }

    }
    return workingStrategies;
  }

  private static ArrayList<Summary> runSims(int studentNumber, int theoryNumber, double pubMark, ArrayList<Strategy> strategies, String flag) {
    ArrayList<Summary> summmaryStrategies = new ArrayList<Summary>();
    ITheory theory = TheoryFactory.createTheory(theoryNumber, pubMark);

    
    
    ArrayList<Strategy> tempStrategy = new ArrayList<Strategy>();
    if(flag.contains("strategy=first")) {
      tempStrategy.add(strategies.get(0));
    } else if(flag.contains("strategy=second")) {
      tempStrategy.add(strategies.get(1));
    } else if(flag.contains("strategy=third")) {
      tempStrategy.add(strategies.get(2));
    }
    else if(flag.contains("strategy=fourth")) {
      tempStrategy.add(strategies.get(3));
    }else {
      tempStrategy = strategies;
    }
    Summary currentBestSummary = new Summary();

    for (Strategy strategy : tempStrategy) {
      double currentMaxTauPerHour = 0;
      for (int i = 0; i < 1; i++) {
        theory.setStrategy(strategy);
        theory.setStudent(studentNumber);
        theory.runUntilPublish();
        

        double tauPerHour = theory.getSummary().tauPerHour;
        if (tauPerHour > currentMaxTauPerHour) {
          currentMaxTauPerHour = tauPerHour;
          currentBestSummary = theory.getSummary();
        }

        theory = TheoryFactory.createTheory(theoryNumber, pubMark);
        if(theoryNumber < 9) {
          theory.setCoastingPubs(theoryNumber - 1, theory.getCoastingPub(theoryNumber - 1) * 0.85);
        } else {
          theory.setCoastingPubs(theoryNumber - 1, theory.getCoastingPub(theoryNumber - 1) * 0.85);
        }
        
      }
      summmaryStrategies.add(currentBestSummary);

    }

    Printer.printSummaries(summmaryStrategies);

    return summmaryStrategies;
  }

  public static ArrayList<Summary> runDetailedSim(int studentNumber, int theoryNumber, double pubMark,
      boolean print, String flag) {
    // double start = System.currentTimeMillis();

    int printHeaderCounter = 0;
    ArrayList<Strategy> workingStrategies = new ArrayList<>();
    ArrayList<Summary> summaries = new ArrayList<>();

    

    workingStrategies = SimRunner.addWorkingStrategies(theoryNumber);

    
    summaries = SimRunner.runSims(studentNumber, theoryNumber, pubMark, workingStrategies, flag);
    

    // double finish = System.currentTimeMillis();
    // double seconds = (finish - start) / 1000.0;
    // System.out.println("Elapsed time: " + seconds + " seconds.");
    workingStrategies = new ArrayList<Strategy>();
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

    if (flag == "all") {
      for (int i = 0; i < summaries.size(); i++) {
        printSummary(summaries.get(i));
      }
      return;
    } else if (flag == "best") {

      double idleBest = 0;
      double activeBest = 0;
      int currentTheoryNumber = 1;

      Summary bestSummaryActive = new Summary();
      Summary bestSummaryIdle = new Summary();

      for (Summary summary : summaries) {
        if (summary.strategyType == "active") {
          if (summary.tauPerHour > activeBest) {
            bestSummaryActive = summary;
          }
        } else if (summary.strategyType == "idle") {
          if (summary.tauPerHour > idleBest) {
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

      for (Summary summary : summaries) {
        switch (summary.theoryNumber) {
          case 1:
            summary1.add(summary);
            break;
          case 2:
            summary2.add(summary);
            break;
          case 3:
            summary3.add(summary);
            break;
          case 4:
            summary4.add(summary);
            break;
          case 5:
            summary5.add(summary);
            break;
          case 6:
            summary6.add(summary);
            break;
          case 7:
            summary7.add(summary);
            break;
          case 8:
            summary8.add(summary);
            break;
          case 10:
            summary10.add(summary);
            break;
          case 11:
            summary11.add(summary);
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

      for (ArrayList<Summary> summaryArray : summ) {

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
        ArrayList<Summary> tempSummaries = runDetailedSim(studentNumber, i + 1 + CTOffset, tauNumbers[i], false, "");
        summaries.addAll(tempSummaries);
      } else {
        ArrayList<Summary> tempSummaries = runDetailedSim(studentNumber, i + 1, tauNumbers[i], false, "");
        summaries.addAll(tempSummaries);
      }
    }

    printSummaries(summaries, "nice");
    return summaries;
  }

}