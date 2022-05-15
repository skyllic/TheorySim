package sim.data;
import sim.theory.*;
import sim.upgrades.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sim.SimRunner;
import sim.Summary;
import sim.strategy.*;

public class DataGenerator {

  public static void main(String[] args) throws Exception {

    DataGenerator.createTable(300, 14, 1, 250, 0.1, "300sigma\\TheoryEFPlayRates.txt");



  }

  public static void createTable(int studentNumber, int theoryNumber, double start, double end, double interval, String fileName) throws IOException {
    ArrayList<ArrayList<Summary>> summaries = new ArrayList<ArrayList<Summary>>();

    double currentStart = start;

    SimRunner simRunner = new SimRunner();
    while(currentStart < end) {
      summaries.add(SimRunner.runDetailedSim(studentNumber, theoryNumber, currentStart, true, "strategy=first"));
      
      currentStart += interval;
    }


    DataGenerator.putSummariesInFile(summaries, "TheorySim\\sim\\data\\" + fileName);

    
  }

  public static void putSummariesInFile(ArrayList<ArrayList<Summary>> summaries, String fileName) throws IOException {

    File file = new File(fileName);
    if(!file.exists()) {
      file.createNewFile();
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
    for(ArrayList<Summary> summary : summaries) {
      double tauPerHour = summary.get(0).tauPerHour;
      double tau = summary.get(0).publicationMark;
      double tauGain = summary.get(0).tauGain;

      writer.write(String.format("%.1f", tau) + "\t" + String.format("%.5f", tauPerHour)
      + "\t" + String.format("%.3f", tauGain) + "\n");
    }

    writer.close();
  }
  
}
