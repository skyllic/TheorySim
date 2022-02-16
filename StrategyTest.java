

import org.junit.Test;








public class StrategyTest {


    
    @Test
    public void testFindStrategyStrengthT1() {
        double pubMark = 600+Math.log10(1);
        Theory1 t1 = new Theory1(pubMark);

        t1.variables[2].deactivate();
        t1.variables[3].deactivate();
        
  
        while(t1.publicationMultiplier < 15000.0) {
            t1.runStrategyAILoop(15000.0);
        }
        System.out.println(t1.totalMultiplier);
    }
    


    @Test
    public void testFindStrategyStrengthT2() {
        double pubMark = 577+Math.log10(3.14);
        Theory2 t2 = new Theory2(pubMark);

      


  
        //best is 163.357 at pub multi 1485.8
        while(t2.publicationMultiplier < 15000.0) {
            t2.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT3() {
        double pubMark = 625+Math.log10(2.8);
        Theory3 t3 = new Theory3(pubMark);

     
        t3.variables[3].deactivate();
        t3.variables[5].deactivate();
        t3.variables[6].deactivate();
  
        while(t3.publicationMultiplier < 15000.0) {
            t3.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT4() {
        double pubMark = 740+Math.log10(1);
        Theory4 t4 = new Theory4(pubMark);

        
        t4.variables[1].deactivate();
        t4.variables[3].deactivate();
        t4.variables[4].deactivate();
        t4.variables[5].deactivate();

      


  
        //best is 163.357 at pub multi 1485.8
        while(t4.publicationMultiplier < 15000.0) {
            t4.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT6() {
        double pubMark = 1136+Math.log10(1);
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
      

        while(t6.publicationMultiplier < 20.0) {
            t6.runStrategyAILoop();
        }
    }

    @Test
    public void testFindStrategyStrengthT7() {
        double pubMark = 610+Math.log10(1);
        Theory7 t7 = new Theory7(pubMark);

        t7.variables[1].deactivate();
        t7.variables[2].deactivate();
        t7.variables[3].deactivate();
        /**t4.variables[1].deactivate();
        t4.variables[3].deactivate();
        t4.variables[4].deactivate();
        t4.variables[5].deactivate();*/

      


  
        //best is 163.357 at pub multi 1485.8
        while(t7.publicationMultiplier < 15000.0) {
            t7.runStrategyAILoop(15000.0);
        }
    }

  
    @Test
    public void testFindStrategyStrengthT8() {
        double pubMark = 600+Math.log10(1);
        Theory8 t8 = new Theory8(pubMark);

  
  
        while(t8.publicationMultiplier < 15000.0) {
            t8.runStrategyAILoop(15000.0);
        }
        System.out.println(t8.totalMultiplier);
    }


        @Test
    public void testFindStrategyStrengthWeierStrass() {
        double pubMark = 961+Math.log10(2.39);
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


       

        
       

        while(weierStrass.publicationMultiplier < 15000.0) {
            weierStrass.runStrategyAILoop(15000);
        }
      
        
    


    }

    @Test
    public void testFindStrategyStrengthSequentialLimit() {
       

      
        /**t4.variables[1].deactivate();
        t4.variables[3].deactivate();
        t4.variables[4].deactivate();
        t4.variables[5].deactivate();*/

        
        double pubMark = 835 + Math.log10(1);
        Sequential_Limit tSL = new Sequential_Limit(pubMark);

        while(tSL.publicationMultiplier < 15000.0) {
            tSL.runStrategyAILoop(15000.0);
        }
        


  
       
    }

 
    

}