package com.rpg.main.math.vector;

public class Matrix4 {
    private final float[][] matrix;

    public Matrix4() {
        matrix = new float[4][4];
    }

    public Matrix4(float[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4)
            throw new IllegalArgumentException("Inputted Matrix must be a 4x4!");
        this.matrix = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 4);
        }
    }

    public void setRow(int n, float[] row) {
        if (row.length != 4)
            throw new IllegalArgumentException("The provided array must have exactly 4 elements!");
        System.arraycopy(row, 0, matrix[n], 0, 4);
    }

    public void setColumn(int n, float[] column) {
        if (column.length != 4)
            throw new IllegalArgumentException("The provided array must have exactly 4 elements!");
        for (int i = 0; i < 4; i++)
            matrix[i][n] = column[i];
    }

    public void setValue(int row, int column, float value) {
        matrix[row][column] = value;
    }

    public void scaleRow(int row, float scale) {
        for (int i = 0; i < 4; i++)
            matrix[row][i] *= scale;
    }

    public void scaleColumn(int column, float scale) {
        for (int i = 0; i < 4; i++)
            matrix[i][column] *= scale;
    }

    public void scaleMatrix(float scale) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] *= scale;
            }
        }
    }

    public Matrix4 transpose() {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix4(result);
    }

    public Vector4 mul(Vector4 vec) {
        return new Vector4(
                matrix[0][0] * vec.x + matrix[0][1] * vec.y + matrix[0][2] * vec.z + matrix[0][3] * vec.w,
                matrix[1][0] * vec.x + matrix[1][1] * vec.y + matrix[1][2] * vec.z + matrix[1][3] * vec.w,
                matrix[2][0] * vec.x + matrix[2][1] * vec.y + matrix[2][2] * vec.z + matrix[2][3] * vec.w,
                matrix[3][0] * vec.x + matrix[3][1] * vec.y + matrix[3][2] * vec.z + matrix[3][3] * vec.w
        );
    }
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
        System.out.println("w = " + w);
        return new Vector3(x, y, z);
    }

    public Matrix4 mul(Matrix4 mat) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = matrix[i][0] * mat.matrix[0][j] + matrix[i][1] * mat.matrix[1][j] + matrix[i][2] * mat.matrix[2][j] + matrix[i][3] * mat.matrix[3][j];
            }
        }
        return new Matrix4(result);
    }

    public static Matrix4 identity() {
        Matrix4 identity = new Matrix4();
        for (int i = 0; i < 4; i++) {
            identity.matrix[i][i] = 1.0f;
        }
        return identity;
    }

    public float determinant() {
        float det = 0;
        for (int i = 0; i < 4; i++) {
            det += (i % 2 == 0 ? 1 : -1) * matrix[0][i] * cofactor(0, i);
        }
        return det;
    }

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

    private float minorDeterminant3(float[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
                - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
                + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }

    public Matrix4 inverse() {
        float det = determinant();
        if (det == 0) throw new ArithmeticException("Matrix is singular and cannot be inverted.");
        float[][] inv = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                inv[j][i] = cofactor(i, j) / det;
            }
        }
        return new Matrix4(inv);
    }

    public float trace() {
        return matrix[0][0] + matrix[1][1] + matrix[2][2] + matrix[3][3];
    }

    public static Matrix4 perspective(float fov, float aspect, float near, float far) {
        float f = 1.0f / (float) Math.tan(Math.toRadians(fov) / 2);
        float range = near - far;
        return new Matrix4(new float[][]{
                {f / aspect, 0, 0, 0},
                {0,f,0,0},
                {0,0,(far + near) / range, 2 * far * near / range},
                {0,0,-1,0}
        });
    }

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

    public float[] getRow(int row) {
        return new float[]{matrix[row][0], matrix[row][1], matrix[row][2], matrix[row][3]};
    }

    public float[] getColumn(int column) {
        return new float[]{matrix[0][column], matrix[1][column], matrix[2][column], matrix[3][column]};
    }

    public float getValue(int row, int column) {
        return matrix[row][column];
    }

    public Matrix4 getCopy() {
        return new Matrix4(matrix);
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f, %.2f]",
                matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3],
                matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3],
                matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3],
                matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3]);
    }
}
