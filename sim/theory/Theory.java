package sim.theory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sim.Summary;
import sim.strategy.Strategy;
import sim.upgrades.Variable;

/**
 * A class representing a theory.
 * 
 * @param rho                   - the current rho in log format.
 * @param rhodot                - the current rhodot in log format. For theory
 *                              with multiple rho types, this field represents
 *                              the rho that contributes to tau directly
 * @param seconds               - the current elapsed time in seconds. Resets on
 *                              publish. Does not reset on variable bought. Is
 *                              affected by adaptive ticks.
 * @param tickCount             - the current number of ticks. Default is 10
 *                              ticks/second. Resets on publish. Does not reset
 *                              on variable bought.
 *                              When adaptive ticks are applied, the tickCount
 *                              rate doesn't change.
 * @param totalMultiplier       - total multiplier. It's the number shown in the
 *                              publication screen (in log format).
 *                              This implicitly includes bonuses from students.
 *                              For custom theories, this DOES NOT include
 *                              bonuses from students.
 * @param publicationMultiplier - current publication multiplier for this
 *                              publication. Can be less than 1.
 *                              A publication multiplier less than 1 implies
 *                              that the theory hasn't recovered back to its
 *                              previous
 *                              publication mark yet.
 * @param publicationMark       - the rho at which publication multiplier = 1,
 *                              represented in log format.
 * @param research9Level        - current research 9 level. Maximum in the game
 *                              is 3R9, that is level 3.
 * @param studentNumber         - current number of students.
 */
public class Theory implements ITheory {

        /** Name of theory. */
        public String name;

        /** List of upgrades (non-permanent). */
        public Variable[] variables;

        /** List of upgrades (non-permanent). */
        public double[] variableWeights;

        /** Current rho in log format. */
        public double rho;

        /** Current rhodot in log format. */
        public double rhodot;

        /** Maximum rho reached in this publication, in log format. */
        public double maxRho;

        /** Number of seconds since beginning of publication. */
        public double seconds;

        /** Number of elapsed ticks since beginning of publication. */
        public int tickCount;

        /** Current tau per hour gained in log format. */
        public double tauPerHour;

        /** Seconds per tick. Default is 0.1 seconds per tick (10 ticks/second). */
        public double tickFrequency;

        /**
         * If sim is currently coasting. Coasting is when the sim stops buying variables
         * and waits until publish.
         */
        public boolean isCoasting = false;

        /** If sim finishes coasting. */
        public boolean finishCoasting = false;

        /** */
        public double[] coastingPubs = { 4.5, 800, 2.5, 6.0, 6, 18.0, 3, 1.8, 0, 10.5, 9.5, 6, 4.0, 6 };
        public int coastingNumber = 0;

        /** Maximum tau per hour during publication. (log format) */
        public double maxTauPerHour;

        /** Current best publication multiplier found by the sim. */
        public double bestPubMulti;

        /** Current best publication time, in seconds found by the sim. */
        public double bestPubTime;

        /** Current best tau gain found by the sim (log format). */
        public double bestTauGain;

        /** Current best publication multiplier to start coasting. */
        public double bestCoastStart;

        /** Longest period of time where a variable is not manually bought. */
        public double longestIdlePeriod;

        public double bestCoastingNumber = 0;
        public int activeFrequency = 10;

        public double recoveryTime;
        public double bestRecoveryTime;
        public boolean recoveryFlag = false;

        public boolean readyToPublish;

        public double totalMultiplier; // e.g. for T6 = T6^0.196 / 50
        public double publicationMultiplier; // current multiplier. e.g. for T6 usually publishes at about 10-30 multi
        public final double publicationMark; // Rho at which you can publish e.g. 2.75e965 etc
        public static int research9Level = 3; // default 3 for lategame purposes.
        public static int studentNumber = 300; // default 300 for endgame purposes.

        public final static double adBonus = 1.5;
        public static int theoryNumber; // First Custom Theory = theory10.
        public double tauEfficiency; // Defined as maxRho divided by tickNumber.

        public Strategy strategy;

        public int currentRatesPointer;

        public double currentIdlePeriod;
        public double minimumWeight;

        public double coastStart = 0;
        public double pubCoefficient;
        public double coastTauPerHour;

        public static double[] K_value = new double[8];

        public double t;

        public static HashMap<Double, Double[]> rates;

        /**
         * 
         * @param theoryNumber - theory number to generate. Default theories are 1-8.
         *                     First CT is theory 10.
         *                     Use 10 to generate Weierstrass Sine Product.
         *                     <p>
         *                     Use 11 to generate Sequential Limit.
         *                     <p>
         *                     Use 12 to generate Convergence to Square Root 2.
         *                     <p>
         *                     Use 13 to generate Basic Theory.
         * @param pubMark      - The last pub mark of the theory in log format. e.g. if
         *                     last pub mark was at 2e600 then
         *                     pubMark = log10(2) + 600. For CT pubmarks, use the RHO
         *                     rather than tau.
         */
        public Theory(int theoryNumber, double pubMark) {
                this.publicationMark = pubMark;
                Theory.theoryNumber = theoryNumber;

                this.seconds = 0;
                this.tickCount = 0;
                this.rho = 0;
                this.rhodot = 0;
                this.currentIdlePeriod = 0;
                this.longestIdlePeriod = 0;
                this.readyToPublish = false;

                if (this.publicationMark < 250) { // Assume full milestones at e250.
                        this.tickFrequency = 1.0;
                } else if (this.publicationMark < 600) {
                        this.tickFrequency = 1.0;
                } else {
                        this.tickFrequency = 1.0;
                }
                if (theoryNumber == 2) {
                        if (this.publicationMark < 250) {
                                this.tickFrequency = 1.0;
                        } else {
                                this.tickFrequency = 1.0;
                        }

                }

                initialiseR9Level();
                initialiseTheoryMultiplier();
                initialiseVariables();
                initialiseVariableWeights();
                if (Theory.rates == null) {
                        Theory.rates = new HashMap<>();
                        // loadTheoryRates();

                }

        }

        private void initialiseTheoryMultiplier() {

                // Sets total multiplier for each theory. Each theory has its own formula.
                if (Theory.theoryNumber == 1) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.164 * this.publicationMark - Math.log10(3);
                        this.pubCoefficient = 0.164;
                } else if (Theory.theoryNumber == 2) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.198 * this.publicationMark - Math.log10(100);
                        this.pubCoefficient = 0.198;
                } else if (Theory.theoryNumber == 3) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.147 * this.publicationMark + Math.log10(3);
                        this.pubCoefficient = 0.147;
                } else if (Theory.theoryNumber == 4) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.165 * this.publicationMark - Math.log10(4);
                        this.pubCoefficient = 0.165;
                } else if (Theory.theoryNumber == 5) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.159 * this.publicationMark;
                        this.pubCoefficient = 0.159;
                } else if (Theory.theoryNumber == 6) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.196 * this.publicationMark - Math.log10(50);
                        this.pubCoefficient = 0.196;
                } else if (Theory.theoryNumber == 7) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.152 * this.publicationMark;
                        this.pubCoefficient = 0.152;
                } else if (Theory.theoryNumber == 8) {
                        this.totalMultiplier = Theory.research9Level
                                        * (Math.log10(Theory.studentNumber) - Math.log10(20))
                                        + 0.15 * this.publicationMark;
                        this.pubCoefficient = 0.15;
                } else if (Theory.theoryNumber == 10) {// WSP
                        this.totalMultiplier = 0.15 * this.publicationMark;
                        this.pubCoefficient = 0.15;
                } else if (Theory.theoryNumber == 11) {// SL
                        this.totalMultiplier = 0.15 * this.publicationMark;
                        this.pubCoefficient = 0.15;
                } else if (Theory.theoryNumber == 12) {// CSR2
                        this.totalMultiplier = 0.2203 * this.publicationMark - Math.log10(200);
                        this.pubCoefficient = 0.2203;
                } else if (Theory.theoryNumber == 13) {// BT
                        this.totalMultiplier = 0.5 * this.publicationMark;
                        this.pubCoefficient = 0.5;
                } else if (Theory.theoryNumber == 14) {// EF
                        this.totalMultiplier = 0.387 * 0.4 * this.publicationMark;
                        this.pubCoefficient = 0.387 * 0.4;
                } else {
                        this.totalMultiplier = 1;
                }
        }

        private void initialiseR9Level() {
                // Research 9 implications.
                if (Theory.studentNumber < 65) {
                        Theory.research9Level = 0;
                } else if (Theory.studentNumber < 75) {
                        Theory.research9Level = 1;
                } else if (Theory.studentNumber < 85) {
                        Theory.research9Level = 2;
                } else {
                        Theory.research9Level = 3;
                }
        }

        private void initialiseVariableWeights() {

                this.variableWeights = new double[this.variables.length];
               for(int i = 0; i < this.variableWeights.length; i++) {
                this.variableWeights[i] = 10.0;
               }
        }
        private void initialiseVariables() {
                if (Theory.theoryNumber == 1) {

                        this.variables = new Variable[6];
                        // Order of variable is q1, q2, c1, c2, c3, c4
                        this.variables[0] = new Variable(2, 5, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(10, 100, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[2] = new Variable(2, 15, Math.pow(2, 0.1), 1, false, true, false, false, true,
                                        new double[2]);
                        this.variables[3] = new Variable(10, 3000, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(Math.pow(10, 4.5), 10000, 10, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[5] = new Variable(Math.pow(10, 8), Math.pow(10, 10), 10, 1, true, true, false,
                                        false, false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 2) {
                        this.variables = new Variable[8];
                        // Order of variable is q1, q2, q3, q4, r1, r2, r3, r4
                        this.variables[0] = new Variable(2, 10, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(2, 5000, Math.pow(2, 0.1), 0, false, true, false, false, false,
                                        new double[2]);
                        this.variables[2] = new Variable(3, 3 * Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[3] = new Variable(4, 8 * Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[4] = new Variable(2, 2 * Math.pow(10, 6), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[5] = new Variable(2, 3 * Math.pow(10, 9), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[6] = new Variable(3, 4 * Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[7] = new Variable(4, 5 * Math.pow(10, 50), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 3) {
                        this.variables = new Variable[12];
                        // Order of variable is b1, b2, b3, c11, c12, c13, c21, c22, c23, c31, c32, c33.
                        this.variables[0] = new Variable(1.18099, 10, Math.pow(2, 0.1), 0, false, true, false, true,
                                        false,
                                        new double[2]);
                        this.variables[1] = new Variable(1.308, 10, Math.pow(2, 0.1), 0, false, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[2] = new Variable(1.675, 3000, Math.pow(2, 0.1), 0, false, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[3] = new Variable(6.3496, 20, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(2.74, 10, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[5] = new Variable(1.965, 1000, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[6] = new Variable(18.8343, 1000, 2, 0, true, true, false, false, false,
                                        new double[2]);
                        this.variables[7] = new Variable(3.65, Math.pow(10, 5), 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[8] = new Variable(2.27, Math.pow(10, 5), 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[9] = new Variable(1248.27, Math.pow(10, 4), 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[10] = new Variable(6.81774, Math.pow(10, 3), 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[11] = new Variable(2.98, Math.pow(10, 5), 2, 1, true, true, false, false, false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 4) {
                        this.variables = new Variable[8];
                        // Order of variable is c1, c2, c3, c4, c5, c6, q1, q2
                        this.variables[0] = new Variable(1.305, 5, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(3.75, 20, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[2] = new Variable(2.468, 2000, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[3] = new Variable(4.85, Math.pow(10, 4), 3, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(12.5, Math.pow(10, 8), 5, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[5] = new Variable(58, Math.pow(10, 10), 10, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[6] = new Variable(100, 1000, Math.pow(2, 0.1), 0, false, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[7] = new Variable(1000, Math.pow(10, 4), 2, 1, true, true, false, false, false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 5) {
                        this.variables = new Variable[5];
                        // Order of variable is q1, q2, c1, c2, c3
                        this.variables[0] = new Variable(1.61328, 10, Math.pow(2, 0.1), 0, false, true, false, true,
                                        false,
                                        new double[2]);
                        this.variables[1] = new Variable(64, 15, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[2] = new Variable(1.18099, Math.pow(10, 6), 2, 1, false, true, false, false,
                                        true,
                                        new double[2]);
                        this.variables[3] = new Variable(4.53725, 75, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(88550700, Math.pow(10, 3), 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 6) {
                        this.variables = new Variable[9];
                        // Order of variable is q1, q2, r1, r2, c1, c2, c3, c4, c5
                        this.variables[0] = new Variable(3, 15, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(100, 500, 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[2] = new Variable(100000, Math.pow(10, 25), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false, new double[2]);
                        this.variables[3] = new Variable(Math.pow(10, 10), Math.pow(10, 30), 2, 1, true, true, false,
                                        false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(2, 10, Math.pow(2, 0.1), 1, false, true, false, false, true,
                                        new double[2]);
                        this.variables[5] = new Variable(5, 100, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[6] = new Variable(1.255, Math.pow(10, 7), Math.pow(2, 0.1), 0, false, true,
                                        false, false,
                                        false,
                                        new double[2]);
                        this.variables[7] = new Variable(500000, Math.pow(10, 25), 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[8] = new Variable(3.9, 15, 2, 1, true, true, false, false, false, new double[2]);
                } else if (Theory.theoryNumber == 7) {
                        this.variables = new Variable[7];
                        // Order of variable is q1, v1, c2, c3, c4, c5, c6
                        this.variables[0] = new Variable(1.51572, 500, Math.pow(2, 0.1), 0, false, true, false, true,
                                        false,
                                        new double[2]);
                        this.variables[1] = new Variable(1.275, 10, Math.pow(2, 0.1), 1, false, true, false, false,
                                        true,
                                        new double[2]);
                        this.variables[2] = new Variable(8, 40, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[3] = new Variable(63, Math.pow(10, 5), 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(2.82, Math.pow(10, 1), 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[5] = new Variable(60, Math.pow(10, 8), 2, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[6] = new Variable(2.81, 100, 2, 1, true, true, false, false, false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 8) {
                        this.variables = new Variable[5];
                        // Order of variable is c1, c2, c3, c4, c5
                        this.variables[0] = new Variable(1.5172, 10, Math.pow(2, 0.1), 0, false, true, false, true,
                                        false,
                                        new double[2]);
                        this.variables[1] = new Variable(64, 20, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[2] = new Variable(Math.pow(3, 1.15), 100, 3, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[3] = new Variable(Math.pow(5, 1.15), 100, 5, 1, true, true, false, false, false,
                                        new double[2]);
                        this.variables[4] = new Variable(Math.pow(7, 1.15), 100, 7, 1, true, true, false, false, false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 10) { // Weierstrass Sine Product
                        this.variables = new Variable[5];
                        // Order of variable is q1, q2, n, c1, c2
                        this.variables[0] = new Variable(Math.pow(2, 3.38 / 4), 10, Math.pow(2, 0.1), 0, false, true,
                                        false, true,
                                        false, new double[2]);
                        this.variables[1] = new Variable(Math.pow(2, 3.38 * 3), 1000, 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                        this.variables[2] = new Variable(Math.pow(2, 3.38), 20, 1, 0, false, false, true, false, false,
                                        new double[2]);
                        this.variables[3] = new Variable(Math.pow(2, 3.38 / 1.5), 50, Math.pow(2, 1 / 50.0), 1, true,
                                        true, false,
                                        false, true, new double[2]);
                        this.variables[4] = new Variable(Math.pow(2, 3.38 * 10), Math.pow(10, 10), 2, 1, true, true,
                                        false, false,
                                        false, new double[2]);
                } else if (Theory.theoryNumber == 11) { // Sequential Limit
                        this.variables = new Variable[4];
                        // Order of variable is a1, a2, b1, b2
                        this.variables[0] = new Variable(Math.pow(10, 0.369), 1.0, Math.pow(3.5, 3.5 / 3.0), 0, false,
                                        true, false,
                                        true, false, new double[] { 3.5, 3 });
                        this.variables[1] = new Variable(10, 175, 2, 1, true, true, false, false, true, new double[2]);
                        this.variables[2] = new Variable(Math.pow(10, 0.649), 500, Math.pow(6.5, 6.5 / 4.0), 1, false,
                                        true, false,
                                        false, false, new double[] { 6.5, 4 });
                        this.variables[3] = new Variable(Math.pow(10, 0.926), 1000, 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 12) { // Convergence to Square Root 2
                        this.variables = new Variable[5];
                        // Order of variable is .... (TBA)
                        this.variables[0] = new Variable(5, 10, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(128, 15, 2, 1, true, true, false, false, false, new double[2]);
                        this.variables[2] = new Variable(16, 1000000, Math.pow(2, 0.1), 1, false, true, false, false,
                                        true,
                                        new double[2]);
                        this.variables[3] = new Variable(Math.pow(256, 3.346), 50, 1, 1, false, false, true, false,
                                        false, new double[2]);
                        this.variables[4] = new Variable(Math.pow(10, 5.65), 1000, 2, 1, true, true, false, false,
                                        false,
                                        new double[2]);
                } else if (Theory.theoryNumber == 13) { // Basic Theory
                        this.variables = new Variable[2];
                        // Order of variable is a1, a2,
                        this.variables[0] = new Variable(2, 15, Math.pow(2, 0.1), 0, false, true, false, true, false,
                                        new double[2]);
                        this.variables[1] = new Variable(10, 5, 2, 1, true, true, false, false, false, new double[2]);
                } else if (Theory.theoryNumber == 14) {// EF
                        this.variables = new Variable[10];

                        this.variables[0] = new Variable(1000000, 1000000, 0.2, 0.2,
                                        false, false, true, false, false, new double[2]);
                        this.variables[1] = new Variable(1.61328, 10, Math.pow(2, 0.1), 0,
                                        false, true, false, true, false, new double[2]);
                        this.variables[2] = new Variable(60, 5, 2, 1,
                                        true, true, false, false, false, new double[2]);
                        this.variables[3] = new Variable(200, 20, Math.pow(2, 0.1), 1,
                                        false, true, false, true, true, new double[2]);
                        this.variables[4] = new Variable(2, 100, Math.pow(1.1, 1), 1,
                                        true, true, false, false, false, new double[2]);
                        this.variables[5] = new Variable(200, 20, Math.pow(2, 0.1), 1,
                                        false, true, false, true, true, new double[2]);
                        this.variables[6] = new Variable(2, 100, Math.pow(1.1, 1), 1,
                                        true, true, false, false, false, new double[2]);
                        this.variables[7] = new Variable(Math.pow(2, 2.2), 2000, Math.pow(2, 0.1), 1,
                                        false, true, false, true, true, new double[2]);
                        this.variables[8] = new Variable(Math.pow(2, 2.2), 500, Math.pow(40, 0.1), 1,
                                        false, true, false, false, true, new double[] { 40, 10 });
                        this.variables[9] = new Variable(Math.pow(2, 2.2), 500, 2, 1,
                                        true, true, false, false, false, new double[2]);

                }

        }

        /** Intended to be overridden by corresponding theory. */

        public void moveTick() {

                this.seconds += this.tickFrequency;
                this.tickCount += 1;
                this.tickFrequency *= 1.0001;

                this.updateStatistics();
                this.collectStatistics();

                // this.isReadyToCoast();
                // this.readyToPublish();

        }

        private boolean readyToPublish() {

                if (this.maxRho < this.publicationMark) {
                        return false;
                }
                if (this.calculateInstantaneousRhoPerHour(this.maxRho) < this.getTauPerHour(this.maxRho)) {
                        return true;
                } else {
                        return false;
                }
        }

        /** Updates general variables that may be affected by moving ticks. */
        private void updateStatistics() {
                this.currentIdlePeriod += this.tickFrequency;
                if (this.longestIdlePeriod < this.currentIdlePeriod) {
                        this.longestIdlePeriod = this.currentIdlePeriod;
                }

                if (this.publicationMultiplier > 1 && recoveryFlag == false) {
                        this.recoveryTime = this.seconds / 3600.0;
                        recoveryFlag = true;
                }

                if (this.rho > this.maxRho) {
                        this.maxRho = this.rho;
                }
                /**
                 * if (this.coastStart > this.bestPubMulti) {
                 * this.coastStart = this.bestPubMulti;
                 * }
                 */
        }

        /** Compiles summary statistics for display to the user. */
        private void collectStatistics() {
                // Compiles various parameters when there's a new fastest tau/hour so that we
                // can print them
                // to the user later.
                if (!isCoasting) {
                        if ((this.maxRho - this.publicationMark) / (this.seconds / 3600.0) > this.maxTauPerHour
                        /** this.seconds > 10000000 */
                        ) {
                                this.maxTauPerHour = (this.maxRho - this.publicationMark) / (this.seconds / 3600.0);
                                this.bestPubMulti = this.publicationMultiplier;
                                this.bestPubTime = this.seconds / 3600.0;
                                this.bestTauGain = this.maxRho - this.publicationMark;
                                this.bestRecoveryTime = this.recoveryTime;
                                this.bestCoastStart = this.coastStart;
                        }
                } else {
                        if ((this.maxRho - this.publicationMark) / (this.seconds / 3600.0) > this.maxTauPerHour
                        /** this.seconds > 10000000 */
                        ) {
                                this.maxTauPerHour = (this.maxRho - this.publicationMark) / (this.seconds / 3600.0);
                                this.bestPubMulti = this.publicationMultiplier;
                                this.bestPubTime = this.seconds / 3600.0;
                                this.bestTauGain = this.maxRho - this.publicationMark;
                                this.bestRecoveryTime = this.recoveryTime;
                                this.bestCoastStart = this.coastStart;
                        }
                }

        }

        private void updateStatisticsSummary() {
                if((this.maxRho - this.publicationMark) / (this.seconds / 3600.0) > this.maxTauPerHour) {
                        this.maxTauPerHour = (this.maxRho - this.publicationMark) / (this.seconds / 3600.0);
                                this.bestPubMulti = this.publicationMultiplier;
                                this.bestPubTime = this.seconds / 3600.0;
                                this.bestTauGain = this.maxRho - this.publicationMark;
                                this.bestRecoveryTime = this.recoveryTime;
                                this.bestCoastStart = this.coastStart;
                }
                
        }

        public void loadTheoryRates() {
                try {

                        BufferedReader reader = new BufferedReader(new FileReader(
                                        "TheorySim\\sim\\data\\300sigma\\Theory"
                                                        + Theory.theoryNumber + "PlayRates.txt"));
                        try {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                        String[] split = line.split("\t");

                                        double tau = Double.parseDouble(split[0]);
                                        double rate = Double.parseDouble(split[1]);
                                        double rhoGain = Double.parseDouble(split[2]);

                                        Theory.rates.put(tau, new Double[] { rate, rhoGain });

                                }

                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        /**
         * Buys 1 level of the variable according to the variableNumber input. For
         * example, an input of 3
         * means buy 1 level of variable[3] (the 4th variable in the array). The nth
         * variable in the array is read
         * top-down in-game.
         * Both the values of the variables and rho are updated in this method, so there
         * is no need to further
         * update the variable parameters.
         * <p>
         * Some theories may wish to override this method as there may be more than one
         * type of rhos.
         * </p>
         * 
         * @param variableNumber - the variable number to buy. Note that the variable
         *                       number starts at 0, not 1.
         */
        public void buyVariable(int variableNumber) {

                double variableCost = this.variables[variableNumber].nextCost;
                if (this.rho >= variableCost) {
                        this.variables[variableNumber].level += 1;
                        this.variables[variableNumber].update();
                        this.rho = Variable.subtract(this.rho, variableCost);
                } else {
                        // too poor to buy stuff feelsbadman
                }

        }

        public void resetIdlePeriod(int variableNumber) {

                this.currentIdlePeriod = 0;

        }

        /**
         * Displays intermittent theory parameters for testing purposes. Not intended
         * for UI output to users.
         */
        public void display() {

        }

        public double findExpectedNextPubRhoRate(double tau) {
                return this.getTauPerHour(tau + this.getRhoGain(tau));
        }

        public double getTauPerHour(double currentTau) {
                double roundedTau = Math.floor(10.0 * (currentTau + 0.05)) / 10.0;
                return Theory.rates.get(roundedTau)[0];
        }

        public double getRhoGain(double currentTau) {
                double roundedTau = Math.floor(10.0 * (currentTau + 0.05)) / 10.0;
                return Theory.rates.get(roundedTau)[1];
        }

        public double calculateInstantaneousRhoPerHour(double currentRho) {
                double rhoPerHour = this.rhodot - Math.log10(this.tickFrequency) + Math.log10(3600);
                double nextRho = Variable.add(this.rho, rhoPerHour);

                double instantaneousRhoPerHour = nextRho - this.rho;

                return instantaneousRhoPerHour;

        }

        public boolean isReadyToCoast() {
                if (this.maxRho < this.publicationMark) {
                        return false;
                }
                if (this.rho > 645.0) {
                        int k = 2;
                }
                if (this.maxRho >= this.getRhoGain(this.publicationMark) + this.publicationMark - 0.01
                /**
                 * this.calculateInstantaneousRhoPerHour(this.rho) <
                 * this.getTauPerHour(this.rho)
                 */
                ) {
                        /**
                         * for(int j = 0; j < this.variables.length; j++) {
                         * this.variables[j].deactivate();
                         * }
                         */
                        this.calculateInstantaneousRhoPerHour(this.rho);
                        this.getTauPerHour(this.rho);

                        this.isCoasting = true;
                        return true;

                } else {
                        return false;
                }
        }

        public double convertCostToPubMulti(double cost) {
                double temp = cost - this.publicationMark;
                double pubMulti = 1;
        
                if(Theory.theoryNumber == 8) {
                        pubMulti = Math.pow(Math.pow(10, 0.15), temp);
                } else if(Theory.theoryNumber == 1) {
                        pubMulti = Math.pow(Math.pow(10, 0.164), temp);
                } else if(Theory.theoryNumber == 2) {
                        pubMulti = Math.pow(Math.pow(10, 0.198), temp);
                } else if(Theory.theoryNumber == 3) {
                        pubMulti = Math.pow(Math.pow(10, 0.147), temp);
                } else if(Theory.theoryNumber == 6) {
                        pubMulti = Math.pow(Math.pow(10, 0.196), temp);
                } 
                else {
                        pubMulti = 1;
                }
                
                return pubMulti;
            }

            public boolean readyToCoast(double coastMulti) {
               
                    
                for(int i = 0; i < this.variables.length; i++) {
                        if(this.variables[i].isActive == 0) {
                                continue;
                        }
                    if(this.convertCostToPubMulti(this.variables[i].nextCost + this.variableWeights[i] - 10)
                     > coastMulti) {
        
                    } else {
                        return false;
                    }
                }
        
                return true;
            }

        public void coastUntilPublish() {

                // System.out.println(this.publicationMultiplier);
                if (this.coastStart == 0) {
                        this.coastStart = this.publicationMultiplier;
                }

                while (this.rho < this.maxRho) {
                        // System.out.println("test");
                        this.moveTick();
                }
                this.tauPerHour = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10) / this.pubCoefficient
                                / this.seconds;
                this.moveTick();
                double tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10) / this.pubCoefficient
                                / this.seconds;

                this.coastTauPerHour = (this.maxRho - this.publicationMark) / (this.seconds / 3600.0);
                
                double tempTauHour;
                // 2 scenarios, one where the next pub tau/h max is lower than current, and one
                // where it's higher.
                // if next is lower.
                if (/**
                     * this.findExpectedNextPubRhoRate(this.publicationMark) <
                     * this.getTauPerHour(this.publicationMark)
                     */
                false) {
                        while (this.calculateInstantaneousRhoPerHour(this.rho) > this.getTauPerHour(this.rho)) {
                                // System.out.println("hi\t" + this.publicationMultiplier);
                                this.tauPerHour = tauRate;
                                this.moveTick();
                                tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                / this.pubCoefficient
                                                / this.seconds;

                        }
                } else {
                        while (this.tauPerHour <= tauRate) {

                                this.tauPerHour = tauRate;
                                this.moveTick();
                                tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                / this.pubCoefficient
                                                / this.seconds;

                        }
                        tempTauHour = this.tauPerHour;
                        

                        if(theoryNumber == 2) {
                                while (tauRate >= tempTauHour * 1.0) {
                                        this.tauPerHour = tauRate;
                                        this.moveTick();
                                        tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                        / this.pubCoefficient
                                                        / this.seconds;
                                }
                        } else if(theoryNumber == 14) {
                                while (tauRate >= tempTauHour * 1.0) {
                                        this.tauPerHour = tauRate;
                                        this.moveTick();
                                        tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                        / this.pubCoefficient
                                                        / this.seconds;
                                }
                        } else if(theoryNumber== 8) {
                                while (tauRate >= tempTauHour * 0.999955) {
                                        //System.out.println("HIHIHIHI");
                                        this.tauPerHour = tauRate;
                                        this.moveTick();
                                        tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                        / this.pubCoefficient
                                                        / this.seconds;
                                }
                        }
                        else {
                                while (tauRate >= tempTauHour * 1.0) {
                                        this.tauPerHour = tauRate;
                                        this.moveTick();
                                        tauRate = 60 * 60 * Math.log(this.publicationMultiplier) / Math.log(10)
                                                        / this.pubCoefficient
                                                        / this.seconds;
                                }    
                        }

                        
                }

                this.updateStatisticsSummary();

                this.resetIdlePeriod(2);
                this.publish();
        }

        public void publish() {
                this.readyToPublish = true;

        }

        public void setCoastingPubs(int position, double value) {
                this.coastingPubs[position] = value;
        }

        public double getCoastingPub(int position) {
                return this.coastingPubs[position];
        }

        public void setStrategy(Strategy strategy) {
                this.strategy = strategy;
        }

        public Summary getSummary() {
                Summary summary = new Summary(Theory.theoryNumber, this.maxTauPerHour, this.bestPubMulti,
                                this.strategy.name, this.strategy.type,
                                this.bestPubTime, this.bestRecoveryTime, this.bestTauGain, this.coastStart,
                                this.variables,
                                this.longestIdlePeriod / 3600.0, this.name, this.publicationMark, Theory.studentNumber);

                return summary;
        }

        public void runUntilPublish() {
                // TODO Auto-generated method stub

        }

        @Override
        public void setStudent(int studentNumber) {
                Theory.studentNumber = studentNumber;

        }

}