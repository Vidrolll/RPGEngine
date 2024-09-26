package com.rpg.main.math;


public class Vector {
    //Vector array to store values in
    protected float[] vec;

    /**
     * Creates an n-dimensional empty vector.
     * @param dimensions The number of dimensions the vector should be.
     */
    public Vector(int dimensions) {
        vec = new float[dimensions];
    }
    /**
     * Creates a vector out of an array of values. The dimensions of the vector is equal to the amount of entries in the list.
     * @param values The array to create a vector out of
     */
    public Vector(float...values) {
        vec = new float[values.length];
        System.arraycopy(values, 0, vec, 0, values.length);
    }

    /**
     * Adds two vectors together and returns a new vector as a result.
     * Note: If the 2 vectors are of different dimensions the returned vector will be the size of the biggest inputted vector. <br>
     * Ex: [0,12,3,3,8] + [3,2,6] would result in a vector [3,14,9,3,8]
     * @param v (Vector) The inputted vector to add to the resulting vector.
     * @return (Vector) The new combined vector.
     */
    public Vector add(Vector v) {
        Vector newVec = new Vector(Math.max(vec.length,v.vec.length));
        for(int i = 0; i < newVec.vec.length; i++) {
            float num1 = (i>=vec.length) ? 0 : getValue(i);
            float num2 = (i>=v.vec.length) ? 0 : v.getValue(i);
            newVec.setValue(i,num1+num2);
        }
        return newVec;
    }
    /**
     * Subtracts two vectors from each other and returns a new vector as a result.
     * Note: If the 2 vectors are of different dimensions the returned vector will be the size of the biggest inputted vector. <br>
     * Ex: [0,12,3,3,8] - [3,2,6] would result in a vector [-3,10,-3,3,8]
     * @param v (Vector) The inputted vector to subtract from the host vector.
     * @return (Vector) The new subtracted vector.
     */
    public Vector sub(Vector v) {
       return add(v.scale(-1));
    }
    /**
     * Multiplies two vectors together and returns the result.
     * Note: If the 2 vectors are of different dimensions the resulting vector will be the same dimensions as the smallest vector. <br>
     * Ex: [0,12,3,3,8] * [3,2,6] would result in a vector [0, 24, 18]
     * @param v (Vector) The inputted vector to multiply to the host vector.
     * @return (Vector) The new multiplied vector.
     */
    public Vector mul(Vector v) {
        Vector newVec = new Vector(Math.min(vec.length,v.vec.length));
        for(int i = 0; i < newVec.vec.length; i++)
            newVec.setValue(i,getValue(i)*v.getValue(i));
        return newVec;
    }

    /**
     * Scales a vector by a value and returns it.
     * @param scale (float) The value to scale the vector by.
     * @return (Vector) The scaled vector.
     */
    public Vector scale(float scale) {
        Vector scaleVec = new Vector(vec);
        for(int i = 0; i < scaleVec.vec.length; i++)
            scaleVec.setValue(i,scaleVec.getValue(i)*scale);
        return scaleVec;
    }

    /**
     * Create the unit vector of the host vector.
     * If the magnitude of the vector is zero, this function will return an empty vector.
     * @return (Vector) The unit vector.
     */
    public Vector norm() {
        if(mag()==0) return new Vector(0,0);
        return scale(1.0f/mag());
    }

    /**
     * Returns the length of the vector.
     * @return (float) The length of the vector.
     */
    public float mag() {
        float mag = 0;
        for(float val : vec)
            mag += val*val;
        return (float)Math.sqrt(mag);
    }

    /**
     * Returns the distance between the heads of two vectors. <br>
     * If the vectors are of different dimensions, it only utilizes the dimensions of the smallest vector.
     * @param v (Vector) The inputted vector to determine a distance from.
     * @return (float) The distance between the heads of the two vectors.
     */
    public float dist(Vector v) {
        float mag = 0;
        for(int i = 0; i < Math.min(v.vec.length,vec.length); i++)
            mag += (vec[i]-v.vec[i])*(vec[i]-v.vec[i]);
        return (float)Math.sqrt(mag);
    }

    /**
     * Returns the dot product between two vectors. <br>
     * If both vectors are of different dimensions, only the dimensions of the smaller vector will be used.
     * @param v (Vector) The inputted vector to determine a dot product of.
     * @return (float) The dot product between the two vectors.
     */
    public float dot(Vector v) {
        float dot = 0;
        for(int i = 0; i < Math.min(v.vec.length,vec.length); i++)
            dot += v.vec[i] * vec[i];
        return dot;
    }

    /**
     * Sets a given value at a given index.
     * @param index (int) The index to change.
     * @param value (float) The value to change to.
     */
    public void setValue(int index, float value) {
        vec[index] = value;
    }
    /**
     * Returns a value in the vector.
     * @param value (int) The index of the value to grab.
     * @return (float) A value in the vector.
     */
    public float getValue(int value) {
        return vec[value];
    }

    //2-Dimensional Vector Functions

    /**
     * Returns a new vector perpendicular to the host vector. <br>
     * Note this function only works correctly for 2D vectors and should not be used otherwise.
     * @return (Vector) A perpendicular vector.
     */
    public Vector perp() {
        return new Vector(-getY(),getX());
    }
    /**
     * Returns the x value of the vector. <br>
     * Note this function only works correctly for 2D vectors and should not be used otherwise.
     * @return (float) x value of the vector
     */
    public float getX() {
        return vec[0];
    }
    /**
     * Returns the y value of the vector. <br>
     * Note this function only works correctly for 2D vectors and should not be used otherwise.
     * @return (float) y value of the vector
     */
    public float getY() {
        return vec[1];
    }
    /**
     * Sets the x value of the vector. <br>
     * Note this function only works correctly for 2D vectors and should not be used otherwise.
     * @param x (float) new x value of the vector.
     */
    public void setX(float x) {
        vec[0] = x;
    }
    /**
     * Sets the y value of the vector. <br>
     * Note this function only works correctly for 2D vectors and should not be used otherwise.
     * @param y (float) new y value of the vector.
     */
    public void setY(float y) {
        vec[1] = y;
    }

    /**
     * Converts the parent vector to a single rowed matrix.
     * @return (Matrix) The converted vector.
     */
    public Matrix toMat() {
        Matrix mat = new Matrix(1,vec.length);
        for(int i = 0; i < vec.length; i++)
            mat.setValue(0,i,vec[i]);
        return mat;
    }

    /**
     * Copies the parent vector.
     * @return (Vector) A copy of the parent vector.
     */
    public Vector copy() {
        Vector vec = new Vector(this.vec.length);
        for(int i = 0; i < this.vec.length; i++) {
            vec.setValue(i,this.vec[i]);
        }
        return vec;
    }

    /**
     * Prints the entire vector out
     */
    public void print() {
        System.out.print("["+vec[0]);
        for(int i = 1; i < vec.length; i++) {
            System.out.print(","+vec[i]);
        }
        System.out.println("]");
    }
}
