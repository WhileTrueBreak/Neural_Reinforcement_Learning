package matrix;

public class Matrix {
	
	private int rows, cols;
	
	private double[] matrix;
	
	public Matrix(int rows, int cols) {
		matrix = new double[cols*rows];
		this.rows = rows;
		this.cols = cols;
	}
	
	public void randomiseMatrix(double min, double max) {
		if(max <= min) System.out.println("min is greater than max");;
		for(int i = 0;i < matrix.length;i++) {
			matrix[i] = Math.random()*(max-min)+min;
		}
	}
	
	public void applyFunc(Activation func) {
		for(int i = 0;i < matrix.length;i++) matrix[i] = func.function(matrix[i]);
	}
	
	public void setMatrixValue(int col, int row, double value) {
		if(col < 0) System.out.println("x is smaller than 0");
		if(row < 0) System.out.println("y is smaller than 0");
		if(col > cols) System.out.println("x is greater than " + (cols-1));
		if(row > rows) System.out.println("y is greater than " + (rows-1));
		matrix[col+row*cols] = value;
	}
	
	public double getMatrixValue(int col, int row) {
		if(col < 0) System.out.println("x is smaller than 0");
		if(row < 0) System.out.println("y is smaller than 0");
		if(col > cols) System.out.println("x is greater than " + (cols-1));
		if(row > rows) System.out.println("y is greater than " + (rows-1));
		return matrix[col+row*cols];
	}
	
	private double getArrayValue(int i) {
		return matrix[i];
	}
	
	private void setArrayValue(int i, double value) {
		matrix[i] = value;
	}
	
	public Matrix cloneMatrix() {
		Matrix m = new Matrix(rows, cols);
		for(int i = 0;i < matrix.length;i++) m.setArrayValue(i, matrix[i]);
		return m;
	}
	
	public double[] toArray() {
		return Matrix.transpose(this).getMatrix();
	}

	private double[] getMatrix() {
		return matrix;
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}
	
	//STATIC METHODS//

	public static Matrix transpose(Matrix m) {
		Matrix mt = new Matrix(m.getCols(), m.getRows());
		for(int i = 0;i < m.getMatrix().length;i++) mt.setArrayValue((i % m.getCols())*m.getRows()+(int)(i/m.getCols()), m.getArrayValue(i));
		return mt;
	}
	
	public static Matrix add(Matrix m1, Matrix m2) {
		if(m1.getCols() != m2.getCols()) {
			System.out.println("m1 width is not the same as m2 width");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		if(m1.getRows() != m2.getRows()) {
			System.out.println("m1 height is not the same as m2 height");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		Matrix out = new Matrix(m1.getRows(), m1.getCols());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)+m2.getArrayValue(i));
		return out;
	}
	
	public static Matrix sub(Matrix m1, Matrix m2) {
		if(m1.getCols() != m2.getCols()) {
			System.out.println("m1 width is not the same as m2 width");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		if(m1.getRows() != m2.getRows()) {
			System.out.println("m1 height is not the same as m2 height");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		Matrix out = new Matrix(m1.getRows(), m1.getCols());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)-m2.getArrayValue(i));
		return out;
	}
	
	public static Matrix scalarProduct(Matrix m1, double scalar) {
		Matrix out = new Matrix(m1.getRows(), m1.getCols());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)*scalar);
		return out;
	}
	
	public static Matrix matrixProduct(Matrix m1, Matrix m2) {
		if(m1.getCols() != m2.getRows()) {
			System.out.println("m1 width is not the same as m2 height");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		Matrix out = new Matrix(m1.getRows(), m2.getCols());
		for(int i = 0;i < out.getMatrix().length;i++) {
			for(int j = 0;j < m1.getCols();j++) {
				out.setArrayValue(i, out.getArrayValue(i)+(m2.getMatrixValue(i%out.getCols(), j)*m1.getMatrixValue(j, (int)(i/out.getCols()))));
			}
		}
		return out;
	}
	
	public static Matrix hadamardProduct(Matrix m1, Matrix m2) {
		if(m1.getCols() != m2.getCols()) {
			System.out.println("m1 width is not the same as m2 width");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		if(m1.getRows() != m2.getRows()) {
			System.out.println("m1 height is not the same as m2 height");
			new Throwable().printStackTrace();
			System.exit(1);
		}
		Matrix out = new Matrix(m1.getRows(), m1.getCols());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)*m2.getArrayValue(i));
		return out;
	}
	
	public static Matrix fromArray(double[] arr) {
		Matrix out = new Matrix(arr.length, 1);
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, arr[i]);
	    return out;
	}
	
}