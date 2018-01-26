package field_calculator;

import java.io.*;

import ch.systemsx.cisd.hdf5.*;


public class CreateSoundSource {
	
	public static void main(String[] args) {
		//String name = args[0];
		//String fileName = args[1];
		String name = "debg";
		String fileName = "fly6.h5";
		CreateSoundSource newSrc = new CreateSoundSource();
		newSrc.newFile(name, fileName);
	}
	

	private void newFile(String name, String fileName) {
		try {
			IHDF5SimpleReader reader = HDF5Factory.openForReading(fileName);
			double[] sensitivity = reader.readDoubleArray("/sens");
			double[] impedance = reader.readDoubleArray("/imped");
			double[] sizes = reader.readDoubleArray("/sizes");
			double weight = reader.readDouble("/weight");			
			double[][][] amplitudeRP = new double[32][181][360];
			double[][][] phaseRP = new double[32][181][360];
			
			
			for(int freq = 1; freq < (impedance.length + 1); freq++) {
				amplitudeRP[freq-1] = reader.readDoubleMatrix(("/amplRP" + freq));
				phaseRP[freq-1] = reader.readDoubleMatrix(("/phaseRP" + freq));
			}

			reader.close();
			
			SoundSource newSource = new SoundSource(name);
			newSource.setAmplitudeRP(amplitudeRP);
			newSource.setPhaseRP(phaseRP);
			newSource.setImpedance(impedance);
			newSource.setSensitivity(sensitivity);
			newSource.setSizes(sizes);
			newSource.setWeight(weight);

			//newSource.plotAmplitudeRP(5000);
			newSource.plotSensitivity();
			
			FileOutputStream fileStream = new FileOutputStream((name + ".ser"));
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(newSource);
			os.close();
		
		} catch (Exception ex) {ex.printStackTrace();}

		
	}

}
