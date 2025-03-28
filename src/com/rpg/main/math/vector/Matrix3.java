package com.rpg.main.math.vector;

/**
 * A class representing a 3x3 matrix and supporting various matrix operations.
 */
public class Matrix3 {
    private final float[][] matrix;

    /**
     * Constructs an empty 3x3 matrix initialized to all zeros.
     */
    public Matrix3() {
        matrix = new float[3][3];
    }

    /**
     * Constructs a 3x3 matrix using the provided 2D float array.
     *
     * @param matrix (float[][]) The input matrix (must be 3x3).
     * @throws IllegalArgumentException if the input matrix is not 3x3.
     */
    public Matrix3(float[][] matrix) {
        if (matrix.length != 3 || matrix[0].length != 3)
            throw new IllegalArgumentException("Inputted Matrix must be a 3x3!");
        this.matrix = new float[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 3);
        }
    }

    /**
     * Sets a specific row of the matrix.
     * @param n (int) The row index (0-2).
     * @param row (float[]) The values to set (must be of size 3).
     * @throws IllegalArgumentException if the input array size is not 3.
     */
    public void setRow(int n, float[] row) {
        if (row.length != 3)
            throw new IllegalArgumentException("The provided array must have exactly 3 elements!");
        System.arraycopy(row, 0, matrix[n], 0, 3);
    }

    /**
     * Sets a specific column of the matrix.
     * @param n (int) The column index (0-2).
     * @param column (float[]) The values to set (must be of size 3).
     * @throws IllegalArgumentException if the input array size is not 3.
     */
    public void setColumn(int n, float[] column) {
        if (column.length != 3)
            throw new IllegalArgumentException("The provided array must have exactly 3 elements!");
        for (int i = 0; i < 3; i++)
            matrix[i][n] = column[i];
    }

    /**
     * Sets a specific value in the matrix.
     * @param row (int) The row index (0-2).
     * @param column (int) The column index (0-2).
     * @param value (float) The value to set.
     */
    public void setValue(int row, int column, float value) {
        matrix[row][column] = value;
    }

    /**
     * Scales a specific row of the matrix by a given factor.
     * @param row (int) The row index (0-2).
     * @param scale (float) The scaling factor.
     */
    public void scaleRow(int row, float scale) {
        for (int i = 0; i < 3; i++)
            matrix[row][i] *= scale;
    }

    /**
     * Scales a specific column of the matrix by a given factor.
     * @param column (int) The column index (0-2).
     * @param scale (float) The scaling factor.
     */
    public void scaleColumn(int column, float scale) {
        for (int i = 0; i < 3; i++)
            matrix[i][column] *= scale;
    }

    /**
     * Scales the entire matrix by a given factor.
     * @param scale (float) The scaling factor.
     */
    public void scaleMatrix(float scale) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] *= scale;
            }
        }
    }

    /**
     * Multiplies this matrix by a vector.
     * @param vec (Vector3) The input vector.
     * @return (Vector3) The resulting vector after matrix multiplication.
     */
    public Vector3 mul(Vector3 vec) {
        return new Vector3(
                matrix[0][0] * vec.x + matrix[0][1] * vec.y + matrix[0][2] * vec.z,
                matrix[1][0] * vec.x + matrix[1][1] * vec.y + matrix[1][2] * vec.z,
                matrix[2][0] * vec.x + matrix[2][1] * vec.y + matrix[2][2] * vec.z
        );
    }

    /**
     * Multiplies this matrix by a vector.
     *
     * @param vec (Vector2) The input vector.
     * @return (Vector2) The resulting vector after matrix multiplication.
     */
    public Vector2 mul(Vector2 vec) {
        Vector3 mv = mul(new Vector3(vec.getX(), vec.getY(), 1));
        return new Vector2(mv.getX(), mv.getY());
    }
}
