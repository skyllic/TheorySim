package sim;
import java.util.ArrayList;

/**A class to input strategies */
public class Strategy {

    public String name; // for example "T4c3d"
    public int theoryNumber; // the theory number the strategy pertains to. For example 6 means the strategy is for theory 6

    public String type; // idle, active, speedrun

    public boolean chaseDoubling;

    public ArrayList<Integer> autoBuyVariable; // A list of 0, 1, 5 means to autobuy variables 0, 1, 5 (and nothing else)

    public ArrayList<Integer> chasingVariable; // A list of variables that are chasing the doublings e.g.
    // for T4c3d, q1 is chasing q2 so q1 in T4 (variable[6] so 6) will be in the list
    
    
    public Strategy(String name, String type) {
        if(name.isBlank() || type.isBlank()) {
            this.name = "";
            this.type = "";
            return;
        } else {
            this.name = name;
            this.type = type.toLowerCase();
        }

        this.autoBuyVariable = new ArrayList<>();
        this.chasingVariable = new ArrayList<>();

        

        if(this.type.equalsIgnoreCase("speedrun")) {
            this.autoBuyVariable = null;
            this.chasingVariable = null;
        }
        else if(this.type.equalsIgnoreCase("idle")) {
            this.chasingVariable = null;
            switch(this.theoryNumber) {
                case 6:
                    if(this.name.equalsIgnoreCase("T6C5")) {
                        this.autoBuyVariable.add(0);
                        this.autoBuyVariable.add(1);
                        this.autoBuyVariable.add(2);
                        this.autoBuyVariable.add(3);
                        this.autoBuyVariable.add(8);
                    }

            }
        } else {
            switch(this.theoryNumber) {
                case 6:


            }
               


        }
    }
   

   




}