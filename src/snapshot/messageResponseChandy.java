package snapshot;

import java.awt.List;
import java.util.UUID;

import succursale.Transaction.Message;

public class messageResponseChandy extends Message {

	private String idSnapshot;
	private int idSuccursale;
	private int montant;
	private List transactionEnAttente = new List();
	
	public messageResponseChandy(){
		transactionEnAttente = new List();
	}
	
	public messageResponseChandy(int idSuccursale, int montant){
		this.idSnapshot = UUID.randomUUID().toString();
		this.idSuccursale = idSuccursale;
		this.montant = montant;
		transactionEnAttente.add(idSnapshot);
	}

	public String getIdSnapshot() {
		return idSnapshot;
	}

	public void setIdSnapshot(String idSnapshot) {
		this.idSnapshot = idSnapshot;
	}

	public int getIdSuccursale() {
		return idSuccursale;
	}

	public void setIdSuccursale(int idSuccursale) {
		this.idSuccursale = idSuccursale;
	}

	public int getMontant() {
		return montant;
	}

	public void setMontant(int montant) {
		this.montant = montant;
	}

	public List getTransactionEnAttente() {
		return transactionEnAttente;
	}

	public void setTransactionEnAttente(List transactionEnAttente) {
		this.transactionEnAttente = transactionEnAttente;
	}
}
