package field_calculator;

import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;

public class SomeMath {
	
	public static boolean anyNegative1D(double[] arr) {
        boolean ans = false;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] < 0) {
                ans = true;
                break;
            }
        }
        return ans;
    }
	
	public static double[] linspace(double min, double max, int points) {  
	    double[] d = new double[points];  
	    for (int i = 0; i < points; i++){  
	        d[i] = min + i * (max - min) / (points - 1);  
	    }  
	    return d;  
	} 
	

	public static ArrayList<double[][]> meshgrid (double[] x, double[] y) {
		double[][] xx = new double[y.length][x.length];
		double[][] yy = new double[x.length][y.length];
		
		for(int i = 0; i < y.length; i++) {
			xx[i] = x;
		}
		
		for(int i = 0; i < x.length; i++) {
			yy[i] = y;
		}
		
		yy = MatrixUtils.createRealMatrix(yy).transpose().getData();
		
		ArrayList<double[][]> grids = new ArrayList<>(2);
		grids.add(0, xx);
		grids.add(1, yy);
		
		return grids;
		
	}

	public static ArrayList<double[][]> sph2cart(double[][] phi, double[][] theta, double[][] r) {
		double[][] cosPhi = new double[phi.length][phi[0].length];
		double[][] sinPhi = new double[phi.length][phi[0].length];
		double[][] cosTheta = new double[theta.length][theta[0].length];
		double[][] sinTheta = new double[theta.length][theta[0].length];
		double[][] x = new double[phi.length][phi[0].length];
		double[][] y = new double[phi.length][phi[0].length];
		double[][] z = new double[phi.length][phi[0].length];

		for (int i = 0; i < phi.length; i++) {
			for (int j = 0; j < phi[0].length; j++) {
				cosPhi[i][j] = Math.cos(phi[i][j]);
				sinPhi[i][j] = Math.sin(phi[i][j]);
			}
		}

		for (int i = 0; i < theta.length; i++) {
			for (int j = 0; j < theta[0].length; j++) {
				cosTheta[i][j] = Math.cos(theta[i][j]);
				sinTheta[i][j] = Math.sin(theta[i][j]);
			}
		}

		for (int i = 0; i < theta.length; i++) {
			for (int j = 0; j < theta[0].length; j++) {
				x[i][j] = r[i][j] * cosPhi[i][j] * sinTheta[i][j];
				y[i][j] = r[i][j] * sinPhi[i][j] * sinTheta[i][j];
				z[i][j] = r[i][j] * cosTheta[i][j];
			}
		}

		ArrayList<double[][]> cartCoords = new ArrayList<>(3);
		cartCoords.add(0, x);
		cartCoords.add(1, y);
		cartCoords.add(2, z);
		return cartCoords;
	}

	public static int findFirst(double[] array, double target) {
		int result = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == target) {
				result = i;
				return result;
			}
		}
		return result;
	}

	public static int[] findFirst(double[][] array, double target) {
		int[] result = {-1, -1};
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] == target) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return result;
	}

}
