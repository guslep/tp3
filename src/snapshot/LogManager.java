package snapshot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {

	private static String logPath = "logChandyLamport.txt";
	private BufferedWriter out; 
	private static LogManager instance = null;
	
	protected LogManager (){
		File fileTemp = new File (this.logPath);
		if (fileTemp.exists()){
			fileTemp.delete();
		}
	}
	
	public static LogManager getInstance(){
		if (instance==null){
			instance = new LogManager();
		}
		return instance;
	}
	
	public void writeLogLine (String logMessage){
		try{
			FileWriter fstream = new FileWriter(this.logPath, true);
			out = new BufferedWriter(fstream);
			out.write(logMessage + "\n");
		} 
		catch (IOException e){
			System.out.println("Error: " + e.getMessage());
		}
		finally{
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					System.out.println("Error: " + e.getMessage());
				}
			}
		}
	}
}
