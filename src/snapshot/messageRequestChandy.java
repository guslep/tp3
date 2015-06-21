package snapshot;

import java.util.UUID;

import succursale.Transaction.Message;

public class messageRequestChandy extends Message{

	private String idSnapshot;
	
	public messageRequestChandy(){
		this.idSnapshot = UUID.randomUUID().toString();
	}
	
	public String getIdSnapShot(){
		return this.idSnapshot;
	}
	
	public void setIdSnapShot(String idSnapShot){
		this.idSnapshot = idSnapShot;
	}
}
