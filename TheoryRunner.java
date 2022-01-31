/**A TheoryRunner class containing the main method used to run to theory simulator */
public class TheoryRunner {

    



    public static void main(String[] args) {
        Theory6 T6 = new Theory6(Math.log10(1) + 1064);
      
        
    

        //T6.runStrategy("T6c5", "idle");
        T6.runStrategyAILoop();
       
        

        
        
        /**for(int i = 0; i < 1000; i++) {
            System.out.println(T6.rho + "\t" + T6.r + "\t" + T6.q + "\t" + T6.qdot + "\t" + T6.rdot);
            T6.moveTick();
            
        }*/
    }
}