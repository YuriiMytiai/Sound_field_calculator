package field_calculator;


import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.math.plot.Plot2DPanel;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SoundSource implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final double[] FREQS = {16, 20, 25, 31.5, 40, 50, 63, 80, 100, 125,
            160, 200, 250, 315, 400, 500, 630, 800, 1e3, 1.25e3,
            1.6e3, 2e3, 2.5e3, 3.15e3, 4e3, 5e3, 6.3e3, 8e3, 1e4, 1.25e4,
            1.6e4, 2e4};
    private final double[] CP_CONST = {1, 0.5, 0.5};
    private String name;
    private double weight;
    private double[] impedance;
    private double[] sensitivity;
    private double[][][] amplitudeRP = new double[FREQS.length][181][360];
    private double[][][] phaseRP = new double[FREQS.length][181][360];
    private double[] sizes = {0.5, 0.5, 0.5,};
    private double[] centralPoint = {0.5, 0.25, 0.25};

    public SoundSource(String s_name) {
        name = s_name;
    }

    public void setWeight(double s_weight) {
        if (s_weight > 0) {
            weight = s_weight;
        } else {
            System.err.println("Weight must be positive value");
        }
    }

    public void setImpedance(double[] s_impedance) {
        if (s_impedance.length == FREQS.length) {
            impedance = s_impedance;
        } else {
            System.err.println("Vector of impedance values must have " + FREQS.length + " elements");
        }
    }

    public void setSensitivity(double[] s_sensitivity) {
        if (s_sensitivity.length == FREQS.length) {
            sensitivity = s_sensitivity;
        } else {
            System.err.println("Vector of sensitivity values must have " + FREQS.length + " elements");
        }
    }

    public void setAmplitudeRP(double[][][] s_amplitudeRP) {
        if ((s_amplitudeRP.length == amplitudeRP.length) & (s_amplitudeRP[0].length == amplitudeRP[0].length) & (s_amplitudeRP[0][0].length == amplitudeRP[0][0].length)) {
            amplitudeRP = s_amplitudeRP;
        } else {
            System.err.println("Matrix of RP values must have " + amplitudeRP.length + "x" + amplitudeRP[0].length + "x" + amplitudeRP[0][0].length + " size");
        }
    }

    public void setPhaseRP(double[][][] s_phaseRP) {
        if ((s_phaseRP.length == phaseRP.length) & (s_phaseRP[0].length == phaseRP[0].length) & (s_phaseRP[0][0].length == phaseRP[0][0].length)) {
            phaseRP = s_phaseRP;
        } else {
            System.err.println("Matrix of RP values must have " + phaseRP.length + "x" + phaseRP[0].length + "x" + phaseRP[0][0].length + " size");
        }
    }

    public void setSizes(double[] s_sizes) {
        if ((s_sizes.length == sizes.length) & !(SomeMath.anyNegative1D(s_sizes))) {
            sizes = s_sizes;
            for (int i = 0; i < sizes.length; i++) {
                centralPoint[i] = sizes[i] * CP_CONST[i];
            }
        }
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public double[] getImpedance() {
        return impedance;
    }

    public double[] getSensitivity() {
        return sensitivity;
    }

    public double[][] getAmplitudeRP(int freqIdx) {
        return amplitudeRP[freqIdx];
    }

    public double[][] getPhaseRP(int freqIdx) {
        return phaseRP[freqIdx];
    }

    public double[] getSizes() {
        return sizes;
    }

    public double[] getCentralPoint() {
        return centralPoint;
    }

    public void plotSensitivity() {
        Plot2DPanel panel = new Plot2DPanel();
        panel.addLinePlot("Sensitivity", FREQS, sensitivity);
        panel.setAxisScale(0,"LOG");
        panel.setFixedBounds(0, FREQS[0], FREQS[(FREQS.length-1)]);
        panel.setAxisLabel(0,"f, Hz");
        panel.setAxisLabel(1,"SPL1.1, dB");
        JFrame frame= new JFrame("Histogram");
        frame.setContentPane(panel);
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void plotImpedance() {
        Plot2DPanel panel = new Plot2DPanel();
        panel.addLinePlot("Impedance", FREQS, impedance);
        panel.setAxisScale(0,"LOG");
        panel.setFixedBounds(0, FREQS[0], FREQS[(FREQS.length-1)]);
        panel.setAxisLabel(0,"f, Hz");
        panel.setAxisLabel(1,"|Z|, Ohm");
        JFrame frame= new JFrame("Histogram");
        frame.setContentPane(panel);
        frame.setSize(300, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void plotAmplitudeRP(double freq) {
        int freqIdx = SomeMath.findFirst(FREQS, freq);
        if (freqIdx == -1) {
            System.out.println(freq + "is not valid frequency");
            return;
        }
        double[] phi = SomeMath.linspace(0, (Math.PI * 2), 360);
        double[] theta = SomeMath.linspace(0, Math.PI, 181);
        double[][] ampRP = amplitudeRP[freqIdx];
        double minAmpRP = Matrix.minValue(ampRP)[0];
        ampRP = Matrix.subtract(ampRP, minAmpRP);
        ArrayList<double[][]> sphGrid = SomeMath.meshgrid(phi, theta);
        ArrayList<double[][]> cartGrid = SomeMath.sph2cart(sphGrid.get(0), sphGrid.get(1), ampRP);
        double[][] x = cartGrid.get(0);
        double[][] y = cartGrid.get(1);
        double[][] z = cartGrid.get(2);

        // Create the 3d object
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < (x.length - 1); i++) {
            for (int j = 0; j < (x[0].length - 1); j++) {
                Polygon polygon = new Polygon();
                polygon.add(new Point( new Coord3d(x[i][j], y[i][j], z[i][j]) ));
                polygon.add(new Point( new Coord3d(x[i][j+1], y[i][j+1], z[i][j+1]) ));
                polygon.add(new Point( new Coord3d(x[i+1][j+1], y[i+1][j+1], z[i+1][j+1]) ));
                polygon.add(new Point( new Coord3d(x[i+1][j], y[i+1][j], z[i+1][j]) ));
                polygons.add(polygon);
            }
        }

        Shape surface = new Shape(polygons);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,1f)));
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);

        Chart chart = new AWTChart(Quality.Intermediate);
        chart.add(surface);
        chart.getView().setBackgroundColor(Color.GRAY);
        AWTCameraMouseController mouseController = new AWTCameraMouseController();
        chart.addController(mouseController);
        chart.open(("Amplitude RP at " + FREQS[freqIdx] + " Hz"), 500, 500);

    }
}