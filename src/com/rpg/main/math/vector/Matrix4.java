package com.rpg.main.math.vector;

public class Matrix4 {
    private final float[][] matrix;

    /**
     * Constructs an empty 4x4 matrix initialized to all zeros.
     */
    public Matrix4() {
        matrix = new float[4][4];
    }

    /**
     * Constructs a 4x4 matrix using the provided 2D float array.
     *
     * @param matrix (float[][]) The input matrix (must be 4x4).
     * @throws IllegalArgumentException If the input matrix is not 4x4.
     */
    public Matrix4(float[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4)
            throw new IllegalArgumentException("Inputted Matrix must be a 4x4!");
        this.matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 4);
        }
    }

    /**
     * Returns the identity matrix.
     * @return (Matrix4) The identity matrix.
     */
    public static Matrix4 identity() {
        Matrix4 identity = new Matrix4();
        for (int i = 0; i < 4; i++) {
            identity.matrix[i][i] = 1.0f;
        }
        return identity;
    }

    /**
     * Returns a perspective projection matrix.
     * @param fov (float) The field of view in degrees.
     * @param aspect (float) The aspect ratio (width/height).
     * @param near (float) The distance to the near clipping plane.
     * @param far (float) The distance to the far clipping plane.
     * @return (Matrix4) The perspective matrix.
     */
    public static Matrix4 perspective(float fov, float aspect, float near, float far) {
        float f = 1.0f / (float) Math.tan(Math.toRadians(fov) / 2);
        float range = near - far;
        return new Matrix4(new float[][]{
                {f / aspect, 0, 0, 0},
                {0, f, 0, 0},
                {0, 0, (far + near) / range, 2 * far * near / range},
                {0, 0, -1, 0}
        });
    }

    /**
     * Returns an orthographic projection matrix.
     * @param left (float) The left side of the near plane.
     * @param right (float) The right side of the near plane.
     * @param bottom (float) The bottom side of the near plane.
     * @param top (float) The top side of the near plane.
     * @param near (float) The distance to the near clipping plane.
     * @param far (float) The distance to the far clipping plane.
     * @return (Matrix4) The orthographic matrix.
     */
    public static Matrix4 orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4 result = new Matrix4();
        result.matrix[0][0] = 2 / (right - left);
        result.matrix[1][1] = 2 / (top - bottom);
        result.matrix[2][2] = -2 / (far - near);
        result.matrix[3][0] = -(right + left) / (right - left);
        result.matrix[3][1] = -(top + bottom) / (top - bottom);
        result.matrix[3][2] = -(far + near) / (far - near);
        result.matrix[3][3] = 1;
        return result;
    }

    /**
     * Sets a specific row of the matrix.
     * @param n (int) The row index (0-3).
     * @param row (float[]) The values to set (must be of size 4).
     * @throws IllegalArgumentException If the input array size is not 4.
     */
    public void setRow(int n, float[] row) {
        if (row.length != 4)
            throw new IllegalArgumentException("The provided array must have exactly 4 elements!");
        System.arraycopy(row, 0, matrix[n], 0, 4);
    }

    /**
     * Sets a specific column of the matrix.
     * @param n (int) The column index (0-3).
     * @param column (float[]) The values to set (must be of size 4).
     * @throws IllegalArgumentException If the input array size is not 4.
     */
    public void setColumn(int n, float[] column) {
        if (column.length != 4)
            throw new IllegalArgumentException("The provided array must have exactly 4 elements!");
        for (int i = 0; i < 4; i++)
            matrix[i][n] = column[i];
    }

    /**
     * Sets a specific value in the matrix.
     * @param row (int) The row index (0-3).
     * @param column (int) The column index (0-3).
     * @param value (float) The value to set.
     */
    public void setValue(int row, int column, float value) {
        matrix[row][column] = value;
    }

    /**
     * Scales a specific row of the matrix by a given factor.
     * @param row (int) The row index (0-3).
     * @param scale (float) The scaling factor.
     */
    public void scaleRow(int row, float scale) {
        for (int i = 0; i < 4; i++)
            matrix[row][i] *= scale;
    }

    /**
     * Scales a specific column of the matrix by a given factor.
     * @param column (int) The column index (0-3).
     * @param scale (float) The scaling factor.
     */
    public void scaleColumn(int column, float scale) {
        for (int i = 0; i < 4; i++)
            matrix[i][column] *= scale;
    }

    /**
     * Scales the entire matrix by a given factor.
     * @param scale (float) The scaling factor.
     */
    public void scaleMatrix(float scale) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] *= scale;
            }
        }
    }

    /**
     * Returns the transpose of the matrix.
     * @return (Matrix4) The transposed matrix.
     */
    public Matrix4 transpose() {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix4(result);
    }

    /**
     * Multiplies this matrix with a Vector4 and returns the resulting vector.
     * @param vec (Vector4) The input Vector4.
     * @return (Vector4) The resulting Vector4 after matrix multiplication.
     */
    public Vector4 mul(Vector4 vec) {
        return new Vector4(
                matrix[0][0] * vec.x + matrix[0][1] * vec.y + matrix[0][2] * vec.z + matrix[0][3] * vec.w,
                matrix[1][0] * vec.x + matrix[1][1] * vec.y + matrix[1][2] * vec.z + matrix[1][3] * vec.w,
                matrix[2][0] * vec.x + matrix[2][1] * vec.y + matrix[2][2] * vec.z + matrix[2][3] * vec.w,
                matrix[3][0] * vec.x + matrix[3][1] * vec.y + matrix[3][2] * vec.z + matrix[3][3] * vec.w
        );
    }

    /**
     * Multiplies this matrix with a Vector3 and returns the resulting vector.
     * @param v (Vector3) The input Vector3.
     * @return (Vector3) The resulting Vector3 after matrix multiplication.
     */
    public Vector3 mul(Vector3 v) {
        float x = matrix[0][0] * v.getX() + matrix[0][1] * v.getY() + matrix[0][2] * v.getZ() + matrix[0][3];
        float y = matrix[1][0] * v.getX() + matrix[1][1] * v.getY() + matrix[1][2] * v.getZ() + matrix[1][3];
        float z = matrix[2][0] * v.getX() + matrix[2][1] * v.getY() + matrix[2][2] * v.getZ() + matrix[2][3];
        float w = matrix[3][0] * v.getX() + matrix[3][1] * v.getY() + matrix[3][2] * v.getZ() + matrix[3][3];
        if (w != 0) {
            x /= w;
            y /= w;
            z /= w;
        }
        return new Vector3(x, y, z);
    }

    /**
     * Multiplies this matrix with a Vector2 and returns the resulting vector.
     *
     * @param v (Vector2) The input Vector2.
     * @return (Vector2) The resulting Vector2 after matrix multiplication.
     */
    public Vector2 mul(Vector2 v) {
        Vector3 vec = mul(new Vector3(v.getX(), v.getY(), 1));
        return new Vector2(vec.getX(), vec.getY());
    }

    /**
     * Multiplies this matrix with another matrix.
     * @param mat (Matrix4) The matrix to multiply with.
     * @return (Matrix4) The resulting matrix after multiplication.
     */
    public Matrix4 mul(Matrix4 mat) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = matrix[i][0] * mat.matrix[0][j] + matrix[i][1] * mat.matrix[1][j] + matrix[i][2] * mat.matrix[2][j] + matrix[i][3] * mat.matrix[3][j];
            }
        }
        return new Matrix4(result);
    }

    /**
     * Calculates the determinant of the matrix.
     * @return (float) The determinant of the matrix.
     */
    public float determinant() {
        float det = 0;
        for (int i = 0; i < 4; i++) {
            det += (i % 2 == 0 ? 1 : -1) * matrix[0][i] * cofactor(0, i);
        }
        return det;
    }

    /**
     * Calculates the cofactor of a specific element in the matrix.
     * @param row (int) The row index of the element.
     * @param col (int) The column index of the element.
     * @return (float) The cofactor of the specified element.
     */
    private float cofactor(int row, int col) {
        float[][] subMatrix = new float[3][3];
        for (int i = 0, subI = 0; i < 4; i++) {
            if (i == row) continue;
            for (int j = 0, subJ = 0; j < 4; j++) {
                if (j == col) continue;
                subMatrix[subI][subJ++] = matrix[i][j];
            }
            subI++;
        }
        return minorDeterminant3(subMatrix);
    }

    /**
     * Calculates the minor determinant of a 3x3 matrix.
     * @param m (float[][]) The 3x3 matrix to calculate the minor determinant of.
     * @return (float) The minor determinant of the matrix.
     */
    private float minorDeterminant3(float[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
                - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
                + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }

    /**
     * Calculates the adjugate matrix (the transpose of the cofactor matrix).
     *
     * @return (Matrix4) The adjugate matrix.
     */
    public Matrix4 adjugate() {
        float[][] adj = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                adj[j][i] = (i + j) % 2 == 0 ? cofactor(i, j) : -cofactor(i, j);
            }
        }
        return new Matrix4(adj);
    }

    /**
     * Inverts the matrix.
     * @return (Matrix4) The inverted matrix.
     * @throws ArithmeticException If the matrix is singular and cannot be inverted.
     */
    public Matrix4 inverse() {
        float det = determinant();
        if (det == 0) throw new ArithmeticException("Matrix is singular and cannot be inverted.");
        Matrix4 adjugate = adjugate();
        adjugate.scaleMatrix(1.0f / det);
        return adjugate;
    }

    /**
     * Returns the trace of the matrix, which is the sum of its diagonal elements.
     * @return (float) The trace of the matrix.
     */
    public float trace() {
        return matrix[0][0] + matrix[1][1] + matrix[2][2] + matrix[3][3];
    }

    /**
     * Returns a specific row of the matrix as a float array.
     * @param row (int) The row index (0-3).
     * @return (float[]) A float array representing the row.
     */
    public float[] getRow(int row) {
        return new float[]{matrix[row][0], matrix[row][1], matrix[row][2], matrix[row][3]};
    }

    /**
     * Returns a specific column of the matrix as a float array.
     * @param column (int) The column index (0-3).
     * @return (float[]) A float array representing the column.
     */
    public float[] getColumn(int column) {
        return new float[]{matrix[0][column], matrix[1][column], matrix[2][column], matrix[3][column]};
    }

    /**
     * Returns the value at a specific position in the matrix.
     * @param row (int) The row index (0-3).
     * @param column (int) The column index (0-3).
     * @return (float) The value at the specified position in the matrix.
     */
    public float getValue(int row, int column) {
        return matrix[row][column];
    }

    /**
     * Returns a copy of the current matrix.
     * @return (Matrix4) A new Matrix4 instance that is a copy of the current matrix.
     */
    public Matrix4 getCopy() {
        return new Matrix4(matrix);
    }

    /**
     * Returns a string representation of the matrix.
     * @return (String) A string representing the matrix in a readable format.
     */
    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]",
                matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3],
                matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3],
                matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3],
                matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3]);
    }

    /**
     * Returns all the values in the matrix as a one dimensional array
     *
     * @return (float[]) The array of values.
     */
    public float[] toArray() {
        return new float[]{
                matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0],
                matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1],
                matrix[0][2], matrix[1][2], matrix[2][2], matrix[3][2],
                matrix[0][3], matrix[1][3], matrix[2][3], matrix[3][3]
        };
    }
}
