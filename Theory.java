import java.math.BigDecimal;

public class Theory {

    public Variable[] variables;
   


    public static void main(String[] args){


        BigDecimal bigNumber = new BigDecimal("2e1000");
        BigDecimal big2 = bigNumber.pow(9999);
        
        System.out.println(big2);


        Variable variable = new Variable();
        variable.calculateValueFromLevel();

    }






    



}