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
     * @return (Matrix3) The parent matrix.
     */
    public Matrix3 scaleMatrix(float scale) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] *= scale;
            }
        }
        return this;
    }

    /**
     * Returns an inverse of the current matrix.
     *
     * @return (Matrix3) The inverse of the current matrix.
     */
    public Matrix3 invert() {
        float m00 = matrix[0][0], m01 = matrix[0][1], m02 = matrix[0][2];
        float m10 = matrix[1][0], m11 = matrix[1][1], m12 = matrix[1][2];
        float m20 = matrix[2][0], m21 = matrix[2][1], m22 = matrix[2][2];
        float det = m00 * (m11 * m22 - m12 * m21)
                - m01 * (m10 * m22 - m12 * m20)
                + m02 * (m10 * m21 - m11 * m20);
        float invDet = 1.0f / det;
        Matrix3 result = new Matrix3();
        result.matrix[0][0] = (m11 * m22 - m12 * m21) * invDet;
        result.matrix[0][1] = -(m01 * m22 - m02 * m21) * invDet;
        result.matrix[0][2] = (m01 * m12 - m02 * m11) * invDet;
        result.matrix[1][0] = -(m10 * m22 - m12 * m20) * invDet;
        result.matrix[1][1] = (m00 * m22 - m02 * m20) * invDet;
        result.matrix[1][2] = -(m00 * m12 - m02 * m10) * invDet;
        result.matrix[2][0] = (m10 * m21 - m11 * m20) * invDet;
        result.matrix[2][1] = -(m00 * m21 - m01 * m20) * invDet;
        result.matrix[2][2] = (m00 * m11 - m01 * m10) * invDet;
        return result;
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
     * @param vec (Vector2) The input vector.
     * @return (Vector2) The resulting vector after matrix multiplication.
     */
    public Vector2 mul(Vector2 vec) {
        Vector3 mv = mul(new Vector3(vec.getX(), vec.getY(), 1));
        return new Vector2(mv.getX(), mv.getY());
    }

    /**
     * Multiplies this matrix by another matrix.
     *
     * @param mat (Matrix3) The input matrix.
     * @return (Matrix3) The resulting matrix after matrix multiplication.
     */
    public Matrix3 mul(Matrix3 mat) {
        Matrix3 result = new Matrix3();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result.matrix[row][col] =
                        matrix[row][0] * mat.matrix[0][col] +
                                matrix[row][1] * mat.matrix[1][col] +
                                matrix[row][2] * mat.matrix[2][col];
            }
        }
        return result;
    }

    /**
     * Returns all the values in the matrix as a one dimensional array
     *
     * @return (float[]) The array of values.
     */
    public float[] toArray() {
        return new float[]{
                matrix[0][0], matrix[0][1], matrix[0][2],
                matrix[1][0], matrix[1][1], matrix[1][2],
                matrix[2][0], matrix[2][1], matrix[2][2]
        };
    }
}
