package cepl;

import cepl.runtime.CELMotor;

public class Main {

    public static void main(String[] args) {
        CELMotor motor = new CELMotor();
        motor.executeDeclaration("DECLARE EVENT T(temp int, id int)");
        motor.executeDeclaration("DECLARE EVENT H(income int, cost int, id int)");
        motor.executeDeclaration("DECLARE EVENT Z()");
        motor.executeDeclaration("DECLARE STREAM S(T, H)");
        motor.executeDeclaration("DECLARE STREAM S2(Z)");
        motor.executeQuery(
                "   SELECT MAX *                                   \n" +
                "   WHERE ( T AS T1 ; H + ; T AS T2 ) +            \n" +
                "   FILTER                                         \n" +
                "      T1[temp < 10] AND                           \n" +
                "      ( T2[temp > 50]                             \n" +
                "        OR                                        \n" +
                "        H[income > 2 * cost] )                    \n" +
                "   PARTITION BY [id]                              \n" +
                "   WITHIN 10 HOURS 20 MINUTES \n" +
                "   CONSUME BY PARTITION "
        );
        System.out.println("Done!");
    }
}
