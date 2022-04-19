package sim;

import java.util.ArrayList;

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

  public Variable[] variables;

  public Summary(int theoryNumber, double tauPerHour, double pubMulti, String strategy, String strategyType, 
  double pubTime, double recoveryTime, double tauGain, double coastStart, Variable[] variables, 
    double longestIdlePeriod) {
    this.theoryNumber = theoryNumber;
    this.tauPerHour = tauPerHour;
    this.pubMulti = pubMulti;
    this.strategy = strategy;
    this.strategyType = strategyType;
    this.pubTime = pubTime;
    this.recoveryTime = recoveryTime;
    this.tauGain = tauGain;
    this.coastStart = coastStart;

    this.variables = variables;
    this.longestIdlePeriod = longestIdlePeriod;
  }

  public Summary(String flags) {
    if(flags.contains("")) {

    }
  }

  public Summary() {
    
  }
  
}