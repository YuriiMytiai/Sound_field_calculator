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
	
	@SuppressWarnings("null")
	public static ArrayList<double[][]> meshgrid (double[] x, double[] y) {
		double[][] xx = null;
		double[][] yy = null;
		
		for(int i = 0; i < y.length; i++) {
			xx[i] = x;
		}
		
		for(int i = 0; i < x.length; i++) {
			yy[i] = y;
		}
		
		yy = MatrixUtils.createRealMatrix(yy).transpose().getData();
		
		ArrayList<double[][]> grids = null;
		grids.add(0, xx);
		grids.add(1, yy);
		
		return grids;
		
	}

}
