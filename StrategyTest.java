import static org.junit.Assert.assertEquals;

import org.junit.Test;








public class StrategyTest {

    

    @Test
    public void testFindStrategyStrengthT6() {
        double pubMark = 1250+Math.log10(1);
        Theory6 t6 = new Theory6(pubMark);

        /** 
        t6.variables[0].level = 2320;
        t6.variables[1].level = 553;
        t6.variables[2].level = 217;
        t6.variables[3].level = 108;
        t6.variables[8].level = 1874;
        t6.rho = Math.log10(1.6) + 1100;
        t6.q = Math.log10(7.53) + 241;
        t6.r = Math.log10(1.132) + 42;*/


        t6.variables[6].deactivate();
        t6.variables[7].deactivate();

        
      

        while(t6.publicationMultiplier < 2000.0) {
            t6.runStrategyAILoop();
        }
    }

    @Test
    public void testFindStrategyStrengthT2() {
        double pubMark = 500+Math.log10(1);
        Theory2 t2 = new Theory2(pubMark);

      


  
        //best is 163.357 at pub multi 1485.8
        while(t2.publicationMultiplier < 15000.0) {
            t2.runStrategyAILoop(15000.0);
        }
    }

        @Test
    public void testFindStrategyStrengthWeierStrass() {
        double pubMark = 400+Math.log10(3);
        WeierStrass weierStrass = new WeierStrass(pubMark);

        /** 
        t6.variables[0].level = 2320;
        t6.variables[1].level = 553;
        t6.variables[2].level = 217;
        t6.variables[3].level = 108;
        t6.variables[8].level = 1874;
        t6.rho = Math.log10(1.6) + 1100;
        t6.q = Math.log10(7.53) + 241;
        t6.r = Math.log10(1.132) + 42;*/


       

        
        System.out.println("hi");

        while(weierStrass.publicationMultiplier < 20) {
            weierStrass.runStrategyAILoop();
        }
      
        
    


    }

 
    

}