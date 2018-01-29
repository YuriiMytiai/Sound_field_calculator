package field_calculator;

import javafx.scene.image.ImageView;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.colors.colormaps.ColorMapRedAndGreen;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;
import org.jzy3d.plot3d.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class Plotter {

    public static ImageView plotRectangularSurface(Shape surface) {

        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);

        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();

        ImageView imageView = factory.bindImageView(chart);

        return imageView;
    }

    public static Shape buildRectSurface(double[][] x, double[][] y) {
        double z = 0;

        // Create the 3d object
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < (x.length - 1); i++) {
            for (int j = 0; j < (x[0].length - 1); j++) {
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(x[i][j], y[i][j], z) ));
                polygon.add(new Point( new Coord3d(x[i][j+1], y[i][j+1], z)));
                polygon.add(new Point( new Coord3d(x[i+1][j+1], y[i+1][j+1], z)));
                polygon.add(new Point( new Coord3d(x[i+1][j], y[i+1][j], z)));
                polygons.add(polygon);
            }
        }

        // Jzy3d
        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapGrayscale(), -5, 1, new Color(1,1,1,1f)));
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.GRAY);
        return surface;
    }

    public static ImageView plotAllSources(Area area, Shape surface) {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        double soundAxisLength = Math.max(area.xSize, area.ySize)*0.3;
        double maxZPosition = 0;
        for (Integer key:area.sources.keySet()) {
            Source curSource = area.sources.get(key);

            if (curSource.position[2] > maxZPosition) {maxZPosition = curSource.position[2];}

            double xMin = - curSource.soundSourceObj.getCentralPoint()[0];
            double yMin = - curSource.soundSourceObj.getCentralPoint()[1];
            double zMin = - curSource.soundSourceObj.getCentralPoint()[2];
            double xMax = xMin + curSource.soundSourceObj.getSizes()[0];
            double yMax = yMin + curSource.soundSourceObj.getSizes()[1];
            double zMax = zMin + curSource.soundSourceObj.getSizes()[2];

            BoundingBox3d bounds = new BoundingBox3d((float) xMin, (float) xMax, (float) yMin, (float) yMax, (float) zMin, (float) zMax);
            Parallelepiped box = new Parallelepiped(bounds);

            Color c1 = new Color(255, 0, 0);
            Point p1 = new Point(new Coord3d(0,0,0), c1);
            Color c2 = new Color(178, 255, 102);
            Point p2 = new Point(new Coord3d(soundAxisLength,0,0), c2);
            LineStrip sourceAxis = new LineStrip(p1, p2);

            box.setColorMapper(new ColorMapper(new ColorMapRedAndGreen(), -5, 30, new Color(1f, 1, 1f, 0.35f)));
            Transform transform = new Transform();
            Rotate rotateTheta = new Rotate(-curSource.theta0, new Coord3d(0, curSource.position[1] + curSource.soundSourceObj.getCentralPoint()[1], 0));
            transform.add(rotateTheta);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);
            transform = null;

            transform = new Transform();
            Rotate rotatePhi = new Rotate(-curSource.phi0, new Coord3d(0, 0, curSource.position[2] + curSource.soundSourceObj.getCentralPoint()[2]));
            transform.add(rotatePhi);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);

            Transform transformShift = new Transform();
            Translate shift = new Translate(new Coord3d(curSource.position[0], curSource.position[1], curSource.position[2]));
            transformShift.add(shift);
            box.applyGeometryTransform(transformShift);
            sourceAxis.applyGeometryTransform(transformShift);
            chart.getScene().getGraph().add(box);
            chart.getScene().getGraph().add(sourceAxis);

            rotateTheta = null;
            rotatePhi = null;
            transform = null;
        }
        BoundingBox3d bbox = new BoundingBox3d();
        bbox.setXmax((float) area.xSize);
        bbox.setXmin((float) (0));
        bbox.setYmax((float) area.ySize);
        bbox.setYmin((float) (0));
        bbox.setZmax((float) (maxZPosition + 1));
        bbox.setZmin((float) (0));
        chart.getView().setBoundManual(bbox);
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getAxeLayout().setZAxeLabel("Z");
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();

        chart.getView().setSquared(false);
        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );

        ImageView imageView = factory.bindImageView(chart);

        return imageView;
    }

    public static ImageView plotLightedSource(Area area, Shape surface, int sourceNum) {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        double soundAxisLength = Math.max(area.xSize, area.ySize)*0.3;
        double maxZPosition = 0;
        for (Integer key:area.sources.keySet()) {
            Source curSource = area.sources.get(key);

            if (curSource.position[2] > maxZPosition) {maxZPosition = curSource.position[2];}

            double xMin = - curSource.soundSourceObj.getCentralPoint()[0];
            double yMin = - curSource.soundSourceObj.getCentralPoint()[1];
            double zMin = - curSource.soundSourceObj.getCentralPoint()[2];
            double xMax = xMin + curSource.soundSourceObj.getSizes()[0];
            double yMax = yMin + curSource.soundSourceObj.getSizes()[1];
            double zMax = zMin + curSource.soundSourceObj.getSizes()[2];

            BoundingBox3d bounds = new BoundingBox3d((float) xMin, (float) xMax, (float) yMin, (float) yMax, (float) zMin, (float) zMax);
            Parallelepiped box = new Parallelepiped(bounds);

            Color c1 = new Color(255, 0, 0);
            Point p1 = new Point(new Coord3d(0,0,0), c1);
            Color c2 = new Color(178, 255, 102);
            Point p2 = new Point(new Coord3d(soundAxisLength,0,0), c2);
            LineStrip sourceAxis = new LineStrip(p1, p2);

            if(key == sourceNum) {
                box.setColorMapper(new ColorMapper(new ColorMapGrayscale(), 0, 0, new Color(0, 0, 0, 0f)));
            } else {
                box.setColorMapper(new ColorMapper(new ColorMapRedAndGreen(), -5, 30, new Color(1f, 1, 1f, 0.35f)));
            }

            Transform transform = new Transform();
            Rotate rotateTheta = new Rotate(-curSource.theta0, new Coord3d(0, curSource.position[1] + curSource.soundSourceObj.getCentralPoint()[1], 0));
            transform.add(rotateTheta);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);
            transform = null;

            transform = new Transform();
            Rotate rotatePhi = new Rotate(-curSource.phi0, new Coord3d(0, 0, curSource.position[2] + curSource.soundSourceObj.getCentralPoint()[2]));
            transform.add(rotatePhi);
            box.applyGeometryTransform(transform);
            sourceAxis.applyGeometryTransform(transform);

            Transform transformShift = new Transform();
            Translate shift = new Translate(new Coord3d(curSource.position[0], curSource.position[1], curSource.position[2]));
            transformShift.add(shift);
            box.applyGeometryTransform(transformShift);
            sourceAxis.applyGeometryTransform(transformShift);
            chart.getScene().getGraph().add(box);
            chart.getScene().getGraph().add(sourceAxis);

            rotateTheta = null;
            rotatePhi = null;
            transform = null;
        }
        BoundingBox3d bbox = new BoundingBox3d();
        bbox.setXmax((float) area.xSize);
        bbox.setXmin((float) (0));
        bbox.setYmax((float) area.ySize);
        bbox.setYmin((float) (0));
        bbox.setZmax((float) (maxZPosition + 1));
        bbox.setZmin((float) (0));
        chart.getView().setBoundManual(bbox);
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getAxeLayout().setZAxeLabel("Z");
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();

        chart.getView().setSquared(false);
        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );

        ImageView imageView = factory.bindImageView(chart);

        return imageView;
    }

    public static ImageView plotField(Shape surface) {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Advanced;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");

        AWTColorbarLegend cbar = new AWTColorbarLegend(surface, chart.getView().getAxe().getLayout());
        surface.setLegend(cbar);
        surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display

        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        chart.getView().getCamera().setScreenGridDisplayed(false);
        chart.getAxeLayout().setXTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setYTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setZTickRenderer( new IntegerTickRenderer() );
        chart.getAxeLayout().setXAxeLabel("X");
        chart.getAxeLayout().setYAxeLabel("Y");
        chart.getAxeLayout().setYAxeLabel("dB SPL");
        org.jzy3d.maths.Scale zScale = new org.jzy3d.maths.Scale(surface.getBounds().getZmin(), surface.getBounds().getZmax());
        chart.getView().setScaleZ(zScale);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();
        chart.getView().setMaximized(true);

        //layout2d(chart);

        ImageView imageView = factory.bindImageView(chart);
        return imageView;
    }
}
