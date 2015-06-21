package snapshot;

import java.util.UUID;

import succursale.Transaction.Message;

public class messageRequestChandy extends Message{

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
