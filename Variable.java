import java.math.*;

/** */
public class Variable {

    public double costScaling;
    public double costBase;
    public double cost;
    public double value;
    public int level;
    public double valueScaling;
    public double valueBase;
    public boolean isDoubling;
    public boolean isExponential;
    public boolean isLinear;

    public boolean isFirst;
    public boolean isOffset;



    public Variable() {
        
    }

    /**Represents a buyable variable in a theory or lemma.
     * 
     * @param costScaling - the cost multiplier for each level. e.g. costScaling of 3 dictates the cost 
     * triples each time the variable is bought
     * @param costBase - the initial cost of the variable. For an 'offset' variable, this is the cost of the 
     * first non-free cost
     * @param valueScaling - how much the value increases with each level. For a standard stepwise non-doubling variable, 
     * the value is 2^0.1.
     * @param valueBase - the initial value of the variable
     * @param isDoubling - whether the variable is a doubling. A standard stepwise variable is not considered a doubling
     * @param isExponential - whether the variable's value scales exponentially with level. Both doublings and standard
     * non-doubling variables are considered exponentials (standard non-doublings double every 10 levels)
     * @param isLinear - whether the variable's value scales linearly. Theories 1-8 don't have this. However some lemmas do
     * @param isFirst - Whether the first level of the variable is buyable for free
     * @param isOffset - If the variable is a standard non-doubling, whether its value starts at 1 instead of 0.
     * If the variable is a doubling, this parameter should be false.
     */
    public Variable(double costScaling, double costBase, double valueScaling, double valueBase, 
                    boolean isDoubling, boolean isExponential, boolean isLinear, boolean isFirst, boolean isOffset) {
        this.costScaling = costScaling;
        this.costBase = costBase;
        this.valueScaling = valueScaling;
        this.valueBase = valueBase;
        this.isDoubling = isDoubling;
        this.isExponential = isExponential;
        this.isLinear = isLinear;
        this.isFirst = isFirst;
        this.isOffset = isOffset;
    }

    /** Calculate and fill in the variable value and cost given an input variable level.
     * 
     * @param level - the level of the variable
     */
    public void initialiseLevel(int level) {
        this.level = level;
        this.calculateValueFromLevel();
        this.calculateCostFromLevel(); //placeholder method not made yet;
    }

    /**Helper method for the initialiseLevel method. Calculates the value of the variable.
     * Assumes that the variable level field has already been filled in.
     */
    public void calculateValueFromLevel() {
        if(isExponential) {
            if(!isDoubling) {
                if(!isOffset) {
                    this.value = this.subtract(Math.log10(10+this.level % 10) + 
                    Math.log10(2) * Math.floor(this.level/10), Math.log10(10));
                } else {
                    this.value = this.subtract(Math.log10(11+this.level % 10) + 
                    Math.log10(2) * Math.floor((1+this.level)/10), Math.log10(10));
                }
            } else {
                this.value = Math.log10(this.valueBase) + this.level * Math.log10(this.valueScaling);    
            }
        }

        if(this.value < -9999) {
            this.value = -9999;
        }
        return;

    }

    /**Helper method for the initialiseLevel method. Calculates the current cost of the variable
     * (not cost of the next level). In the game screen, the cost calculated from this method will be the
     * 1 level LOWER than the cost shown in the screen (as the cost shown in the game screen is for the next variable level)
     */
    public void calculateCostFromLevel() {
        if(isExponential) {
            if(!isFirst) {
                this.cost = Math.log10(this.costBase) + this.level * Math.log10(this.costScaling);
            } else {
                if(this.level <= 1) {
                    this.cost = -9999; //close enough to 0
                } else {
                    this.cost = Math.log10(this.costBase) + (this.level - 1) * Math.log10(this.costScaling);
                }
            }
        }
    }

    /**Updates the value and cost of the variable. Uses the variable level in the field column */
    public void update() {
        this.calculateValueFromLevel();
        this.calculateCostFromLevel();
    }

    /**
     * Calculates the sum of 2 numbers in log format. e.g. an input of 320.5 is meant to represent 10^320.5.
     * 
     * If the 2 inputs differ by more than 10^8 magnitude, return the higher of the 2 numbers.
     * 
     * @param value1 - the first value in log format
     * @param value2 - the second value in log format
     * @return - the sum (in log format) of the 2 input values
     */
    public static double add(double value1, double value2) {
        double fractionalPart1 = Math.pow(10, value1 % 1);
        double wholePart1 = Math.floor(value1);
        double fractionalPart2 = Math.pow(10, value2 % 1);
        double wholePart2 = Math.floor(value2);

        double fractionalPart;
        double wholePart;
        //if the powers are the same
        if(Math.abs(wholePart1 - wholePart2) < 0.01) {
            if(fractionalPart1 + fractionalPart2 >= 10) {
                wholePart = wholePart1 + 1;
                fractionalPart = (fractionalPart1 + fractionalPart2)/10.0;
            } else {
                wholePart = wholePart1;
                fractionalPart = fractionalPart1 + fractionalPart2;
            }
        // if the powers are vastly different
        } else if(Math.abs(wholePart1 - wholePart2) > 8) {
            if(wholePart1 > wholePart2) {
                wholePart = wholePart1;
                fractionalPart = fractionalPart1;
            } else {
                wholePart = wholePart2;
                fractionalPart = fractionalPart2;
            }
        // otherwise
        } else {
            if(wholePart1 > wholePart2) {
                fractionalPart2 = fractionalPart2/Math.pow(10, wholePart1-wholePart2);
                wholePart2 = wholePart1;
                if(fractionalPart1 + fractionalPart2 >= 10) {
                    wholePart = wholePart1 + 1;
                    fractionalPart = (fractionalPart1 + fractionalPart2)/10.0;
                } else {
                    wholePart = wholePart1;
                    fractionalPart = fractionalPart1 + fractionalPart2;
                }
                
            } else {
                fractionalPart1 = fractionalPart1/Math.pow(10, wholePart2-wholePart1);
                wholePart1 = wholePart2;
                if(fractionalPart1 + fractionalPart2 >= 10) {
                    wholePart = wholePart2 + 1;
                    fractionalPart = (fractionalPart1 + fractionalPart2)/10.0;
                } else {
                    wholePart = wholePart2;
                    fractionalPart = fractionalPart1 + fractionalPart2;
                }
            }
        }
        double[] result = new double[2];
        result[0] = fractionalPart;
        result[1] = wholePart;

        return wholePart + Math.log10(fractionalPart);
            
        
    }

     /**
     * Calculates the difference of 2 numbers in log format. e.g. an input of 320.5 is meant to represent 10^320.5.
     * 
     * If the 2 inputs differ by more than 10^8 magnitude, return the higher of the 2 numbers.
     * 
     * @param value1 - the first value in log format
     * @param value2 - the second value in log format
     * @return - the difference (in log format) of the 2 input values
     */
    public double subtract(double value1, double value2) {
        double fractionalPart1 = Math.pow(10, value1 % 1);
        double wholePart1 = value1 - fractionalPart1;
        double fractionalPart2 = Math.pow(10, value2 % 1);
        double wholePart2 = value2 - fractionalPart2;

        double fractionalPart;
        double wholePart;
        //if the powers are the same
        if(wholePart1 - wholePart2 < 0.01) {
            if(fractionalPart1 - fractionalPart2 <= 1) {
                wholePart = wholePart1 - 1;
                fractionalPart = (fractionalPart1 - fractionalPart2)*10.0;
            } else {
                wholePart = wholePart1;
                fractionalPart = fractionalPart1 - fractionalPart2;
            }
        // if the powers are vastly different
        } else if(Math.abs(wholePart1 - wholePart2) > 8) {
            if(wholePart1 > wholePart2) {
                wholePart = wholePart1;
                fractionalPart = fractionalPart1;
            } else {
                wholePart = wholePart2;
                fractionalPart = fractionalPart2;
            }
        // otherwise
        } else {
            if(wholePart1 > wholePart2) {
                fractionalPart2 = fractionalPart2/Math.pow(10, wholePart1-wholePart2);
                wholePart2 = wholePart1;
                if(fractionalPart1 - fractionalPart2 <= 1) {
                    wholePart = wholePart1 - 1;
                    fractionalPart = (fractionalPart1 - fractionalPart2)/10.0;
                } else {
                    wholePart = wholePart1;
                    fractionalPart = fractionalPart1 - fractionalPart2;
                }
                
            } else {
                fractionalPart1 = fractionalPart1/Math.pow(10, wholePart2-wholePart1);
                wholePart1 = wholePart2;
                if(fractionalPart1 - fractionalPart2 <= 1) {
                    wholePart = wholePart2 - 1;
                    fractionalPart = (fractionalPart1 - fractionalPart2)/10.0;
                } else {
                    wholePart = wholePart2;
                    fractionalPart = fractionalPart1 - fractionalPart2;
                }
            }
        }

        while(fractionalPart < 1) {
            wholePart = wholePart - 1;
            fractionalPart = (fractionalPart)*10.0;
        }
        double[] result = new double[2];
        result[0] = fractionalPart;
        result[1] = wholePart;
        return wholePart + Math.log10(fractionalPart);
            
        
    }


    
}