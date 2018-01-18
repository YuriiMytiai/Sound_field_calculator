package field_calculator;

import java.io.*;

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
        } else {System.err.println("Weight must be positive value");}
    }

    public void setImpedance(double[] s_impedance) {
        if(s_impedance.length == FREQS.length) {
            impedance = s_impedance;
        } else {System.err.println("Vector of impedance values must have " + FREQS.length + " elements");}
    }

    public void setSensitivity(double[] s_sensitivity) {
        if(s_sensitivity.length == FREQS.length) {
            sensitivity = s_sensitivity;
        } else {System.err.println("Vector of sensitivity values must have " + FREQS.length + " elements");}
    }

    public void setAmplitudeRP(double[][][] s_amplitudeRP) {
        if((s_amplitudeRP.length == amplitudeRP.length) & (s_amplitudeRP[0].length == amplitudeRP[0].length) & (s_amplitudeRP[0][0].length == amplitudeRP[0][0].length)) {
            amplitudeRP = s_amplitudeRP;
        } else {System.err.println("Matrix of RP values must have " + amplitudeRP.length + "x" + amplitudeRP[0].length + "x" + amplitudeRP[0][0].length + " size");}
    }

    public void setPhaseRP(double[][][] s_phaseRP) {
        if((s_phaseRP.length == phaseRP.length) & (s_phaseRP[0].length == phaseRP[0].length) & (s_phaseRP[0][0].length == phaseRP[0][0].length)) {
            phaseRP = s_phaseRP;
        } else {System.err.println("Matrix of RP values must have " + phaseRP.length + "x" + phaseRP[0].length + "x" + phaseRP[0][0].length + " size");}
    }

    public void setSizes(double[] s_sizes) {
        if ((s_sizes.length == sizes.length) & !(anyNegative1D(s_sizes))) {
        } else {
        sizes = s_sizes;
	        for(int i = 0; i < sizes.length; i++) {
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
    
    public double[] getSensitivity( ) {
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

    public static boolean anyNegative1D(double[] arr) {
        boolean ans = false;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] < 0) {
                ans = true;
                break;
            }
        }
        return ans;
    }

}
