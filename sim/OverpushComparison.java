package sim;

import java.util.ArrayList;

public class OverpushComparison {

    public static void main(String[] args) {

        double[] p1Dist = new double[]{600, 550, 600, 700, 900, 1100, 550, 500};
        double[] p2Dist = new double[]{600, 550, 600, 700, 900, 1100, 550, 500};
        double[] goalDist = new double[]{650, 650, 650, 800, 1000, 1200, 630, 530};
        double p1Sum = getSum(p1Dist);
        double p2Sum = getSum(p2Dist);

        double[] p1OverPushRatios = new double[] { 1, 1, 1, 1, 1, 1, 1, 1 };
        double[] p2OverPushRatios = new double[] { 1, 10.2, 1, 1.5, 1, 3, 1, 1 };

    
        
        int p1Sigma = (int) (p1Sum / 20.0)+15;
        int p2Sigma = p1Sigma;
        double p1Tau = p1Sum;
        double p2Tau = p2Sum;

        double time1 = 0;
        double time2 = 0;

        SimRunner simRunner = new SimRunner();

        

        while (!finishComparison(p1Dist, p2Dist, goalDist)) {
            ArrayList<ArrayList<Summary>> summary1 = new ArrayList<ArrayList<Summary>>();
            ArrayList<ArrayList<Summary>> summary2 = new ArrayList<ArrayList<Summary>>();

            p1Sum = getSum(p1Dist);
            p2Sum = getSum(p2Dist);
            p1Sigma = (int) (Math.floor(p1Sum / 20.0));
            p2Sigma = (int) (Math.floor(p2Sum / 20.0));


            for (int i = 0; i < 8; i++) {
                summary1.add(SimRunner.runDetailedSim(p1Sigma, i + 1, p1Dist[i], true, "strategy=first"));
                summary2.add(SimRunner.runDetailedSim(p2Sigma, i + 1, p2Dist[i], true, "strategy=first"));

            }

            int p1BestTheory = getTheoryToDo(summary1, p1OverPushRatios, goalDist);
            int p2BestTheory = getTheoryToDo(summary2, p2OverPushRatios, goalDist);

            if (summary1.get(p1BestTheory).get(0).publicationMark
                    + summary1.get(p1BestTheory).get(0).tauGain < goalDist[p1BestTheory]) {
                time1 += summary1.get(p1BestTheory).get(0).pubTime;
                p1Dist[p1BestTheory] += summary1.get(p1BestTheory).get(0).tauGain;
            } else if (summary1.get(p1BestTheory).get(0).publicationMark + 0.01 < goalDist[p1BestTheory]) {
                time1 += (goalDist[p1BestTheory] - summary1.get(p1BestTheory).get(0).publicationMark)
                        / summary1.get(p1BestTheory).get(0).tauPerHour;
                p1Dist[p1BestTheory] = goalDist[p1BestTheory];
            }

            if (summary2.get(p2BestTheory).get(0).publicationMark
                    + summary2.get(p2BestTheory).get(0).tauGain < goalDist[p2BestTheory]) {
                time2 += summary2.get(p2BestTheory).get(0).pubTime;
                p2Dist[p2BestTheory] += summary2.get(p2BestTheory).get(0).tauGain;
            } else if (summary2.get(p2BestTheory).get(0).publicationMark + 0.01 < goalDist[p2BestTheory]) {
                time2 += (goalDist[p2BestTheory] - summary2.get(p2BestTheory).get(0).publicationMark)
                        / summary2.get(p2BestTheory).get(0).tauPerHour;
                p2Dist[p2BestTheory] = goalDist[p2BestTheory];
            }

        }

        System.out.println(time1);
        System.out.println(time2);
    }

    public static int getTheoryToDo(ArrayList<ArrayList<Summary>> summaries, double[] overpushRatios, double[] goalDist) {

        int bestIndex = 0;
        double bestTaudotRatio = 0;
        for (int i = 0; i < 8; i++) {
            if(summaries.get(i).get(0).publicationMark + 0.01 > goalDist[i]) {
                continue;
            }
            double taudotRatio = summaries.get(i).get(0).tauPerHour * overpushRatios[i];
            if (taudotRatio > bestTaudotRatio) {
                bestTaudotRatio = taudotRatio;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    public static boolean finishComparison(double[] p1Dist, double[] p2Dist, double[] goalDist) {
        for (int i = 0; i < p1Dist.length; i++) {
            if (p1Dist[i] + 0.001 < goalDist[i] || p2Dist[i] + 0.001 < goalDist[i]) {
                return false;

            }
        }

        return true;
    }

    public static double getSum(double[] dist) {
        double sum = 0;
        for(int i = 0; i < dist.length; i++) {
            sum += dist[i];
        }

        return sum;
    }

}
