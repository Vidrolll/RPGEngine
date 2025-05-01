package com.rpg.main.math.vector;

public class Vector4 {
    // Vector components
    protected float x, y, z, w;

    /**
     * Creates a 3-Dimensional vector.
     * @param x (float) The X value of the vector.
     * @param y (float) The Y value of the vector.
     * @param z (float) The Z value of the vector.
     */
    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Creates a 3-Dimensional vector with values (0,0,0).
     */
    public Vector4() {
        this(0, 0, 0, 0);
    }

    /**
     * Adds two vectors together and returns a new vector as a result.
     */
    public Vector4 add(Vector4 v) {
        return new Vector4(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    /**
     * Subtracts two vectors and returns a new vector as a result.
     */
    public Vector4 sub(Vector4 v) {
        return add(v.scale(-1));
    }

    /**
     * Scales a vector by a value and returns it.
     */
    public Vector4 scale(float scalar) {
        return new Vector4(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    /**
     * Returns the magnitude (length) of the vector.
     */
    public float mag() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    /**
     * Normalizes the vector (converts it to a unit vector).
     * @throws IllegalArgumentException if the vector has zero magnitude.
     */
    public Vector4 norm() {
        float mag = mag();
        if (mag == 0) {
            throw new IllegalArgumentException("Cannot normalize a zero vector.");
        }
        return new Vector4(x / mag, y / mag, z / mag, w / mag);
    }

    /**
     * Returns the dot product between two vectors.
     */
    public float dot(Vector4 v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    /**
     * Returns the distance between the heads of two vectors.
     */
    public float dist(Vector4 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        float dw = w - v.w;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }

    /**
     * Linearly interpolates between this vector and another vector.
     */
    public Vector4 lerp(Vector4 v, float t) {
        return new Vector4(
                x + t * (v.x - x),
                y + t * (v.y - y),
                z + t * (v.z - z),
                w + t * (v.w - w)
        );
    }

    /**
     * Negates this vector (inverts its direction).
     */
    public Vector4 negate() {
        return new Vector4(-x, -y, -z, -w);
    }

    /**
     * Returns the x value of the vector.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y value of the vector.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the z value of the vector.
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the w value of the vector.
     */
    public float getW() {
        return w;
    }

    /**
     * Sets the x value of the vector.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y value of the vector.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the z value of the vector.
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Sets the w value of the vector.
     */
    public void setW(float w) {
        this.w = w;
    }

    /**
     * Copies the parent vector.
     */
    public Vector4 copy() {
        return new Vector4(x, y, z, w);
    }

    /**
     * Returns the entire vector as a string.
     */
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "," + w + "]";
    }
}
