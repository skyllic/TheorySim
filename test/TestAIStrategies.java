package test;

import org.junit.Test;

import sim.*;






public class TestAIStrategies {

    
    @Test
    public void testT6AI() {
        Theory6 theory6 = new Theory6(1060);
        for(int i = 0; i < theory6.variables.length; i++) {
            while(theory6.variables[i].nextCost < theory6.publicationMark * 1.0) {
                theory6.variables[i].level += 1;
                theory6.variables[i].update();
            }
            
        }
        theory6.display();
        theory6.display();
        
    }
    

    

}