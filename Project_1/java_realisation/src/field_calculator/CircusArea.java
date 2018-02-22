package field_calculator;

import javafx.scene.image.ImageView;
import org.jzy3d.plot3d.primitives.Shape;

import java.util.ArrayList;

public class CircusArea extends Area {
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

    public CircusArea(double rInner, double rOuter, double xStep_, double yStep_, double hMax) {
        xSize = rOuter * 2;
        ySize = rOuter * 2;
        xStep = xStep_;
        yStep = yStep_;
        zSize = hMax;

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
        gridZ = makeZgrid(gridX, gridY, rInner, rOuter, hMax);
    }

    private double[][] makeZgrid(double[][] gridX, double[][] gridY, double rInner, double rOuter, double hMax) {
        double z0 = hMax * rOuter / rInner - hMax;

        double[][] gridZ = new double[gridX.length][gridX[0].length];
        double a1;
        double a2;
        double sum;
        for (int i = 0; i < gridZ.length; i++) {
            for (int j = 0; j < gridZ[0].length; j++) {
                a1 = ((gridX[i][j]) - rOuter) * ((gridX[i][j]) - rOuter);
                a2 = ((gridY[i][j]) - rOuter) * ((gridY[i][j]) - rOuter);
                sum = a1 + a2;
                if (sum > (rOuter * rOuter)) gridZ[i][j]  = hMax;
                else {
                    gridZ[i][j] = Math.sqrt(sum) / (rOuter / (hMax + z0)) - z0;
                    if (gridZ[i][j] < 0) gridZ[i][j] = 0;
                }
            }
        }

        return gridZ;
    }

    @Override
    public ImageView plotSurface() {
        Shape surface = buildSurface();
        return Plotter.plotRectangularSurface(surface);
    }

    @Override
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

    private Shape buildSurface() {
        return Plotter.buildRectInclinedSurface(gridX, gridY, gridZ);
    }
}
