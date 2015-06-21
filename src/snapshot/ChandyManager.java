package snapshot;

import java.util.HashMap;

public class ChandyManager {

	private HashMap iHashSnapShot;
	private ChandySnapshot chandySnapShot;
	
	public ChandyManager(){
		iHashSnapShot = new HashMap();
	}

	public HashMap getHashMapSnapShot(){
		return this.iHashSnapShot;
	}
	
	public ChandySnapshot getChandySnapShot(){
		return this.chandySnapShot;
	}
	
	public void setHashMapSnapShot(HashMap newHashMap){
		this.iHashSnapShot = newHashMap;
	}
	
	public void setChandySnapShot(ChandySnapshot chandySnapShot){
		this.chandySnapShot = chandySnapShot;
	}
	
	public void creerSnapShot(){
		chandySnapShot = new ChandySnapshot();
	}
	
	public void removeSnapShot(String uuidSnapShot){
		iHashSnapShot.remove(uuidSnapShot);
	}
}
