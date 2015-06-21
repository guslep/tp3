package snapshot;

import java.util.HashMap;

public class ChandyManager {

	private HashMap<String,ChandySnapshot>  iHashSnapShot=new HashMap<String, ChandySnapshot>();

	
	public ChandyManager(){
		iHashSnapShot = new HashMap();

        new Thread(
                new AutoChandySnapshot(this)
        ).start();


	}

	public HashMap getHashMapSnapShot(){
		return this.iHashSnapShot;
	}
	

	
	public void setHashMapSnapShot(HashMap newHashMap){
		this.iHashSnapShot = newHashMap;
	}
	

	
	public void creerSnapShot(){
		ChandySnapshot snapshot= new ChandySnapshot();
        iHashSnapShot.put(snapshot.getId().toString(),snapshot);
	}
	
	public void removeSnapShot(String uuidSnapShot){
		iHashSnapShot.remove(uuidSnapShot);
	}

    public void dispatchChandyResponse(messageResponseChandy response){

        iHashSnapShot.get(response.getIdSnapshot());
    }

}
