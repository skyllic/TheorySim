

public class Summary {

  public int theoryNumber;
  public double tauPerHour;
  public double pubMulti;
  public String strategy;
  public String strategyType;
  public double pubTime;
  public double tauGain;
  public double coastStart;

  public Summary(int theoryNumber, double tauPerHour, double pubMulti, String strategy, String strategyType, 
  double pubTime, double tauGain, double coastStart) {
    this.theoryNumber = theoryNumber;
    this.tauPerHour = tauPerHour;
    this.pubMulti = pubMulti;
    this.strategy = strategy;
    this.strategyType = strategyType;
    this.pubTime = pubTime;
    this.tauGain = tauGain;
    this.coastStart = coastStart;
  }

  public Summary() {
  }
  
}