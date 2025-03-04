package com.rpg.main.math.vector;

public class Matrix2 {
    // A 2-dimensional array to hold the matrix data
    private final float[][] matrix;

    /** Creates a new 2 * 2 matrix. */
    public Matrix2() {
        matrix = new float[2][2];
    }

    /**
     * Creates a matrix using a predefined 2-dimensional array.
     * @param matrix (float[][]) A predefined 2-dimensional float array to define the matrix to.
     * @throws IllegalArgumentException If the inputted matrix is NOT a 2*2 matrix.
     */
    public Matrix2(float[][] matrix) {
        if (matrix.length != 2 || matrix[0].length != 2)
            throw new IllegalArgumentException("Inputted Matrix must be a 2x2!");
        this.matrix = new float[2][2];
        for (int i = 0; i < 2; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 2);
        }
    }

    /**
     * Sets an entire row to a predefined array.
     * @param n (int) The chosen row to define.
     * @param row (float[]) The array to set row "n" to.
     * @throws IllegalArgumentException If the inputted row does not match the matrix row size.
     */
    public void setRow(int n, float[] row) {
        if (row.length != 2)
            throw new IllegalArgumentException("The provided array must have exactly 2 elements!");
        System.arraycopy(row, 0, matrix[n], 0, 2);
    }

    /**
     * Sets an entire column to a predefined array.
     * @param n (int) The chosen column to define.
     * @param column (float[]) The array to set column "n" to.
     * @throws IllegalArgumentException If the inputted column does not match the matrix column size.
     */
    public void setColumn(int n, float[] column) {
        if (column.length != 2)
            throw new IllegalArgumentException("The provided array must have exactly 2 elements!");
        for (int i = 0; i < 2; i++)
            matrix[i][n] = column[i];
    }

    /**
     * Sets a value at a given index to a given value.
     * @param row The row of the index to change.
     * @param column The column of the index to change.
     * @param value The value to set the given index to.
     */
    public void setValue(int row, int column, float value) {
        matrix[row][column] = value;
    }

    /**
     * Scales every value in a matrix row by the inputted scale value.
     * @param row (int) The chosen row to modify.
     * @param scale (float) The scale quantity.
     */
    public void scaleRow(int row, float scale) {
        for (int i = 0; i < 2; i++)
            matrix[row][i] *= scale;
    }

    /**
     * Scales every value in a matrix column by the inputted scale value.
     * @param column (int) The chosen column to modify.
     * @param scale (float) The scale quantity.
     */
    public void scaleColumn(int column, float scale) {
        for (int i = 0; i < 2; i++)
            matrix[i][column] *= scale;
    }

    /**
     * Scales an entire matrix by the inputted scale value.
     * @param scale (float) The scale value.
     */
    public void scaleMatrix(float scale) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrix[i][j] *= scale;
            }
        }
    }

    /**
     * Performs matrix multiplication on a given vector.
     * @param vec (Vector2) The vector to multiply.
     * @return (Vector2) The multiplied vector.
     */
    public Vector2 mul(Vector2 vec) {
        return new Vector2(
                matrix[0][0] * vec.x + matrix[0][1] * vec.y,
                matrix[1][0] * vec.x + matrix[1][1] * vec.y
        );
    }

    /**
     * Performs matrix multiplication on another matrix.
     * @param mat (Matrix2) The matrix to multiply by.
     * @return (Matrix2) The multiplied matrix.
     */
    public Matrix2 mul(Matrix2 mat) {
        float[][] result = new float[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i][j] = matrix[i][0] * mat.matrix[0][j] + matrix[i][1] * mat.matrix[1][j];
            }
        }
        return new Matrix2(result);
    }

    /**
     * Grabs the data of a row of the matrix and returns it as a 1-dimensional array.
     * @param row (int) The row you want to grab.
     * @return (float[]) The returned row data.
     */
    public float[] getRow(int row) {
        return new float[]{matrix[row][0], matrix[row][1]};
    }

    /**
     * Grabs the data of a column of the matrix and returns it as a 1-dimensional array.
     * @param column (int) The column you want to grab.
     * @return (float[]) The returned column data.
     */
    public float[] getColumn(int column) {
        return new float[]{matrix[0][column], matrix[1][column]};
    }

    /**
     * Grabs the data at an inputted index.
     * @param row (int) The inputted row index.
     * @param column (int) The inputted column index.
     * @return (float) The data stored at the inputted index.
     */
    public float getValue(int row, int column) {
        return matrix[row][column];
    }

    /**
     * Creates a copy of the parent matrix.
     * @return (Matrix2) A copy of the parent matrix.
     */
    public Matrix2 getCopy() {
        return new Matrix2(matrix);
    }

    /**
     * Returns a string representation of the matrix.
     * @return (String) The matrix formatted as a string.
     */
    @Override
    public String toString() {
        return String.format("[%.2f, %.2f]\n[%.2f, %.2f]", matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]);
    }
}
