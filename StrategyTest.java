
import java.util.ArrayList;
import java.util.Timer;

import org.junit.Test;

public class StrategyTest {

    @Test
    public void testFindStrategyStrengthT1() {
        double pubMark = 600 + Math.log10(1);
        Theory1 t1 = new Theory1(pubMark);

        t1.variables[2].deactivate();
        t1.variables[3].deactivate();

        while (t1.publicationMultiplier < 15000.0) {
            t1.runEpoch();
        }
        System.out.println(t1.totalMultiplier);
    }

    @Test
    public void testFindStrategyStrengthT2() {
        double pubMark = 1500 + Math.log10(1);
        Theory2 t2 = new Theory2(pubMark);

        double start = System.currentTimeMillis();
        // best is 163.357 at pub multi 1485.8
        while (t2.publicationMultiplier < 15000.0) {
            t2.runEpoch();
        }
        double finish = System.currentTimeMillis();
        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");
    }

    @Test
    public void testFindStrategyStrengthT3() {
        double pubMark = 625 + Math.log10(2.8);
        Theory3 t3 = new Theory3(pubMark);

        t3.variables[3].deactivate();
        t3.variables[5].deactivate();
        t3.variables[6].deactivate();

        while (t3.publicationMultiplier < 15000.0) {
            t3.runEpoch();
        }
    }

    @Test
    public void testFindStrategyStrengthT4() {
        double pubMark = 721 + Math.log10(1.14);
        Theory.studentNumber = 293;
        Theory4 t4 = new Theory4(pubMark);

        t4.variables[1].deactivate();
        t4.variables[3].deactivate();
        t4.variables[4].deactivate();
        t4.variables[5].deactivate();

        Theory4 t42 = new Theory4(pubMark);

        t4.variables[6].level = 100;
        t4.variables[7].level = 100;
        t42.variables[6].level = 100;
        t42.variables[7].level = 100;
        t4.variables[6].update();
        t4.variables[7].update();
        t42.variables[6].update();
        t42.variables[7].update();


        for(int i = 0; i < 50; i++) {
            t4.moveTick();
            t42.moveTick();
            System.out.println(t4.q + "\t" + t42.q);
        }

        t42.variables[7].level += 1;
        t42.variables[7].update();

        for(int i = 0; i < 50; i++) {
            t4.moveTick();
            t42.moveTick();
            System.out.println(t4.q + "\t" + t42.q);
        }

        // best is 163.357 at pub multi 1485.8
        /**while (t4.publicationMultiplier < 15000.0) {
            t4.runEpoch();
        }*/
    }

    @Test
    public void testFindStrategyStrengthT5() {
        double pubMark = 950 + Math.log10(1);
        Theory5 t5 = new Theory5(pubMark);

        while (t5.publicationMultiplier < 4.0) {
            t5.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT6() {
        double pubMark = 1138 + Math.log10(1);
        Theory6 t6 = new Theory6(pubMark);

        /**
         * t6.variables[0].level = 2320;
         * t6.variables[1].level = 553;
         * t6.variables[2].level = 217;
         * t6.variables[3].level = 108;
         * t6.variables[8].level = 1874;
         * t6.rho = Math.log10(1.6) + 1100;
         * t6.q = Math.log10(7.53) + 241;
         * t6.r = Math.log10(1.132) + 42;
         */

        t6.variables[6].deactivate();
        t6.variables[7].deactivate();

        while (t6.publicationMultiplier < 20.0) {
            t6.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT7() {
        double pubMark = 710 + Math.log10(1);
        Theory7 t7 = new Theory7(pubMark);

        t7.variables[1].deactivate();
        t7.variables[2].deactivate();
        t7.variables[3].deactivate();
        /**
         * t4.variables[1].deactivate();
         * t4.variables[3].deactivate();
         * t4.variables[4].deactivate();
         * t4.variables[5].deactivate();
         */

        // best is 163.357 at pub multi 1485.8
        while (t7.publicationMultiplier < 15000.0) {
            t7.runStrategyAILoop(15000.0);
        }
    }

    @Test
    public void testFindStrategyStrengthT8() {
        double pubMark = 600 + Math.log10(1);
        Theory8 t8 = new Theory8(pubMark);

        while (t8.publicationMultiplier < 15000.0) {
            t8.runStrategyAILoop(15000.0);
        }
        System.out.println(t8.totalMultiplier);
    }

    @Test
    public void testFindStrategyStrengthWeierStrass() {
        double pubMark = 870 + Math.log10(Math.pow(1, 10));

        double maxTauPerHour = 0;
        int bestCoastingNumber = 0;

        ArrayList<Strategy> strategies = new ArrayList<>();

        strategies.add(new Strategy("WSPAI", "active"));
        strategies.add(new Strategy("WSP", "idle"));

        double start = System.currentTimeMillis();
        for (Strategy strategy : strategies) {
            
                WeierStrass weierStrass = new WeierStrass(pubMark);
                weierStrass.strategy = strategy;
                
                while (weierStrass.finishCoasting == false) {
                    weierStrass.runEpoch();
                }
        
            weierStrass.printSummary();

            }

        // System.out.println(bestCoastingNumber);

        double finish = System.currentTimeMillis();

        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");

    }

    @Test
    public void testFindStrategyStrengthSequentialLimit() {

        /**
         * t4.variables[1].deactivate();
         * t4.variables[3].deactivate();
         * t4.variables[4].deactivate();
         * t4.variables[5].deactivate();
         */

        double pubMark = 873 + Math.log10(2);
        Sequential_Limit tSL = new Sequential_Limit(pubMark);
        double start = System.currentTimeMillis();
        while (tSL.publicationMultiplier < 15000.0) {
            tSL.runEpoch();
        }
        double finish = System.currentTimeMillis();

        System.out.println("elapsed time: " + String.format("%.3f", (finish - start) / 1000.0) + " seconds.");

    }

}