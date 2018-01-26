package field_calculator;


public class Source {
	
	public int number;
	public String name;
	public double[] position = {0, 0, 0};
	public int phi0 = 0;
	public int theta0 = 0;
	public double kAmp = 0;
	public double tau0 = 0;
	public SoundSource soundSourceObj;
	public double[][][] preasureAbs;
	public double[][][] preasurePhase;
	public double c0 = 340;
	
	public Source(int num, String name_, double[] pos, SoundSource ssObj) {
		number = num;
		position = pos;
		soundSourceObj = ssObj;
		name = name_;
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
