package field_calculator;

public class RotMatrix {
	
	public static double[][] getRotX(double ang) {
		double sinAng = Math.sin(Math.toRadians(ang));
		double cosAng = Math.cos(Math.toRadians(ang));
		double[][] rotX = {{1, 0, 0},
							{0, cosAng, -sinAng},
							{0, sinAng, cosAng}};
		return rotX;
	}
	
	public static double[][] getRotY(double ang) {
		double sinAng = Math.sin(Math.toRadians(ang));
		double cosAng = Math.cos(Math.toRadians(ang));
		double[][] rotY = {{cosAng, 0, sinAng},
							{0, 1, 0},
							{-sinAng, 0, cosAng}};
		return rotY;
	}
	
	public static double[][] getRotZ(double ang) {
		double sinAng = Math.sin(Math.toRadians(ang));
		double cosAng = Math.cos(Math.toRadians(ang));
		double[][] rotZ = {{cosAng, -sinAng, 0},
							{sinAng, cosAng, 0},
							{0, 0, 1}};
		return rotZ;
	}

}
