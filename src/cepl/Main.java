package cepl;

import cepl.runtime.CELMotor;

public class Main {

    public static void main(String[] args) {
        CELMotor motor = new CELMotor();
        motor.executeDeclaration("DECLARE EVENT T(temp int, id int)");
        motor.executeDeclaration("DECLARE EVENT H(hum int, id int)");
        motor.executeDeclaration("DECLARE STREAM S(T, H)");
        motor.executeQuery(
                "SELECT temp, id                                \n" +
                "FROM S                                         \n" +
                "WHERE T AS T1 ; H + ; T AS T2                  \n" +
                "FILTER                                         \n" +
                "    T1[temp < 0] AND T2[temp > 20 or id == 5]  \n" +
                "    OR H[hum < 50]"
        );
        System.out.println("Done!");
    }
}
