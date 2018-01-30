package field_calculator;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Area implements Serializable{
    public double xSize;
    public double ySize;
    public double xStep;
    public double yStep;
    public double zSize;
    public double zStep;
    public double[][] gridX;
    public double[][] gridY;
    public double[][] gridZ;
    public double[][][] sumFieldAbs;
    public double[][][] sumFieldPhase;
    public HashMap<Integer, Source> sources = new HashMap<>();

    public static final double[] FREQS = {16, 20, 25, 31.5, 40, 50, 63, 80, 100, 125,
            160, 200, 250, 315, 400, 500, 630, 800, 1e3, 1.25e3,
            1.6e3, 2e3, 2.5e3, 3.15e3, 4e3, 5e3, 6.3e3, 8e3, 1e4, 1.25e4,
            1.6e4, 2e4};
    public static final double P0 = 2e-5;





    public void addSource(Source source, int place) {
        sources.put(place, source);
        //sourcesCounter++;
    }

    public abstract void calcSummPreasure(int freqIdx);

    public abstract ImageView plotSurface();

    public abstract ImageView plotSources();

    public abstract ImageView plotLightedSource(int sourceNum);

    public abstract ImageView plotSingleSourceField(int freqIdx, int sourceNum);

    public abstract ImageView plotSummaryField(int freqIdx);
}
