/**A TheoryRunner class containing the main method used to run to theory simulator */
public class TheoryRunner {

    



    public static void main(String[] args) {
        Theory6 T6 = new Theory6(Math.log10(5.2) + 1023);
      
        double result = Variable.add(1, 1);
        System.out.println(result);
        System.out.println(T6.variables[0].value);
        result = Variable.add(-Double.MAX_VALUE, -0.8239087409443188);
        
        for(int i = 0; i < 1000; i++) {
            System.out.println(T6.rho + "\t" + T6.r + "\t" + T6.q + "\t" + T6.qdot + "\t" + T6.rdot);
            T6.moveTick();
            
        }
    }
}