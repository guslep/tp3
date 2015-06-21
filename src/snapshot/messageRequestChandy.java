package snapshot;

import java.io.Serializable;
import java.util.UUID;

import succursale.Transaction.Message;

public class messageRequestChandy extends Message implements Serializable{

	private String idSnapshot;

    public messageRequestChandy(String type, String idSnapshot) {
        super(type);
        this.idSnapshot = idSnapshot;
    }

    public String getIdSnapShot(){
		return this.idSnapshot;
	}
	
	public void setIdSnapShot(String idSnapShot){
		this.idSnapshot = idSnapShot;
	}
}
