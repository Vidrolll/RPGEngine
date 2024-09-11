package com.rpg.main.math;

public class Matrix {
    //A 2-dimensional array to hold the matrix data
    private final float[][] matrix;

    /** Default constructor. Creates a new matrix with a defined size.
     * @param rows (int) The number of rows for the matrix to have.
     * @param columns (int) The number of columns for the matrix to have.
     */
    public Matrix(int rows, int columns) {
        matrix = new float[rows][columns];
    }
    /**Alternative constructor. Creates a matrix using a predefined 2-dimensional array.
     *@param matrix (float[][]) A predefined 2-dimensional float array to define the matrix to.
     */
    public Matrix(float[][] matrix) {
        this.matrix = matrix;
    }

    /**Sets an entire row to a predefined array.
     * @param n (int) The chosen row to define.
     * @param row (float[]) The array to set row "n" to.
     */
    public void setRow(int n, float[] row) {
        if(matrix[n].length!=row.length) {
            System.err.println("The provided array does not match the matrix row size!");
            return;
        }
        matrix[n] = row;
    }

    /**Sets an entire column to a predefined array.
     * @param n (int) The chosen column to define.
     * @param column (float[]) The array to set column "n" to.
     */
    public void setColumn(int n, float[] column) {
        if(matrix.length!=column.length) {
            System.err.println("The provided array does not match the matrix row size!");
            return;
        }
        for(int i = 0; i < matrix.length; i++)
            matrix[i][n] = column[i];
    }

    /**Sets a value at a given index to a given value
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
        for(int i = 0; i < matrix[row].length; i++) {
            matrix[row][i] *= scale;
        }
    }
    /**
     * Scales every value in a matrix column by the inputted scale value.
     * @param column (int) The chosen column to modify.
     * @param scale (float) The scale quantity.
     */
    public void scaleColumn(int column, float scale) {
        for(int i = 0; i < matrix.length; i++) {
            matrix[i][column] *= scale;
        }
    }

    /**
     * Scales an entire matrix by the inputted scale value.
     * @param scale (float) The scale value.
     */
    public void scaleMatrix(float scale) {
        for(float[] row : matrix)
            for(int i = 0; i < row.length; i++)
                row[i] *= scale;
    }

    /**
     * Performs matrix multiplication on a given vector. <br>
     * Returns null if the inputted vector and the parent matrix cannot be multiplied.
     * @param vec (Vector) The vector to multiply.
     * @return (Vector) The multiplied vector.
     */
    public Vector mul(Vector vec) {
        if(vec.vec.length != matrix[0].length) return null;
        Vector mul = new Vector(matrix.length);
        for(int i = 0; i < mul.vec.length; i++) {
            mul.setValue(i,vec.dot(new Vector(getRow(i))));
        }
        return mul;
    }

    /**
     * Performs matrix multiplication on another matrix. <br>
     * Returns null if the inputted matrices cannot be multiplied.
     * @param mat (Matrix) The matrix to multiply by.
     * @return (Matrix) The multiplied matrix.
     */
    public Matrix mul(Matrix mat) {
        if(matrix.length != mat.matrix[0].length) return null;
        Matrix mul = new Matrix(matrix.length,mat.matrix[0].length);
        for(int i = 0; i < mul.matrix.length; i++) {
            for(int j = 0; j < mul.matrix[0].length; j++) {
                Vector row = new Vector(getRow(i));
                Vector column = new Vector(mat.getColumn(j));
                mul.setValue(i,j,row.dot(column));
            }
        }
        return mul;
    }

    /**Grabs the data of a row of the matrix and returns it as a 1-dimensional array.
     * @param row (int) The row you want to grab.
     * @return (float[]) The returned row data.
     */
    public float[] getRow(int row) {
        return matrix[row];
    }
    /**Grabs the data of a column of the matrix and returns it as a 1-dimensional array.
     * @param column (int) The column you want to grab.
     * @return (float[]) The returned column data.
     */
    public float[] getColumn(int column) {
        float[] columnArr = new float[matrix.length];
        for(int i = 0; i < columnArr.length; i++)
            columnArr[i] = matrix[i][column];
        return columnArr;
    }

    /**Grabs the data at an inputted index.
     * @param row (int) The inputted row index.
     * @param column (int) The inputted column index.
     * @return (float) The data stored at the inputted index.
     */
    public float getValue(int row, int column) {
        return matrix[row][column];
    }

    /**
     * Creates a copy of the parent matrix.
     * @return (Matrix) A copy of the parent matrix.
     */
    public Matrix getCopy() {
        Matrix copy = new Matrix(matrix.length,matrix[0].length);
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                copy.setValue(i,j,matrix[i][j]);
            }
        }
        return copy;
    }

    /**
     * Converts the parent matrix into a vector. <br>
     * If the parent matrix has more than one row, it will only return the first row as a vector.
     * @return (Vector) The converted matrix.
     */
    public Vector toVector() {
        return new Vector(getRow(0));
    }

    /**
     * Prints out the parent matrix.
     */
    public void print() {
        for(int i = 0; i < matrix.length; i++) {
            System.out.print("[");
            for(int j = 0; j < matrix[i].length; j++) {
                System.out.print(getValue(i,j));
                if(j+1 != matrix[i].length)
                    System.out.print(",");
            }
            System.out.println("]");
        }
    }
}
