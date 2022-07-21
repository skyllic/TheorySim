package UI;

import java.util.ArrayList;

import sim.Summary;

/**A class to print outputs to the user. */
public class Printer {

  public static final void printSummary(Summary summary) {

    printSummaryHeader(summary);
    printSummaryBody(summary);
  }

  public static final void printSummaries(ArrayList<Summary> summaryList) {
    printSummaryHeader(summaryList.get(0));
    for(Summary summary : summaryList) {
      printSummaryBody(summary);
    }
  }

  private static void printSummaryBody(Summary summary) {
    System.out.print(String.format("%.2f",
                summary.tauPerHour));
        System.out.print("\t\t" + String.format("%.2f", summary.pubMulti) + "\t\t\t");
        if (summary.strategy.length() > 7) {
            System.out.print(String.format("%s", summary.strategy) + "\t\t");
        } else {
            System.out.print(String.format("%s", summary.strategy) + "\t\t\t");
        }

        System.out.print(String.format("%.2f", summary.pubTime));
        System.out.print("\t\t" + String.format("%.2f", summary.tauGain));
        //System.out.print("\t\t" + String.format("%.2f", summary.coastStart));
        //System.out.print("\t\t" + String.format("%.2f", summary.longestIdlePeriod / 3600.0));
        // System.out.print("\t\t" + String.format("%.1f", this.publicationMark));
        System.out.println("");
  }

  private static void printSummaryHeader(Summary summary) {
    System.out.println(summary.name + " at e" + String.format("%.1f", summary.publicationMark) + " rho" +
                ", " + summary.studentNumber + " students");
        System.out.print("Rho/hr\t\t" + "PubMulti\t\t" + "Strategy\t\t" + "PubTime\t\t" + "RhoGain\t\t\n"
                /**+ "CoastM\t\t" + "LongestIdle\n"*/);
  }



}