package field_calculator;

import org.jzy3d.chart.AWTChart;

import java.io.Serializable;

public abstract class Source implements Serializable {

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


    public Source(int num, String name_, double[] pos, SoundSource ssObj) {
        number = num;
        position = pos;
        soundSourceObj = ssObj;
        name = name_;
    }
    public abstract void calcSourcePreasure(double[][] gridX, double[][] gridY, int freqIdx);

    public abstract AWTChart plotSource(AWTChart chart, double soundAxisLength, boolean isHighlighted);


}
