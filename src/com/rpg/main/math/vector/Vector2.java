package com.rpg.main.math.vector;


public class Vector2 {
    //Vector array to store values in
    protected float x,y;

    /**
     * Creates a 2-Dimensional vector.
     * @param x (float) The X value of the vector.
     * @param y (float) The Y value of the vector.
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Creates a 2-Dimensional vector with values (0,0).
     */
    public Vector2() {
        this(0,0);
    }

    /**
     * Adds two vectors together and returns a new vector as a result.
     * @param v (Vector2) The inputted vector to add to the resulting vector.
     * @return (Vector2) The new combined vector.
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(x+v.x,y+v.y);
    }
    /**
     * Subtracts two vectors from each other and returns a new vector as a result.
     * @param v (Vector2) The inputted vector to subtract from the host vector.
     * @return (Vector2) The new subtracted vector.
     */
    public Vector2 sub(Vector2 v) {
       return add(v.scale(-1));
    }

    /**
     * Scales a vector by a value and returns it.
     * @param scalar (float) The value to scale the vector by.
     * @return (Vector2) The scaled vector.
     */
    public Vector2 scale(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Create the unit vector of the host vector.
     * If the magnitude of the vector is zero, this function will return an empty vector.
     * @return (Vector2) The unit vector.
     * @throws IllegalArgumentException if the vector has zero magnitude.
     */
    public Vector2 norm() {
        float mag = mag();
        if (mag == 0) {
            throw new IllegalArgumentException("Cannot normalize a zero vector.");
        }
        return new Vector2(x / mag, y / mag);
    }

    /**
     * Returns the length of the vector.
     * @return (float) The length of the vector.
     */
    public float mag() {
         return (float)Math.sqrt(x*x+y*y);
    }

    /**
     * Returns the distance between the heads of two vectors.
     * @param v (Vector2) The inputted vector to determine a distance from.
     * @return (float) The distance between the heads of the two vectors.
     */
    public float dist(Vector2 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns the dot product between two vectors.
     * @param v (Vector2) The inputted vector to determine a dot product of.
     * @return (float) The dot product between the two vectors.
     */
    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    /**
     * Returns the angle in radians between the head of this vector and another vector.
     * @param v (Vector2) The inputted vector to calculate the angle between.
     * @return (float) The angle in radians.
     */
    public float angle(Vector2 v) {
        return (float) Math.acos(dot(v) / mag()*v.mag());
    }

    /**
     * Linearly interpolates between this vector and another vector.
     * @param v (Vector) The target vector.
     * @param t (float) A value between 0 and 1 indicating the interpolation factor.
     * @return (Vector2) The interpolated vector.
     */
    public Vector2 lerp(Vector2 v, float t) {
        return new Vector2(x + t * (v.x - x), y + t * (v.y - y));
    }

    /**
     * Reflects this vector across a given normal vector.
     * @param v (Vector2) The normal vector to reflect off of.
     * @return (Vector2) The reflected vector.
     */
    public Vector2 reflect(Vector2 v) {
        float dotProd = dot(v);
        return sub(v.scale(2 * dotProd));
    }

    /**
     * Projects this vector onto another vector.
     * @param v (Vector2) The vector to project onto.
     * @return (Vector2) The projected vector.
     */
    public Vector2 project(Vector2 v) {
        float dotProd = dot(v);
        float magnitudeSquared = v.x * v.x + v.y * v.y;
        float scalar = dotProd / magnitudeSquared;
        return v.scale(scalar);
    }

    /**
     * Returns a new vector perpendicular to the host vector.
     * @return (Vector2) A perpendicular vector.
     */
    public Vector2 perp() {
        return new Vector2(-getY(),getX());
    }

    /**
     * Negates this vector (inverts its direction).
     * @return (Vector2) The negated vector.
     */
    public Vector2 negate() {
        return new Vector2(-x,-y);
    }

    /**
     * Returns the x value of the vector.
     * @return (float) x value of the vector
     */
    public float getX() {
        return x;
    }
    /**
     * Returns the y value of the vector.
     * @return (float) y value of the vector
     */
    public float getY() {
        return y;
    }
    /**
     * Sets the x value of the vector.
     * @param x (float) new x value of the vector.
     */
    public void setX(float x) {
        this.x = x;
    }
    /**
     * Sets the y value of the vector.
     * @param y (float) new y value of the vector.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Copies the parent vector.
     * @return (Vector2) A copy of the parent vector.
     */
    public Vector2 copy() {
        return new Vector2(x,y);
    }

    /**
     * Returns the entire vector as a string
     * @return (String) The vector data as a string.
     */
    public String toString() {
        return "["+x+","+y+"]";
    }
}
