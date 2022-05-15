package sim.theory;



public class TheoryFactory {

  public TheoryFactory() {

  }

  public static ITheory createTheory(int theoryNumber, double pubMark) {
    if(theoryNumber == 1) {
      return new Theory1(pubMark);
    } else if(theoryNumber == 2) {
      return new Theory2(pubMark);
    } else if(theoryNumber == 3) {
      return new Theory3(pubMark);
    } else if(theoryNumber == 4) {
      return new Theory4(pubMark);
    } else if(theoryNumber == 5) {
      return new Theory5(pubMark);
    } else if(theoryNumber == 6) {
      return new Theory6(pubMark);
    } else if(theoryNumber == 7) {
      return new Theory7(pubMark);
    } else if(theoryNumber == 8) {
      return new Theory8(pubMark);
    } else if(theoryNumber == 10) {
      return new WeierStrass(pubMark);
    } else if(theoryNumber == 11) {
      return new Sequential_Limit(pubMark);
    } else if(theoryNumber == 12) {
      return new Convergence_Square_Root(pubMark);
    } else if(theoryNumber == 13) {
      return new Basic(pubMark);
    } else if(theoryNumber == 14) {
      return new Euler_Formula(pubMark);
    } else {
      return null;
    }
    
  }
  
}
