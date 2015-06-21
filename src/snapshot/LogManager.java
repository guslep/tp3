package snapshot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {

	private static String logPath;
	private BufferedWriter out; 
	
	public LogManager (String logPath){
		this.logPath = logPath;
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
