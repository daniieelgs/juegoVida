package jocVida;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//©Daniel Garcia (daniieelgs@gmail.com)

public class config {

	public static dataConfig createDefaultConfig() {
		
		return new dataConfig();
		
	}
	
	public static dataConfig createDefaultConfig(int ruleLive, int ruleDie, int mode, int initialGroup, int vel, int sizeX, int sizeY) {
		
		return new dataConfig(ruleLive, ruleDie, mode, initialGroup, vel, sizeX, sizeY);
		
	}
	
	public static dataConfig readConfigurationFile(String path) {
		
		dataConfig config = null;
		
		try {
			ObjectInputStream load_configuration = new ObjectInputStream(new FileInputStream(path));

			config = (dataConfig) load_configuration.readObject();
			load_configuration.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return config;
		
	}
	
	public static void saveConfigurationFile(String path, dataConfig conf) {
		
		try {
			ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(path));
			
			save.writeObject(conf);
			save.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
