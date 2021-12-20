import static org.junit.Assert.assertEquals;

import org.junit.Test;








public class VariableTest {

    

    @Test
    public void testVariableAdd() {
        double result = Variable.add(20, 20);
        assertEquals(20.30103, result, 0.001);

        result = Variable.add(20, 0);
        assertEquals(20, result, 0.001);

        result = Variable.add(50, 40);
        assertEquals(50, result, 0.001);

        result = Variable.add(10000, 9999);
        assertEquals(10000.041392685, result, 0.001);

        result = Variable.add(400, 395);
        assertEquals(400.00000434, result, 0.00001);

    }

    @Test
    public void testVariableSubtract() {
        double result = Variable.subtract(20, 20);
        assertEquals(-Double.MAX_VALUE, result, 0.001);

        result = Variable.subtract(20, 0);
        assertEquals(20, result, 0.001);

        result = Variable.subtract(50, 40);
        assertEquals(50, result, 0.001);

        result = Variable.subtract(10000, 9999);
        assertEquals(9999.95424251, result, 0.001);

        result = Variable.subtract(400, 395);
        assertEquals(399.9999957, result, 0.00001);

    }

}