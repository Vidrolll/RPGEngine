package com.rpg.main.math.vector;

public class Matrix3 {
    private final float[][] matrix;

    public Matrix3() {
        matrix = new float[3][3];
    }

    public Matrix3(float[][] matrix) {
        if (matrix.length != 3 || matrix[0].length != 3)
            throw new IllegalArgumentException("Inputted Matrix must be a 3x3!");
        this.matrix = new float[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 3);
        }
    }

    public void setRow(int n, float[] row) {
        if (row.length != 3)
            throw new IllegalArgumentException("The provided array must have exactly 3 elements!");
        System.arraycopy(row, 0, matrix[n], 0, 3);
    }

    public void setColumn(int n, float[] column) {
        if (column.length != 3)
            throw new IllegalArgumentException("The provided array must have exactly 3 elements!");
        for (int i = 0; i < 3; i++)
            matrix[i][n] = column[i];
    }

    public void setValue(int row, int column, float value) {
        matrix[row][column] = value;
    }

    public void scaleRow(int row, float scale) {
        for (int i = 0; i < 3; i++)
            matrix[row][i] *= scale;
    }

    public void scaleColumn(int column, float scale) {
        for (int i = 0; i < 3; i++)
            matrix[i][column] *= scale;
    }

    public void scaleMatrix(float scale) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] *= scale;
            }
        }
    }

    public Vector3 mul(Vector3 vec) {
        return new Vector3(
                matrix[0][0] * vec.x + matrix[0][1] * vec.y + matrix[0][2] * vec.z,
                matrix[1][0] * vec.x + matrix[1][1] * vec.y + matrix[1][2] * vec.z,
                matrix[2][0] * vec.x + matrix[2][1] * vec.y + matrix[2][2] * vec.z
        );
    }

    public Matrix3 mul(Matrix3 mat) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = matrix[i][0] * mat.matrix[0][j] + matrix[i][1] * mat.matrix[1][j] + matrix[i][2] * mat.matrix[2][j];
            }
        }
        return new Matrix3(result);
    }

    public static Matrix3 identity() {
        Matrix3 identity = new Matrix3();
        for (int i = 0; i < 3; i++) {
            identity.matrix[i][i] = 1.0f;
        }
        return identity;
    }

    public float determinant() {
        return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
    }

    public Matrix3 inverse() {
        float det = determinant();
        if (det == 0) throw new ArithmeticException("Matrix is singular and cannot be inverted.");
        float[][] inv = new float[3][3];

        inv[0][0] = (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) / det;
        inv[0][1] = (matrix[0][2] * matrix[2][1] - matrix[0][1] * matrix[2][2]) / det;
        inv[0][2] = (matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) / det;
        inv[1][0] = (matrix[1][2] * matrix[2][0] - matrix[1][0] * matrix[2][2]) / det;
        inv[1][1] = (matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) / det;
        inv[1][2] = (matrix[0][2] * matrix[1][0] - matrix[0][0] * matrix[1][2]) / det;
        inv[2][0] = (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]) / det;
        inv[2][1] = (matrix[0][1] * matrix[2][0] - matrix[0][0] * matrix[2][1]) / det;
        inv[2][2] = (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) / det;

        return new Matrix3(inv);
    }

    public float trace() {
        return matrix[0][0] + matrix[1][1] + matrix[2][2];
    }

    public Matrix3 transpose() {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix3(result);
    }

    public float[] getRow(int row) {
        return new float[]{matrix[row][0], matrix[row][1], matrix[row][2]};
    }

    public float[] getColumn(int column) {
        return new float[]{matrix[0][column], matrix[1][column], matrix[2][column]};
    }

    public float getValue(int row, int column) {
        return matrix[row][column];
    }

    public Matrix3 getCopy() {
        return new Matrix3(matrix);
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f]\n[%.2f, %.2f, %.2f]",
                matrix[0][0], matrix[0][1], matrix[0][2],
                matrix[1][0], matrix[1][1], matrix[1][2],
                matrix[2][0], matrix[2][1], matrix[2][2]);
    }

    public Vector2 mul(Vector2 vertex) {
        Vector3 vec = new Vector3(vertex.x,vertex.y,1);
        vec = mul(vec);
        return new Vector2(vec.getX(),vec.getY());
    }
}
