package field_calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.image.ImageView;
import org.apache.commons.math3.complex.Complex;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbowNoBorder;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;

public class RectangularArea extends Area implements Serializable {
	/*
	Properties from super class Area:

	public double xSize;
	public double ySize;
	public double xStep;
	public double yStep;
	public double[][] gridX;
	public double[][] gridY;
	public HashMap<Integer, Source> sources = new HashMap<Integer, Source>();
	private double[][][] sumFieldAbs;
	private double[][][] sumFieldPhase;
	*/

	public RectangularArea(double xSize_, double ySize_, double xStep_, double yStep_) {
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
		gridZ = new double[gridX.length][gridX[0].length];
	}

	


	public ImageView plotSurface() {
		Shape surface = buildSurface();
		return Plotter.plotRectangularSurface(surface);
	}

	public ImageView plotSources() {
		Shape surface = buildSurface();
		ImageView imageView = Plotter.plotAllSourcesRectArea(this, surface);
		return imageView;
	}

	public ImageView plotLightedSource(int sourceNum) {
		Shape surface = buildSurface();
		ImageView imageView = Plotter.plotLightedSourceRectArea(this, surface, sourceNum);
		return imageView;
	}

	public Shape buildSurface() {
		return Plotter.buildRectSurface(gridX, gridY);
	}


}
