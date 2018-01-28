package field_calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.complex.Complex;

public class Area implements Serializable {
	
	public static final double P0 = 2e-5;
	public double xSize;
	public double ySize;
	public double xStep;
	public double yStep;
	public double[][] gridX;
	public double[][] gridY;
	public HashMap<Integer, Source> sources = new HashMap<Integer, Source>();
	public double[][][] sumFieldAbs;
	public double[][][] sumFieldPhase;
	public static final double[] FREQS = {16, 20, 25, 31.5, 40, 50, 63, 80, 100, 125,
			160, 200, 250, 315, 400, 500, 630, 800, 1e3, 1.25e3,
			1.6e3, 2e3, 2.5e3, 3.15e3, 4e3, 5e3, 6.3e3, 8e3, 1e4, 1.25e4,
			1.6e4, 2e4};
	//private int sourcesCounter = 0;
	
	public Area(double xSize_, double ySize_, double xStep_, double yStep_) {
		xSize = xSize_;
		ySize = ySize_;
		xStep = xStep_;
		yStep = yStep_;
		
		double numXPoints = xSize / xStep + 1;
		if ((numXPoints - Math.floor(numXPoints)) != 0) {
			numXPoints += 1;
			xStep = xSize / numXPoints;
		}
		
		double numYPoints = ySize / yStep + 1;
		if ((numYPoints - Math.floor(numYPoints)) != 0) {
			numYPoints += 1;
			yStep = ySize / numYPoints;
		}
		
		double[] xPoints = SomeMath.linspace(0, xSize, (int) numXPoints);
		double[] yPoints = SomeMath.linspace(0, ySize, (int) numYPoints);
		
		ArrayList<double[][]> grids = SomeMath.meshgrid(xPoints, yPoints);
		gridX = grids.get(0);
		gridY = grids.get(1);
	}
	
	public void addSource(Source source, int place) {
		sources.put(place, source);
		//sourcesCounter++;
	}
	

	public void calcSummPreasure(int freqIdx) {
		if (sumFieldAbs == null) {
			sumFieldAbs = new double[32][gridX.length][gridX[0].length];
			sumFieldPhase = new double[32][gridX.length][gridX[0].length];
		}
		
		
	    Complex[][] p = new Complex [gridX.length][gridX[0].length];
		for(int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[0].length; j++) {
				p[i][j] = new Complex(0,0);
			}
		}
	    
		for (Integer key: sources.keySet()) {
			
		     Source curSource = sources.get(key);
			 
		     double[][] ro = Matrix.dotMultiply(P0, Matrix.pow(10, Matrix.dotMultiply(0.05, curSource.preasureAbs[freqIdx])));
		     double[][] phi = Matrix.dotMultiply(-1, curSource.preasurePhase[freqIdx]);
		     
		     for (int i = 0; i < gridX.length; i++) {
		            for (int j = 0; j < gridX[0].length; j++) {
		            	double Re = ro[i][j] * Math.cos(phi[i][j]);
		            	double Im = ro[i][j] * Math.sin(phi[i][j]);
		                Complex pCur = new Complex(Re, Im);
		                p[i][j] = p[i][j].add(pCur);
		            }
		     }    
		}
		
		double[][] absP = new double[gridX.length][gridX[0].length];
		for (int i = 0; i < gridX.length; i++) {
            for (int j = 0; j < gridX[0].length; j++) {
                absP[i][j] = p[i][j].abs();
            }
        }
		
		double[][] angP = new double[gridX.length][gridX[0].length];
		for (int i = 0; i < gridX.length; i++) {
            for (int j = 0; j < gridX[0].length; j++) {        	
                angP[i][j] = p[i][j].getArgument();
            }
        }

        double oneDivP0 = 1/P0;
		sumFieldAbs[freqIdx] = Matrix.dotMultiply(20, Matrix.dotLog10(Matrix.dotMultiply(oneDivP0,absP)));
		sumFieldPhase[freqIdx] = angP;
	}


}