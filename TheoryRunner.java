public class TheoryRunner {

    



    public static void main(String[] args) {
        Theory6 T6 = new Theory6(Math.log10(5.2) + 1023);
      
        
        for(int i = 0; i < 100; i++) {
            System.out.println(T6.rho + "\t" + T6.variables[0].value + "\t" + T6.q);
            T6.moveTick();
            
        }
    }
}