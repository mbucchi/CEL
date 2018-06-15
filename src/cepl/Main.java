package cepl;

import cepl.runtime.CELMotor;

public class Main {

    public static void main(String[] args) {
        CELMotor motor = new CELMotor();
        motor.executeDeclaration("DECLARE EVENT T(temp int, id int)");
        motor.executeDeclaration("DECLARE EVENT H(income int, cost int, id int)");
        motor.executeDeclaration("DECLARE STREAM S(T, H)");
        motor.executeQuery(
                "SELECT MAX *                                   \n" +
                "FROM S                                         \n" +
                "WHERE ( T AS T1 ; H + ; T AS T2 ) +            \n" +
                "FILTER                                         \n" +
                "    T1[temp < 10] AND (T2[temp > 50]           \n" +
                "    OR                                         \n" +
                "    H[income > 2 * cost])"
        );
        System.out.println("Done!");
    }
}
