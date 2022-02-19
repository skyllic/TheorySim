

public class Summary {

  public double tauPerHour;
  public double pubMulti;
  public String strategy;
  public double pubTime;
  public double tauGain;

  public Summary(double tauPerHour, double pubMulti, String strategy, double pubTime, double tauGain) {
    this.tauPerHour = tauPerHour;
    this.pubMulti = pubMulti;
    this.strategy = strategy;
    this.pubTime = pubTime;
    this.tauGain = tauGain;
  }

  public Summary() {
  }
  
}