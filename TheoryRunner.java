/**A TheoryRunner class containing the main method used to run to theory simulator */
public class TheoryRunner {

    



    public static void main(String[] args) {
        Theory6 T6 = new Theory6(Math.log10(1) + 1064);
      
        double result = Variable.add(1, 1);
        System.out.println(result);
        System.out.println(T6.variables[0].value);
        result = Variable.add(-Double.MAX_VALUE, -0.8239087409443188);
        T6.variables[3].level += 1;
        T6.variables[3].update();
        T6.variables[3].displayInScientific("cost");

        //T6.runStrategy("T6c5", "idle");
        for(int i = 0; i < 10000; i++) {
            T6.runStrategyAI();
        }
        

        
        
        /**for(int i = 0; i < 1000; i++) {
            System.out.println(T6.rho + "\t" + T6.r + "\t" + T6.q + "\t" + T6.qdot + "\t" + T6.rdot);
            T6.moveTick();
            
        }*/
    }
}