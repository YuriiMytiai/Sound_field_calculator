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


}
