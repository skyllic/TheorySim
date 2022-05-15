package sim.theory;

import sim.Summary;
import sim.strategy.Strategy;

public interface ITheory {

  public void moveTick();

  public void runUntilPublish();

  public Summary getSummary();

  public void setStrategy(Strategy strategy);

  public void setStudent(int studentNumber);

  public void setCoastingPubs(int position, double value);
  
  public double getCoastingPub(int position);

}
