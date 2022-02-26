# TheorySim
 
Initial release version of Exponential Idle Theory Simulator, with support for Custom Theories. This version is meant to be run from the command line only. 

Example usage:
For individual theory:
Open the command line, navigate to the directory containing the sim.jar file and run the following:
java -jar --enable-preview sim.jar <studentNumber> <theoryNumber> <tau> 
(omitting the < and > signs).
theoryNumber = 10 for Weierstrass Sine Product and 11 for Sequential Limit.
Currently, <tau> supports integers only. 

To run all sims:
Open the command line, navigate to the directory containing the sim.jar file and run the following:
java -jar --enable-preview sim.jar <studentNumber> <theoryTau1> <theoryTau2> <theoryTau3> ..... <theoryTauSL>
Note: input a tau value for ALL theories. Put zero for a corresponding theoryTau if you wish to ignore that theory. 
WSP tau comes before SL tau. 