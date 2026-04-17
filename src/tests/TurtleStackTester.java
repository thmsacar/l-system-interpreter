package tests;

import turtle.Turtle2D;

import java.util.Random;

/**
 * Class to test push/pop operations of Turtle.
 */
public class TurtleStackTester {
    public static void main(String[] args) {
        testPushPop();
    }

    public static void testPushPop() {
        System.out.println("Testing Turtle Push/Pop...");

        Turtle2D turtle = new Turtle2D(100, 100, 90);

        turtle.push();

        Random random = new Random();

        int x;
        for (int i = 0; i < 10; i++) {
            x = random.nextInt(3);
            switch (x){
                case 0 -> turtle.move(10);
                case 1 -> turtle.turnLeft(90);
                case 2 -> turtle.turnRight(90);
            }
        }

        turtle.pop();

        assert Math.abs(turtle.getTurtleX() - 100) < 0.001;
        assert Math.abs(turtle.getTurtleY() - 100) < 0.001;
        assert Math.abs(turtle.getAngle() - 90) < 0.001;

        turtle.stop();

        System.out.println("Turtle Stack Test passed.");
    }
}