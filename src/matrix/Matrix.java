package matrix;

public class Matrix {
	
	private int w, h;
	
	private double[] matrix;
	
	public Matrix(int w, int h) {
		matrix = new double[w*h];
		this.w = w;
		this.h = h;
	}
	
	public void randomiseMatrix(double min, double max) {
		if(max <= min) System.out.println("min is greater than max");;
		for(int i = 0;i < matrix.length;i++) {
			matrix[i] = Math.random()*(max-min)+min;
		}
	}
	
	public void setMatrixValue(int x, int y, double value) {
		if(x < 0) System.out.println("x is smaller than 0");
		if(y < 0) System.out.println("y is smaller than 0");
		if(x >= w) System.out.println("x is greater than " + (w-1));
		if(y >= h) System.out.println("y is greater than " + (h-1));
		matrix[x+y*w] = value;
	}
	
	public double getMatrixValue(int x, int y) {
		if(x < 0) System.out.println("x is smaller than 0");
		if(y < 0) System.out.println("y is smaller than 0");
		if(x >= w) System.out.println("x is greater than " + (w-1));
		if(y >= h) System.out.println("y is greater than " + (h-1));
		return matrix[x+y*w];
	}
	
	public double getArrayValue(int i) {
		return matrix[i];
	}
	
	public void setArrayValue(int i, double value) {
		matrix[i] = value;;
	}
	
	public Matrix cloneMatrix() {
		Matrix m = new Matrix(w, h);
		for(int i = 0;i < matrix.length;i++) m.setArrayValue(i, matrix[i]);
		return m;
	}

	public double[] getMatrix() {
		return matrix;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	
	//STATIC METHODS//

	public static Matrix transpose(Matrix m) {
		Matrix mt = new Matrix(m.getH(), m.getW());
		for(int i = 0;i < m.getMatrix().length;i++) mt.setArrayValue((i % m.getW())*2+(int)(i/m.getW()), m.getArrayValue(i));
		return mt;
	}
	
	public static Matrix add(Matrix m1, Matrix m2) {
		if(m1.getW() != m2.getW()) System.out.println("m1 width is not the same as m2 width");
		if(m1.getH() != m2.getH()) System.out.println("m1 height is not the same as m2 height");
		Matrix out = new Matrix(m1.getW(), m1.getH());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)+m2.getArrayValue(i));
		return out;
	}
	
	public static Matrix sub(Matrix m1, Matrix m2) {
		if(m1.getW() != m2.getW()) System.out.println("m1 width is not the same as m2 width");
		if(m1.getH() != m2.getH()) System.out.println("m1 height is not the same as m2 height");
		Matrix out = new Matrix(m1.getW(), m1.getH());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)-m2.getArrayValue(i));
		return out;
	}
	
	public static Matrix scalarProduct(Matrix m1, double scalar) {
		Matrix out = new Matrix(m1.getW(), m1.getH());
		for(int i = 0;i < out.getMatrix().length;i++) out.setArrayValue(i, m1.getArrayValue(i)*scalar);
		return out;
	}
	
	public static Matrix matrixProduct(Matrix m1, Matrix m2) {
		if(m1.getW() != m2.getH()) System.out.println("m1 width is not the same as m2 height");
		Matrix out = new Matrix(m2.getW(), m1.getH());
		for(int i = 0;i < out.getMatrix().length;i++) {
			for(int j = 0;j < m1.getW();j++) {
				out.setArrayValue(i, out.getArrayValue(i)+(m2.getMatrixValue(i%out.getW(), j)*m1.getMatrixValue(j, (int)(i/out.getW()))));
			}
		}
		return out;
	}
	
}
