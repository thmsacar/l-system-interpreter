package turtle;

/**
 * An interface that generalizes Turtle Graphics functions
 */
public interface Turtle {

    /**
     * Turns turtle left by given degrees. Rotation on Z axis
     * @param angle degrees between 0 and 360
     */
    void turnLeft(double angle);

    /**
     * Turns turtle right by given degrees. Rotation on Z axis
     * @param angle degrees between 0 and 360
     */
    void turnRight(double angle);

    /**
     * Pushes the current position and direction to stack
     */
    void push();

    /**
     * Pops the last stacked position and direction. This position and direction will be the current position and direction of turtle.
     */
    void pop();

    /**
     * Moves by distance towards current direction
     * @param distance distance to move
     */
    void move(double distance);

    /**
     * Pitch turtle down by given degrees. Rotation on X axis
     * @param angle degrees between 0 and 360
     */
    default void pitchDown(double angle){};

    /**
     * Pitch turtle up by given degrees. Rotation on X axis
     * @param angle degrees between 0 and 360
     */
    default void pitchUp(double angle){};

    /**
     * Roll turtle left by given degrees. Rotation on Y axis
     * @param angle degrees between 0 and 360
     */
    default void rollLeft(double angle){};

    /**
     * Roll turtle right by given degrees. Rotation on Y axis
     * @param angle degrees between 0 and 360
     */
    default void rollRight(double angle){};

    /**
     * Stop the execution and delete the frame
     */
    void stop();

}
