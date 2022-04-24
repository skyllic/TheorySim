package sim;

import java.util.ArrayList;

import UI.Printer;
import sim.strategy.*;
import sim.theory.ITheory;
import sim.theory.Theory1;
import sim.theory.TheoryFactory;

/**A class for Running different types of sim. E.g. individual theory sims, combined sims etc.*/
public class SimRunner {

  public static ArrayList<Strategy> strategies = new ArrayList<Strategy>();
 

  public SimRunner() {

    SimRunner.addAllKnownStrategies();
  }

  /**A collection of all known strategies the sim supports. */
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

    SimRunner.strategies.add(new Strategy("T4PlaySpqcey", 4,  "active"));
    SimRunner.strategies.add(new Strategy("T4SolC", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Sol2", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Solar", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4Gold", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4C3d", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4C3",4,  "idle"));
    SimRunner.strategies.add(new Strategy("T4Baby", 4, "active"));
    SimRunner.strategies.add(new Strategy("T4NoMS", 4, "active"));

    SimRunner.strategies.add(new Strategy("T5Play", 5, "active"));
    SimRunner.strategies.add(new Strategy("T5", 5, "idle"));
    SimRunner.strategies.add(new Strategy("T5PlayI", 5, "idle"));
    
    SimRunner.strategies.add(new Strategy("T6Play", 6,  "active"));
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
    SimRunner.strategies.add(new Strategy("WSPd", 10,  "active"));
    SimRunner.strategies.add(new Strategy("WSPStC1", 10, "idle"));
    SimRunner.strategies.add(new Strategy("WSP", 10, "idle"));

    SimRunner.strategies.add(new Strategy("SLPlay2", 11, "active"));
    SimRunner.strategies.add(new Strategy("SLPlay", 11, "active"));
    SimRunner.strategies.add(new Strategy("SLst", 11, "idle"));
    SimRunner.strategies.add(new Strategy("SLd", 11, "active"));

    SimRunner.strategies.add(new Strategy("CSRPlay", 12, "active"));
    SimRunner.strategies.add(new Strategy("CSR2d", 12, "active"));
    SimRunner.strategies.add(new Strategy("CSR2", 12, "idle"));

    SimRunner.strategies.add(new Strategy("BTPlay", 13, "active"));
    SimRunner.strategies.add(new Strategy("BTd", 13, "active"));
    SimRunner.strategies.add(new Strategy("BT", 13,  "idle"));
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
  public static double runChainSims(int studentNumber, int theoryNumber, double startPub, double endPub, String flag) {

    double totalTime = 0;
    double currentPub = startPub;
    ArrayList<Summary> summaries;
    while (currentPub < endPub) {
      if(flag.contains("print=true")) {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, true, flag);
      } else {
        summaries = runDetailedSim(studentNumber, theoryNumber, currentPub, false, flag);
      }
      
      currentPub = currentPub + summaries.get(0).tauGain;
      totalTime = totalTime + summaries.get(0).pubTime;
    }

    System.out.println(totalTime);
    
    return totalTime;
  }

  /**This method is used to run a specific strategy from one theory. It will not run other strategies not specified.
   * If a non-existent strategy is specified, it will run sims for all known strategies for that particular theory.
   */
  public static void runSpecificSim(int studentNumber, int theoryNumber, double pubMark, String flag) {
    if(flag.contains("print=false")) {
      runDetailedSim(studentNumber, theoryNumber, pubMark, false, flag);
    } else {
      runDetailedSim(studentNumber, theoryNumber, pubMark, true, flag);
    }
    
  }

  public static void appendStrategies(int theoryNumber, String flag) {
    for(int i = 0; i < SimRunner.strategies.size(); i++) {
      if(SimRunner.strategies.get(i).theoryNumber == theoryNumber) {
        if(flag.contains(SimRunner.strategies.get(i).name.toLowerCase())) {
          
        }
      }
    }
  }

  private static ArrayList<Strategy> addWorkingStrategies(int theoryNumber) {
    ArrayList<Strategy> workingStrategies = new ArrayList<>();
    for(int i = 0; i < SimRunner.strategies.size(); i++) {
      if(SimRunner.strategies.get(i).theoryNumber == theoryNumber) {
        workingStrategies.add(SimRunner.strategies.get(i));
      }
      
    }
    return workingStrategies;
  }

  private static ITheory runSims(int theoryNumber, double pubMark, ArrayList<Strategy> strategies) {
    ArrayList<Summary> summmaryStrategies = new ArrayList<Summary>();
    ITheory theory = TheoryFactory.createTheory(theoryNumber, pubMark);
    for(Strategy strategy : strategies) {
      
      theory.setStrategy(strategy);
      theory.runUntilPublish();
      summmaryStrategies.add(theory.getSummary());
      theory = TheoryFactory.createTheory(theoryNumber, pubMark);
      
    }

    Printer.printSummaries(summmaryStrategies);
  
    

    return theory;
  }

  public static ArrayList<Summary> runDetailedSim(int studentNumber, int theoryNumber, double pubMark,
      boolean print, String flag) {
    // double start = System.currentTimeMillis();

    int printHeaderCounter = 0;
    ArrayList<Strategy> workingStrategies = new ArrayList<>();
    ArrayList<Summary> summaries = new ArrayList<>();

    addAllKnownStrategies();

    

    workingStrategies = SimRunner.addWorkingStrategies(theoryNumber);
    
    ITheory theory = TheoryFactory.createTheory(theoryNumber, pubMark);
    theory = SimRunner.runSims(theory, workingStrategies);
    
  


    // double finish = System.currentTimeMillis();
    // double seconds = (finish - start) / 1000.0;
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