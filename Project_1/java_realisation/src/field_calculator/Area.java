package field_calculator;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.complex.Complex;

public class Area {
	
	public final double P0 = 2e-5;
	private double xSize;
	private double ySize;
	private double xStep;
	private double yStep;
	double[][] gridX;
	double[][] gridY;
	HashMap<Integer, Source> sources = new HashMap<Integer, Source>();
	double[][][] sumFieldAbs;
	double[][][] sumFieldPhase;
	private int sourcesCounter = -1;
	
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
		double[] yPoints = SomeMath.linspace(0, xSize, (int) numYPoints);
		
		ArrayList<double[][]> grids = SomeMath.meshgrid(xPoints, yPoints);
		gridX = grids.get(0);
		gridY = grids.get(1);
	}
	
	public void addSource(Source source) {
		sourcesCounter++;
		source.number = sourcesCounter++;
		sources.put(sourcesCounter, source);
	}
	
	@SuppressWarnings("null")
	public void calcSummPreasure(int freqIdx) {
		if (sumFieldAbs == null) {
			sumFieldAbs = new double[32][gridX.length][gridX[0].length];
			sumFieldPhase = new double[32][gridX.length][gridX[0].length];
		}
		
		
	    Complex[][] p = new Complex [gridX.length][gridX[0].length];
	    
		for (Integer key: sources.keySet()) {
			
		     Source curSource = sources.get(key);
		     double[][] ro = null;
			 double[][] phi = null;
			 
		     ro = Matrix.dotMultiply(P0, Matrix.pow(10, Matrix.dotMultiply(1/20, curSource.preasureAbs[freqIdx])));
		     phi = Matrix.dotMultiply(-1, curSource.preasureAbs[freqIdx]);
		     Complex pCur;
		     
		     for (int i = 0; i < gridX.length; i++) {
		            for (int j = 0; j < gridX[0].length; j++) {        	
		                pCur = new Complex(ro[i][j] * Math.cos(phi[i][j]), ro[i][j] * Math.sin(phi[i][j]));
		                p[i][j] = p[i][j].add(pCur);
		            }
		     }    
		}
		
		double[][] absP = null;
		for (int i = 0; i < gridX.length; i++) {
            for (int j = 0; j < gridX[0].length; j++) {        	
                absP[i][j] = p[i][j].abs();
            }
        }
		
		double[][] angP = null;
		for (int i = 0; i < gridX.length; i++) {
            for (int j = 0; j < gridX[0].length; j++) {        	
                angP[i][j] = Math.atan2(p[i][j].getImaginary(), p[i][j].getReal());
            }
        }
		
		sumFieldAbs[freqIdx] = Matrix.dotMultiply(20, Matrix.dotMultiply(1/P0, Matrix.dotLog10(absP)));
		sumFieldPhase[freqIdx] = angP;
	}


}
