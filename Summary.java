

public class Summary {

  public double tauPerHour;
  public double pubMulti;
  public String strategy;
  public double pubTime;
  public double tauGain;
  public double coastStart;

  public Summary(double tauPerHour, double pubMulti, String strategy, double pubTime, double tauGain, double coastStart) {
    this.tauPerHour = tauPerHour;
    this.pubMulti = pubMulti;
    this.strategy = strategy;
    this.pubTime = pubTime;
    this.tauGain = tauGain;
    this.coastStart = coastStart;
  }

  public Summary() {
  }
  
}