package cel;

import cel.runtime.CELMotor;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        long time = System.nanoTime();
        CELMotor motor = new CELMotor();
        motor.executeFromFile(args[0]);
//        System.out.println("Total Time: " + ((double) (System.nanoTime() - time) / 1000000000));
    }
}
