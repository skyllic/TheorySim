import static org.junit.Assert.assertEquals;

import org.junit.Test;








public class TheoryCloningTest {

    
    @Test
    public void testTheoryCloning() {
        Theory6 T6 = new Theory6(1020);
        Theory6 t6Clone = T6.cloneTheory6();

        t6Clone.q += 100;

        //checks that the fields of T6 and t6Clone are updated independently
        assertEquals(T6.q + 100, t6Clone.q, 0.001); 
        
    }

   

}