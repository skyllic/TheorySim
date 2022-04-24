package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sim.upgrades.Variable;








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

        result = Variable.add(-Double.MAX_VALUE, -0.8239087409443188);
        assertEquals(Math.log10(1.5*0.1), result, 0.00001);



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

    @Test
    public void testVariableInitialisation() {
        Variable testVariable = new Variable(1000, 10000, 2, 1,
         true, true, false, false, false, new double[2]);
         testVariable.level = 249;
         testVariable.update();
         assertEquals(748, testVariable.cost, 0.01);
         assertEquals(751, testVariable.nextCost, 0.01);


         testVariable = new Variable(Math.pow(10, 0.649), 500, Math.pow(6.5, 6.5/4.0), 1,
          false, true, false, false, false, new double[] {6.5, 4});

          testVariable.level = 169;
          testVariable.update();

          double p = Variable.subtract(Math.log10(testVariable.stepwiseParam[1] + testVariable.level % testVariable.stepwiseParam[1]) + 
                            Math.log10(testVariable.stepwiseParam[0]) * Math.floor(testVariable.level / testVariable.stepwiseParam[1]), Math.log10(testVariable.stepwiseParam[1]));
        System.out.println(p);
          assertEquals(740, testVariable.value, 0.01);
    }

}