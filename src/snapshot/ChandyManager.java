package snapshot;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ChandyManager implements Observer {

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
        snapshot.addObserver(this);
        iHashSnapShot.put(snapshot.getId().toString(),snapshot);

	}
	
	public void removeSnapShot(String uuidSnapShot){
		iHashSnapShot.remove(uuidSnapShot);
	}

    public void dispatchChandyResponse(messageResponseChandy response){

        iHashSnapShot.get(response.getIdSnapshot()).manageResponse(response);


    }


    @Override
    public void update(Observable o, Object arg) {
        String uuid=(String) arg;
      //  iHashSnapShot.remove(uuid);
    }
}
