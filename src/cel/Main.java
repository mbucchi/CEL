package cel;


import cel.runtime.CELMotor;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CELMotor motor = new CELMotor();
        motor.executeFromFile(args[0]);
    }

}
