package turtle;

/**
 * A class representing state of 2D Turtle. Used mainly for push and pop actions.
 */
public class State2D {

    /** Position x of the turtle */
    private double x;

    /** Position y of the turtle */
    private double y;

    /** Heading angle of the turtle in degrees. Between 0 and 360*/
    private double angle;

    /**
     * Constructor of State2D
     * @param x position x of the turtle
     * @param y position y of the turtle
     * @param angle heading angle of the turtle in degrees. Between 0 and 360
     */
    public State2D(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    /**
     * Gets position of x the turtle
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets position of y the turtle
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets heading angle of the turtle
     * @return heading in degrees between 0 and 360
     */
    public double getAngle() {
        return angle;
    }
}
