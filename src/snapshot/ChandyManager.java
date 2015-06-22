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
        System.out.println("new snapshot "+ snapshot.getId());

	}
	
	public void removeSnapShot(String uuidSnapShot){
		iHashSnapShot.remove(uuidSnapShot);
	}

    public void dispatchChandyResponse(messageResponseChandy response){



        if(iHashSnapShot.get(response.getIdSnapshot())!=null){

            iHashSnapShot.get(response.getIdSnapshot()).manageResponse(response);
            System.out.println("Good dispatch respons "+response.getIdSnapshot());

        }
        else{

            System.out.println("Error "+response.getIdSnapshot());
        }


    }


    @Override
    public void update(Observable o, Object arg) {
        String uuid=(String) arg;
      //  iHashSnapShot.remove(uuid);
    }
}
