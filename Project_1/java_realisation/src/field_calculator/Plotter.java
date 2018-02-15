package field_calculator;

import javafx.scene.image.ImageView;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.IntegerTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;
import org.jzy3d.plot3d.rendering.view.ViewportMode;

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

    public static ImageView plotAllSourcesRectArea(Area area, Shape surface) {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        double soundAxisLength = Math.max(area.xSize, area.ySize)*0.3;
        double maxZPosition = 0;
        for (Integer key:area.sources.keySet()) {
            Source curSource = area.sources.get(key);
            if (curSource.position[2] > maxZPosition) {maxZPosition = curSource.position[2];}

            chart = curSource.plotSource(chart, soundAxisLength, false);
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

    public static ImageView plotLightedSourceRectArea(Area area, Shape surface, int sourceNum) {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        Quality quality = Quality.Intermediate;
        AWTChart chart = (AWTChart) factory.newChart(quality, "offscreen");
        chart.getScene().getGraph().add(surface);

        double soundAxisLength = Math.max(area.xSize, area.ySize)*0.3;
        double maxZPosition = 0;
        for (Integer key:area.sources.keySet()) {
            Source curSource = area.sources.get(key);

            if (curSource.position[2] > maxZPosition) {maxZPosition = curSource.position[2];}

            if(key == sourceNum) {
                chart = curSource.plotSource(chart, soundAxisLength, true);
            } else {
                chart = curSource.plotSource(chart, soundAxisLength, false);
            }

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

    public static ImageView plotField(Shape surface, String zLabel) {
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
        chart.getAxeLayout().setYAxeLabel(zLabel);
        org.jzy3d.maths.Scale zScale = new org.jzy3d.maths.Scale(surface.getBounds().getZmin(), surface.getBounds().getZmax());
        chart.getView().setScaleZ(zScale);
        chart.getView().getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);
        chart.addMouseCameraController();
        chart.getView().setMaximized(true);

        //layout2d(chart);

        ImageView imageView = factory.bindImageView(chart);
        return imageView;
    }

    public static Shape buildRectInclinedSurface(double[][] x, double[][] y, double[][] z) {
        // Create the 3d object
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < (x.length - 1); i++) {
            for (int j = 0; j < (x[0].length - 1); j++) {
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(x[i][j], y[i][j], z[i][j]) ));
                polygon.add(new Point( new Coord3d(x[i][j+1], y[i][j+1], z[i][j+1])));
                polygon.add(new Point( new Coord3d(x[i+1][j+1], y[i+1][j+1], z[i+1][j+1])));
                polygon.add(new Point( new Coord3d(x[i+1][j], y[i+1][j], z[i+1][j])));
                polygons.add(polygon);
            }
        }

        // Jzy3d
        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapGrayscale(), -5, (z[z.length -1][z[0].length-1] + 5), new Color(1,1,1,1f)));
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.GRAY);
        return surface;
    }

}
