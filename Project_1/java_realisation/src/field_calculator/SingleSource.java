package field_calculator;


import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.colors.colormaps.ColorMapRedAndGreen;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Parallelepiped;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

import java.io.Serializable;

public class SingleSource extends Source implements Serializable{

	/*
	Properties from super class Source:

	public int number;
	public String name;
	public double[] position = {0, 0, 0};
	public SoundSource soundSourceObj;
	public double[][][] preasureAbs;
	public double[][][] preasurePhase;
	public int phi0 = 0;
	public int theta0 = 0;
	public double kAmp = 0;
	public double tau0 = 0;
	public double c0 = 340;
	*/
	
	public SingleSource(int num, String name_, double[] pos, SoundSource ssObj) {
		super(num, name_, pos, ssObj);
	}
	
	public void calcSourcePreasure(double[][] gridX, double[][] gridY, int freqIdx) {
		if (preasureAbs == null) {
			preasureAbs = new double[32][gridX.length][gridX[0].length];
			preasurePhase = new double[32][gridX.length][gridX[0].length];
		}
		int numCols = gridX.length;
		int numRows = gridX[0].length;
		
		for (int xPoint = 0; xPoint < numCols; xPoint++) {
			for (int yPoint = 0; yPoint < numRows; yPoint++) {
				double[] preasure = calcPreasure(gridX[xPoint][yPoint], gridY[xPoint][yPoint],
													 freqIdx);
				preasureAbs[freqIdx][xPoint][yPoint] = preasure[0];
				preasurePhase[freqIdx][xPoint][yPoint] = preasure[1];				
			}
		}
	}

	public AWTChart plotSource(AWTChart chart, double soundAxisLength, boolean isHighlighted) {
		double xMin = - soundSourceObj.getCentralPoint()[0];
		double yMin = - soundSourceObj.getCentralPoint()[1];
		double zMin = - soundSourceObj.getCentralPoint()[2];
		double xMax = xMin + soundSourceObj.getSizes()[0];
		double yMax = yMin + soundSourceObj.getSizes()[1];
		double zMax = zMin + soundSourceObj.getSizes()[2];

		BoundingBox3d bounds = new BoundingBox3d((float) xMin, (float) xMax, (float) yMin, (float) yMax, (float) zMin, (float) zMax);
		Parallelepiped box = new Parallelepiped(bounds);

		Color c1 = new Color(255, 0, 0);
		Point p1 = new Point(new Coord3d(0,0,0), c1);
		Color c2 = new Color(178, 255, 102);
		Point p2 = new Point(new Coord3d(soundAxisLength,0,0), c2);
		LineStrip sourceAxis = new LineStrip(p1, p2);

		if(isHighlighted) {
			box.setColorMapper(new ColorMapper(new ColorMapGrayscale(), 0, 0, new Color(0, 0, 0, 0f)));
		} else {
			box.setColorMapper(new ColorMapper(new ColorMapRedAndGreen(), -5, 30, new Color(1f, 1, 1f, 0.35f)));
		}

		Transform transform = new Transform();
		Rotate rotateTheta = new Rotate(-theta0, new Coord3d(0, position[1] + soundSourceObj.getCentralPoint()[1], 0));
		transform.add(rotateTheta);
		box.applyGeometryTransform(transform);
		sourceAxis.applyGeometryTransform(transform);
		transform = null;

		transform = new Transform();
		Rotate rotatePhi = new Rotate(-phi0, new Coord3d(0, 0, position[2] + soundSourceObj.getCentralPoint()[2]));
		transform.add(rotatePhi);
		box.applyGeometryTransform(transform);
		sourceAxis.applyGeometryTransform(transform);

		Transform transformShift = new Transform();
		Translate shift = new Translate(new Coord3d(position[0], position[1], position[2]));
		transformShift.add(shift);
		box.applyGeometryTransform(transformShift);
		sourceAxis.applyGeometryTransform(transformShift);
		chart.getScene().getGraph().add(box);
		chart.getScene().getGraph().add(sourceAxis);

		return chart;
	}

    private double[] calcPreasure(double gridXP, double gridYP, int freqIdx) {
        double[][] rotY = RotMatrix.getRotY(theta0);
        double[][] rotZ = RotMatrix.getRotZ(phi0);

        double xRS = gridXP - position[0];
        double yRS = gridYP - position[1];
        double zRS = 0 - position[2];
        double[] packedCoords = {xRS, yRS, zRS};

        double[] rotatedZ = Matrix.multiply(rotZ, packedCoords);
        double[] rotatedY = Matrix.multiply(rotY, rotatedZ);

        xRS = rotatedY[0];
        yRS = rotatedY[1];
        zRS = rotatedY[2];

        double r = Math.sqrt((xRS*xRS + yRS*yRS + zRS*zRS));
        int theta = (int) Math.toDegrees(Math.round(Math.acos((zRS/r))));
        int phi = (int) Math.toDegrees(Math.round(Math.atan2(yRS, xRS)));

        if (phi > 359) {
            phi -= 359;
        }
        if (phi < 0) {
            phi += 359;
        }

        double alpha = 1.24e-11 * SoundSource.FREQS[freqIdx] * SoundSource.FREQS[freqIdx];

        double rLog;
        if (r > 2.1) {
            rLog = r;
        } else {rLog = 2.1;}

        double A = soundSourceObj.getSensitivity()[freqIdx] + kAmp - 20 * Math.log10(rLog-1) + soundSourceObj.getAmplitudeRP(freqIdx)[theta][phi] - 20 * alpha * r * Math.log10(Math.exp(1));
        double Psi = 2 * Math.PI * SoundSource.FREQS[freqIdx] * (r/c0 + tau0) + soundSourceObj.getPhaseRP(freqIdx)[theta][phi];

        return new double[]{A, Psi};
    }
}
