

/**A class representing a theory. 
 * @param rho - the current rho in log format
 * @param rhodot - the current rhodot in log format. For theory with multiple rho types, this field represents the rho that contributes to tau directly
 * @param tickNumber - the current tick number. Default is 10 ticks/second. Resets on publish. Does not reset on variable bought
 * @param totalMultiplier - total multiplier. It's the number shown in the publication screen (in log format). This implicitly includes bonuses from students
 * @param publicationMultiplier - current publication multiplier for this publication
 * @param publicationMark - the rho at which publication multiplier = 1, represented in log format
 * @param research9Level - current research 9 level. Maximum in the game is 3R9, that is level 3
 * @param studentNumber - current number of students
 */ 
public class Theory implements Simmable {

    public Variable[] variables;
    public double rho;
    public double rhodot;
    public double tickNumber;
    public double totalMultiplier; //e.g. for T6 = T6^0.196 / 50
    public double publicationMultiplier; // current multiplier. e.g. for T6 usually publishes at about 10-30 multi
    public double publicationMark; // Rho at which you can publish e.g. 2.75e965 etc
    public int research9Level;
    public int studentNumber;

    public Theory() {
        
    }
   








    



}