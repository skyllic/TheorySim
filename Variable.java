import java.math.*;

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

    public void initialiseLevel(int level) {
        this.level = level;
        this.calculateValueFromLevel();
        this.calculateCostFromLevel(); //placeholder method not made yet;
    }

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

    public void update() {
        this.calculateValueFromLevel();
        this.calculateCostFromLevel();
    }

    public static double add(double value1, double value2) {
        double fractionalPart1 = Math.pow(10, value1 % 1);
        double wholePart1 = value1 - fractionalPart1;
        double fractionalPart2 = Math.pow(10, value2 % 1);
        double wholePart2 = value2 - fractionalPart2;

        double fractionalPart;
        double wholePart;
        //if the powers are the same
        if(wholePart1 - wholePart2 < 0.01) {
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