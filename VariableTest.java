import static org.junit.Assert.assertEquals;

import org.junit.Test;








public class VariableTest {

    

    @Test
    public void testVariable() {
        double result = Variable.add(20, 20);
        assertEquals(20.30103, result, 0.01);
    
    }

}