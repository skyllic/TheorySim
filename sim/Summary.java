package sim;

import java.util.ArrayList;
import sim.upgrades.Variable;

public class Summary {

  public int theoryNumber;
  public double tauPerHour;
  public double pubMulti;
  public String strategy;
  public String strategyType;
  public double pubTime;
  public double recoveryTime;
  public double tauGain;
  public double coastStart;
  public double longestIdlePeriod;
  public double longTauGain;

  public String name;
  public double publicationMark;
  public int studentNumber;

  public Variable[] variables;

  public Summary(int theoryNumber, double tauPerHour, double pubMulti, String strategy, String strategyType, 
  double pubTime, double recoveryTime, double tauGain, double coastStart, Variable[] variables, 
    double longestIdlePeriod, String name, double publicationMark, int studentNumber, double longTauGain) {

    if(longestIdlePeriod / 3600.0 > pubTime) {
      this.longestIdlePeriod = pubTime * 3600.0;
    } else {
      this.longestIdlePeriod = longestIdlePeriod;
    }

    this.theoryNumber = theoryNumber;
    this.tauPerHour = tauPerHour;
    this.pubMulti = pubMulti;
    this.strategy = strategy;
    this.strategyType = strategyType;
    this.pubTime = pubTime;
    this.recoveryTime = recoveryTime;
    this.tauGain = tauGain;
    this.coastStart = coastStart;
    this.name = name;
    this.publicationMark = publicationMark;
    this.studentNumber = studentNumber;

    this.variables = variables;
    this.longTauGain = longTauGain;
    
  }

  public Summary(String flags) {
    if(flags.contains("theory=")) {

    }
  }

  public Summary() {

  }
  
}