package com.rpg.main.graphics;

import com.rpg.main.math.vector.Vector2;
import com.rpg.main.util.Time;

public class Camera {
    //Vectors for the cameras position and scale
    private Vector2 position;
    private Vector2 scale;

    private Vector2 test;

    //Camera movement logic
    private Vector2 minClamp,maxClamp;

    /**
     * Creates a new camera object at the inputted position and scale.
     * @param position (Vector2) The position to place the camera at.
     * @param scale (Vector2) The scale to have the camera at.
     */
    public Camera(Vector2 position, Vector2 scale) {
        this.position = position;
        this.scale = scale;
    }

    /**
     * Creates a new camera object at the inputted position with a scale of (1,1).
     * @param position (Vector2) The position to place the camera at.
     */
    public Camera(Vector2 position) {
        this(position,new Vector2(1,1));
    }

    /**
     * Creates a new camera at position (0,0) with scale of (1,1).
     */
    public Camera() {
        this(new Vector2(),new Vector2(1,1));
    }

    /**
     * Updates the camera's information every frame.
     */
    public void update() {
        if(test==null) return;

        // Calculate the direction vector
        Vector2 direction = test.sub(position);

        // Get the distance between the camera and player
        float distance = direction.mag();

        // Calculate the speed based on the distance (faster as further away)
        float speed = Math.min(30, distance*0.25f); // Use a cap to avoid too fast movement

        // Normalize the direction to have a unit vector
        if(direction.mag()!=0)
            direction = direction.norm();

        // Move the camera smoothly using linear interpolation (lerp)
        position = position.add(direction.scale((float)(speed * Time.deltaTime)));
    }

    public void setTest(Vector2 test) {
        this.test = test;
    }

    /**
     * Translates the camera's position by a given offset.
     * @param offset (Vector2) The offset to move the camera by.
     */
    public void translate(Vector2 offset) {
        position = position.add(offset);
    }

    /**
     * Scales the camera by a specified factor.
     * @param scaleFactor (float) The factor by which to scale the camera.
     */
    public void zoom(float scaleFactor) {
        scale = scale.scale(scaleFactor);
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
        this.scale = scale;
    }

    /**
     * Grabs the camera's current scale.
     * @return (Vector2) The current scale of the camera.
     */
    public Vector2 getScale() {
        return scale;
    }
}
