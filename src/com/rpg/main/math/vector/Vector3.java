package com.rpg.main.math.vector;

public class Vector3 {
    // Vector components
    protected float x, y, z;

    /**
     * Creates a 3-Dimensional vector.
     * @param x (float) The X value of the vector.
     * @param y (float) The Y value of the vector.
     * @param z (float) The Z value of the vector.
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a 3-Dimensional vector with values (0,0,0).
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Adds two vectors together and returns a new vector as a result.
     */
    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Subtracts two vectors and returns a new vector as a result.
     */
    public Vector3 sub(Vector3 v) {
        return add(v.scale(-1));
    }

    /**
     * Scales a vector by a value and returns it.
     */
    public Vector3 scale(float scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Returns the magnitude (length) of the vector.
     */
    public float mag() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes the vector (converts it to a unit vector).
     * @throws IllegalArgumentException if the vector has zero magnitude.
     */
    public Vector3 norm() {
        float mag = mag();
        if (mag == 0) {
            throw new IllegalArgumentException("Cannot normalize a zero vector.");
        }
        return new Vector3(x / mag, y / mag, z / mag);
    }

    /**
     * Returns the dot product between two vectors.
     */
    public float dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Returns the cross product between two vectors.
     */
    public Vector3 cross(Vector3 v) {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    /**
     * Returns the distance between the heads of two vectors.
     */
    public float dist(Vector3 v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Linearly interpolates between this vector and another vector.
     */
    public Vector3 lerp(Vector3 v, float t) {
        return new Vector3(
                x + t * (v.x - x),
                y + t * (v.y - y),
                z + t * (v.z - z)
        );
    }

    /**
     * Negates this vector (inverts its direction).
     */
    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    /**
     * Rotates the vector around an arbitrary axis by a given angle using Rodrigues' rotation formula.
     * @param axis (Vector3) The axis to rotate the vector around (must be a unit vector).
     * @param angle (float) The angle to rotate the vector by (in degrees).
     * @return (Vector3) The rotated vector.
     */
    public Vector3 rotateAroundAxis(Vector3 axis, float angle) {
        // Convert angle to radians
        float rad = (float) Math.toRadians(angle);

        // Normalize the axis to make sure it's a unit vector
        Vector3 unitAxis = axis.norm();

        // Calculate the components of Rodrigues' rotation formula
        Vector3 term1 = this.scale((float) Math.cos(rad)); // v * cos(θ)
        Vector3 term2 = unitAxis.cross(this).scale((float) Math.sin(rad)); // (k x v) * sin(θ)
        Vector3 term3 = unitAxis.scale(unitAxis.dot(this) * (1 - (float) Math.cos(rad))); // k * (k · v) * (1 - cos(θ))

        // Add all terms to get the rotated vector
        return term1.add(term2).add(term3);
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
     * Copies the parent vector.
     */
    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    /**
     * Returns the entire vector as a string.
     */
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
