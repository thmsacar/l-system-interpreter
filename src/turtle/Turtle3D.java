package turtle;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 *  Class for Turtle graphics using Java3D
 *  An instance is a turtle in a panel within a JFrame.
 *  A turtle is a spot on the panel that you can move from place
 *  to place. All instances of Turtle use the same JFrame and
 *  panel.
 *  This class represents a Turtle on space/three dimension
 */

public class Turtle3D implements Turtle {

    /* Frame which turtle draws on */
    private final JFrame frame;

    /* Main branch group */
    private final BranchGroup root;

    private final SimpleUniverse universe;

    /* TransformGroup to draw lines on */
    private TransformGroup mainTG;

    /* Additional TransformGroup that holds the main. Used mainly for moving camera and zooming */
    private TransformGroup viewTG;

    /* Matrix holding current position and direction of turtle */
    private Transform3D currentTransform = new Transform3D();

    /* Stack for holding positions. Mainly used for L-Systems with branches. */
    private final Stack<Transform3D> stack = new Stack<>();

    /**
     * Constructor of Turtle3D
     * Creates a turtle on three dimension and a frame with panel
     */
    public Turtle3D() {

        //JFrame and canvas
        this.frame = new JFrame("Turtle3D");
        this.frame.setSize(1000, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        this.frame.add(canvas, BorderLayout.CENTER);

        //Universe and root (Main branch)
        this.universe = new SimpleUniverse(canvas);
        this.root = new BranchGroup();
        this.root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        this.root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        //BoundingSphere
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

        // --- Mouse interaction ---


        //TransformGroup that is used for moving and zooming
        this.viewTG = new TransformGroup();
        this.viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);


        //TransformGroup under viewTG. Used for rotating.
        this.mainTG = new TransformGroup(); //That's where we add the lines.
        this.mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.mainTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        this.mainTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);


        MouseRotate mr = new MouseRotate(mainTG);
        mr.setSchedulingBounds(bounds);

        MouseTranslate mt = new MouseTranslate(viewTG);
        mt.setFactor(2.0);
        mt.setSchedulingBounds(bounds);

        MouseWheelZoom mwz = new MouseWheelZoom(viewTG);
        mwz.setFactor(5.0);
        mwz.setSchedulingBounds(bounds);

        root.addChild(viewTG);
        viewTG.addChild(mainTG);

        root.addChild(mr);
        root.addChild(mt);
        root.addChild(mwz);

        // --- Light and Camera ---
        addSceneLighting(this.root, bounds);
        this.universe.getViewingPlatform().setNominalViewingTransform();

        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup viewPlatformTG = vp.getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        //Set camera position 1000 zoomed out enough
        t3d.setTranslation(new Vector3d(0.0, 0.0, 5000.0));
        viewPlatformTG.setTransform(t3d);

        //Set clipback distance big enough for better visual experience
        View view = universe.getViewer().getView();
        view.setBackClipDistance(5000.0);

        //Adding branch and showing frame
        this.universe.addBranchGraph(this.root);
        this.frame.setVisible(true);
    }

    /**
     * Method used for setting light
     * @param bg
     * @param bounds
     */
    private void addSceneLighting(BranchGroup bg, BoundingSphere bounds) {
        //Ambient Light
        Color3f ambientColor = new Color3f(1.0f, 0.0f, 0.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        bg.addChild(ambientLightNode);

        //Directional Light
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);//Setting light
        Vector3f light1Direction = new Vector3f(-1.0f, -1.0f, -1.0f);//Setting direction
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        bg.addChild(light1);

    }

    /**
     * Move turtle by distance and draw a line
     * @param distance
     */
    @Override
    public void move(double distance) {
        //Creating a line at length 'distance' at position 0,0,0
        LineArray line = new LineArray(2, LineArray.COORDINATES);
        line.setCoordinate(0, new Point3d(0, 0, 0));
        line.setCoordinate(1, new Point3d(0, distance, 0));

        //Setting appearance of the line (color width etc.)
        Appearance app = new Appearance();
        LineAttributes lineAttr = new LineAttributes(3.0f, LineAttributes.PATTERN_SOLID, true);
        app.setLineAttributes(lineAttr);
        ColoringAttributes colorAttr = new ColoringAttributes(new Color3f(0.8f, 0.8f, 0.8f), ColoringAttributes.NICEST);
        app.setColoringAttributes(colorAttr);

        //Add shape to a new transform group. This will put the line to turtle's current position.
        TransformGroup tg = new TransformGroup(new Transform3D(currentTransform));
        tg.addChild(new Shape3D(line, app));

        //We are putting TransformGroup into BranchGroup to as our mainTG is a live scene
        BranchGroup bgWrapper = new BranchGroup();
        bgWrapper.addChild(tg);
        this.mainTG.addChild(bgWrapper);

        //Creating new translation matrix to move 'distance'
        Transform3D translation = new Transform3D();
        translation.setTranslation(new Vector3d(0, distance, 0));
        //Apply translation matrix to current position
        currentTransform.mul(translation);
    }

    /**
     * Turn left by angle
     * @param angle
     */
    @Override
    public void turnLeft(double angle) {
        Transform3D rotation = new Transform3D();
        rotation.rotZ(Math.toRadians(angle));
        currentTransform.mul(rotation);
    }

    /**
     * Turn right by angle
     * @param angle
     */
    @Override
    public void turnRight(double angle) {
        turnLeft(-angle);
    }

    /**
     * Save current position
     */
    @Override
    public void push() {
        stack.push(new Transform3D(currentTransform));
    }

    /**
     * Pop and move the last saved position without drawing a line
     */
    @Override
    public void pop() {
        if (!stack.isEmpty()) {
            currentTransform = stack.pop();
        }
    }

    @Override
    public void pitchUp(double angle) {
        // Pitch: X axis pitch up
        Transform3D rotation = new Transform3D();
        rotation.rotX(Math.toRadians(angle));
        currentTransform.mul(rotation);
    }

    @Override
    public void pitchDown(double angle) {
        // Pitch: X axis pitch down
        Transform3D rotation = new Transform3D();
        rotation.rotX(Math.toRadians(-angle));
        currentTransform.mul(rotation);
    }

    @Override
    public void rollLeft(double angle) {
        // Roll: Y axis roll left
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.toRadians(angle));
        currentTransform.mul(rotation);
    }

    @Override
    public void rollRight(double angle) {
        // Roll: Y axis roll right
        Transform3D rotation = new Transform3D();
        rotation.rotY(Math.toRadians(-angle));
        currentTransform.mul(rotation);
    }

    public void stop(){
        this.frame.setVisible(false);
        this.frame.dispose();
    }


}
