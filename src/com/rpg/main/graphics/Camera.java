package com.rpg.main.graphics;

import com.rpg.main.entity.Entity;
import com.rpg.main.graphics.opengl.Renderer;
import com.rpg.main.math.vector.Matrix4;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.util.Time;

public class Camera {
    //Vectors for the cameras position and scale
    private Vector2 position;
    private Vector2 scale;
    private Vector2 scalePos;
    private float rotationAngle = 0f;

    private Entity lockOn;

    //Camera movement logic
    private Vector2 minClamp,maxClamp;

    /**
     * Creates a new camera object at the inputted position and scale and scale position.
     * @param position (Vector2) The position to place the camera at.
     * @param scale (Vector2) The scale to have the camera at.
     * @param scalePos (Vector2) The position to scale the camera at.
     */
    public Camera(Vector2 position, Vector2 scale, Vector2 scalePos) {
        this.position = position;
        this.scale = scale;
        this.scalePos = scalePos;
    }

    /**
     * Creates a new camera object at the inputted position with a scale of (1,1) and a scale position of (0,0).
     * @param position (Vector2) The position to place the camera at.
     */
    public Camera(Vector2 position) {
        this(position, new Vector2(1, 1), new Vector2(0, 0));
    }

    /**
     * Creates a new camera at position (0,0) with scale of (1,1) and a scale position of (0,0).
     */
    public Camera() {
        this(new Vector2(), new Vector2(1, 1), new Vector2(0, 0));
    }

    /**
     * Updates the camera's information every frame.
     */
    public void update() {
        if (lockOn != null) {
            Vector2 direction = getScreenCenter().sub(lockOn.getPos()).sub(position);
            float distance = direction.mag();
            float speed = Math.min(30, distance * 0.25f);
            if (direction.mag() != 0)
                direction = direction.norm();
            move(direction.scale((float) (speed * Time.deltaTime)));
        }
        clampPos();
    }

    /**
     * Returns the center of the screen based off the window size.
     *
     * @return (Vector2) The center position of the screen.
     */
    public Vector2 getScreenCenter() {
        return new Vector2(Renderer.getWindow().getWidth() / 2f, Renderer.getWindow().getHeight() / 2f);
    }

    /**
     * Returns the current transformation matrix.
     *
     * @return (Matrix4) The current transformation matrix.
     */
    public Matrix4 getTransformMatrix() {
        Matrix4 scalePosMatrix = new Matrix4(new float[][]{
                {1, 0, 0, scalePos.getX()},
                {0, 1, 0, scalePos.getY()},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        Matrix4 negScalePosMatrix = new Matrix4(new float[][]{
                {1, 0, 0, -scalePos.getX()},
                {0, 1, 0, -scalePos.getY()},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        Matrix4 scaleMatrix = new Matrix4(new float[][]{
                {scale.getX(), 0, 0, 0},
                {0, scale.getY(), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        Matrix4 translationMatrix = new Matrix4(new float[][]{
                {1, 0, 0, position.getX()},
                {0, 1, 0, position.getY()},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        Matrix4 rotationMatrix = new Matrix4(new float[][]{
                {(float) Math.cos(rotationAngle), (float) -Math.sin(rotationAngle), 0, 0},
                {(float) Math.sin(rotationAngle), (float) Math.cos(rotationAngle), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return scalePosMatrix.mul(scaleMatrix).mul(negScalePosMatrix).mul(translationMatrix).mul(rotationMatrix);
    }

    /**
     * Locks the camera to follow a specific entity.
     * To unlock the camera, simply input null.
     * @param lockOn (Entity) The given entity to follow with the camera.
     */
    public void lockOn(Entity lockOn) {
        this.lockOn = lockOn;
    }

    /**
     * Scales the camera by a specified factor.
     * @param scaleFactor (float) The factor by which to scale the camera.
     */
    public void zoom(float scaleFactor) {
        scale = scale.scale(scaleFactor);
        if (scale.getX() < 0.5f) scale.setX(0.5f);
        if (scale.getY() < 0.5f) scale.setY(0.5f);
    }

    /**
     * Scales the camera by a specified factor into a specified point.
     *
     * @param scaleFactor (float) The factor by which to scale the camera.
     * @param scalePos    (Vector2) The position by which to scale the camera into.
     */
    public void zoom(float scaleFactor, Vector2 scalePos) {
        Vector2 before = getTransformMatrix().inverse().mul(scalePos);
        this.scalePos = scalePos;
        this.scale = scale.scale(scaleFactor);
        if (scale.getX() < 0.5f) scale.setX(0.5f);
        if (scale.getY() < 0.5f) scale.setY(0.5f);
        Vector2 after = getTransformMatrix().inverse().mul(scalePos);
        move(after.sub(before));
    }

    /**
     * Clamps the camera's position to stay within specified boundaries.
     */
    private void clampPos() {
        if(minClamp==null||maxClamp==null) return;
        position = new Vector2(
                Math.max(minClamp.getX(), Math.min(position.getX(), maxClamp.getX())),
                Math.max(minClamp.getY(), Math.min(position.getY(), maxClamp.getY()))
        );
    }

    /**
     * Sets the clamp positions for the camera to follow. Should be inputted as the top left coordinate,
     * and the bottom right coordinate of an imaginary rectangle that you want the camera to stay inside.
     * To unclamp the camera, simply input these values as null.
     * @param minClamp (Vector2) The top-left most coordinate the camera should be positioned at.
     * @param maxClamp (Vector2) The bottom-right most coordinate the camera should be positioned at.
     */
    public void setClamp(Vector2 minClamp, Vector2 maxClamp) {
        this.minClamp = minClamp;
        this.maxClamp = maxClamp;
    }

    /**
     * Resets the camera's scale to its default value (usually (1, 1)).
     */
    public void resetZoom() {
        scale = new Vector2(1, 1);
    }

    /**
     * Resets the camera's position and scale to default values.
     */
    public void reset() {
        position = new Vector2(0, 0);
        scale = new Vector2(1, 1);
        scalePos = new Vector2(0,0);
    }

    /**
     * Moves the camera by a specific amount along the x and y axes.
     * @param dx (float) The amount to move along the x-axis.
     * @param dy (float) The amount to move along the y-axis.
     */
    public void move(float dx, float dy) {
        position = position.add(new Vector2(dx, dy));
    }

    /**
     * Moves the camera by a specific amount along the x and y axes.
     *
     * @param move (Vector2) The amount to move along both axes in vector form.
     */
    public void move(Vector2 move) {
        move(move.getX(), move.getY());
    }

    /**
     * Sets the camera's position to a new position.
     * @param position (Vector2) The new position to set the camera at.
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Grabs the camera's current position.
     * @return (Vector2) The current position of the camera.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the camera's scale to a new value.
     * @param scale (Vector2) The new scale to set the camera to.
     */
    public void setScale(Vector2 scale) {
        if (scale.getX() < 0.5f) scale.setX(0.5f);
        if (scale.getY()<0.5f) scale.setY(0.5f);
        this.scale = scale;
    }

    /**
     * Grabs the camera's current scale position.
     *
     * @return (Vector2) The current scale position of the camera.
     */
    public Vector2 getScalePos() {
        return scalePos;
    }

    /**
     * Grabs the camera's current scale.
     * @return (Vector2) The current scale of the camera.
     */
    public Vector2 getScale() {
        return scale;
    }

    /**
     * Sets the camera's scale position to a new value.
     *
     * @param scalePos (Vector2) The new scale position to set the camera to.
     */
    public void setScalePos(Vector2 scalePos) {
        this.scalePos = scalePos;
    }
}
