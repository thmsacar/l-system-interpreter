package turtle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;


/**
 *  An instance is a turtle in a panel within a JFrame.
 *  A turtle is a spot on the panel that you can move from place
 *  to place. All instances of Turtle use the same JFrame and
 *  panel.<br>
 *  This class represents a Turtle on plain/two dimension
 */
public class Turtle2D implements Turtle {
        /** The frame on which the turtles move. */
        private final JFrame frame;

        /** The panel in the JFrame. */
        private final JPanel panel;

        /** The panel on which the turtle moves is of
         size (width, height). */
        private final static int WIDTH = 1000;
        private final static int HEIGHT = 1000;

        /** The canvas to draw on */
        private static BufferedImage canvas;
        private final static int CANVAS_WIDTH = 5000;
        private final static int CANVAS_HEIGHT = 5000;
        private double zoomFactor = 1;
        private final double ZOOM_COEFFICIENT = 1.05;
        private double offsetX = 0;
        private double offsetY = 0;
        private Point lastMousePos;

        /** The graphics associated with the panel */
        private Graphics2D g2d;


        /** The turtle is at point (x, y). */
        private double x, y;

        /** The turtle points in direction angle: 0 <= angle < 360.
         0: east, 90:north, 180: west, 270 south. */
        private double angle;

        private Stack<State2D> stack = new Stack<>();

        /** the pen color. */
        private Color turtleColor= Color.black;

        /** Constructor: a black turtle starting at the middle of the
         panel with angle 0 (looking east).
         East (right) is angle 0; north (up), 90; west (left), 180;
         South (down). 270. The pen is down.*/
        public Turtle2D() {
                this(CANVAS_WIDTH /2.0, CANVAS_HEIGHT /2.0, 90);
        }

        /** Constructor: a black turtle starting at (x,y) with angle ang.
         East (right) is angle 0; north (up), 90; west (left), 180;
         South (down), 270. The pen is down. */
        public Turtle2D(double x, double y, double ang) {
                frame = new JFrame("Turtle2D");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                canvas = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);

                panel = createTurtlePanel();
                MouseAdapter mouseHandler = createMouseHandler();

                panel.addMouseListener(mouseHandler);
                panel.addMouseMotionListener(mouseHandler);
                panel.addMouseWheelListener(mouseHandler);

                panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                panel.setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                panel.setBackground(Color.white);

                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.pack();

                g2d = canvas.createGraphics();
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, WIDTH, HEIGHT);

                frame.setVisible(true);

                pause(100);
                clear();
                pause(100);

                this.x = x;
                this.y = y;
                angle = ang;
        }


        /**
         * This panel was essentially designed to reprint BufferedImage onto panel in case of a resizing
         * It also handles zoom and panning
         * @return JPanel object designed to painting canvas (BufferedImage) as desired when necessary
         */
        private JPanel createTurtlePanel() {
                return new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                                super.paintComponent(g);
                                Graphics2D g2 = (Graphics2D) g;

                                //Some hopeful attempts to get rid of ugly lines when zoomed too much :D
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                                //Panning to desired point
                                g2.translate(offsetX, offsetY);

                                //Using pivot zoom method
                                double pivotX = getWidth() / 2.0;
                                double pivotY = getHeight() / 2.0;
                                g2.translate(pivotX, pivotY);     //Go to center
                                g2.scale(zoomFactor, zoomFactor); // Zoom
                                g2.translate(-pivotX, -pivotY);   // Come back

                                //Putting the center of canvas to center of panel
                                double startX = (getWidth() - canvas.getWidth()) / 2.0;
                                double startY = (getHeight() - canvas.getHeight()) / 2.0;


                                if (canvas != null) {
                                        g2.drawImage(canvas, (int)startX, (int)startY, null);
                                }
                        }
                };
        }

        /**
         * @return MouseAdapter object used for zooming and panning features
         */
        private MouseAdapter createMouseHandler() {
                return new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                                lastMousePos = e.getPoint();
                        }

                        @Override
                        public void mouseDragged(MouseEvent e) {
                                if (lastMousePos != null) {
                                        // Calculate how much the mouse has moved (Delta)
                                        double dx = e.getX() - lastMousePos.x;
                                        double dy = e.getY() - lastMousePos.y;

                                        // Add to current offset  (pan)
                                        offsetX += dx;
                                        offsetY += dy;

                                        lastMousePos = e.getPoint();
                                        panel.repaint();
                                }
                        }

                        @Override
                        public void mouseWheelMoved(MouseWheelEvent e) {
                                double rotation = e.getPreciseWheelRotation();

                                if (rotation < 0) {
                                        zoomIn();
                                } else {
                                        zoomOut();
                                }
                                //Zoom factor is limited [0.01, 100]
                                zoomFactor = Math.max(0.01, Math.min(zoomFactor, 100.0));
                                panel.repaint();
                        }
                };
        }


        /**
         * Gets x position of turtle
         * @return x-coordinate of the turtle. */
        public double getTurtleX() {
                return x;
        }

        /**
         * Gets y position of turtle
         * @return y-coordinate of the panel. */
        public double getTurtleY() {
                return y;
        }

        /**
         * Gets current angle in degrees
         * @return angle of the turtle (in degrees). East (right) is angle 0;
         north (up), 90; west (left), 180; South (down), 270. */
        public double getAngle() {
                return angle;
        }

        /**
         * Gets width of panel
         * @return width of panel. */
        public int getWidth() {
                return WIDTH;
        }

        /**
         * Gets height of panel
         * @return height of the panel. */
        public int getHeight() {
                return HEIGHT;
        }


        /** Set the angle to ang degrees. East (right) is angle 0;
         north (up), 90; west (left), 180; South (down), 270.*/
        public void setAngle(double ang) {
                angle= mod(ang, 360);
        }

        /** Add ang degrees to the angle. */
        public void addAngle(double ang) {
                angle= mod (angle + ang, 360);
        }


        /**
         * Move the turtle to (x,y), without drawing, and face it at angle ang
         * @param x coordinate x
         * @param y coordinate y
         * @param ang angle (0 is east)
         */
        public void moveTo(double x, double y, double ang) {
                Color save= g2d.getColor();
                g2d.setColor(turtleColor);
                this.x= x;
                this.y= y;
                angle= ang;
                g2d.setColor(save);
        }

        /**
         * Move the turtle d units in its current direction.
         * If the pen is down, a line will be drawn; if the pen
         * is up, it won't be drawn.
         * @param d distance to move
         */
        public void move(double d) {
                //Angle is converted to radian
                double rAngle= (angle * Math.PI) / 180;
                double newX= x + Math.cos(rAngle) * d;
                double newY= y - Math.sin(rAngle) * d;
                Color save= g2d.getColor();
                g2d.setColor(turtleColor);
                g2d.drawLine((int)Math.round(x), (int)Math.round(y),
                        (int)Math.round(newX), (int)Math.round(newY));
                g2d.setColor(save);
                panel.repaint();
                x= newX;
                y= newY;
        }


        /**
         * This method does a special mod operation to find result between 0 and y. The result is never negative.
         * @param x
         * @param y
         * @return x mod y
         * @throws IllegalArgumentException
         */
        private double mod(double x, double y) throws IllegalArgumentException {
                if (y==0) throw new IllegalArgumentException("Remainder operation with 0 is not allowed");
                double ans= x % y;
                if (ans < 0)
                        ans= ans + y;
                return ans;
        }

        /** Clear the screen (make it all white). */
        public void clear() {
                Color c= g2d.getColor();
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, WIDTH, HEIGHT);
                g2d.setColor(c);
        }

        /** Pause the program for msec milliseconds. */
        public void pause(int msec) {
                try { Thread.sleep(msec); }
                catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                }
        }

        public void turnLeft(double ang) {
                addAngle(ang);
        }

        public void turnRight(double ang) {
                addAngle(-1*ang);
        }

        /**
         * Zoom in to the panel
         */
        public void zoomIn(){
                zoomFactor *= ZOOM_COEFFICIENT;
        }

        /**
         * Zoom out from the panel
         */
        public void zoomOut(){
                zoomFactor /= ZOOM_COEFFICIENT;
        }

        public void push(){
                stack.push(new State2D(this.x, this.y, this.angle));
        }

        public void pop(){
                State2D pop= stack.pop();
                moveTo(pop.getX(), pop.getY(), pop.getAngle());
        }

        public void stop(){
                this.frame.setVisible(false);
                this.frame.dispose();
        }




}
