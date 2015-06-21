package snapshot;

import java.awt.List;
import java.util.HashMap;
import java.util.UUID;

import Banque.Succursale;
import succursale.ActiveSuccursale;
import succursale.Transaction.Message;

public class messageResponseChandy extends Message {

	private String idSnapshot;

    private Succursale succrusale;
	private HashMap transactionEnAttente ;
	

	


    public messageResponseChandy(String idSnapshot) {
        super("Snapshot Response");
        this.idSnapshot = idSnapshot;
        Succursale thisSuccursale=ActiveSuccursale.getInstance().getThisSuccrusale();
         int idSuccursale;
         int montant;
        idSuccursale=thisSuccursale.getId();
        montant=thisSuccursale.getMontant();
        transactionEnAttente= ActiveSuccursale.getInstance().getTransactionDispatcher().getMapTransaction();
        succrusale=new Succursale(null,montant,thisSuccursale.getNom(),null);
        succrusale.setId(idSuccursale);


    }

    public String getIdSnapshot() {
		return idSnapshot;
	}

	public void setIdSnapshot(String idSnapshot) {
		this.idSnapshot = idSnapshot;
	}

    public Succursale getSuccrusale() {
        return succrusale;
    }

    public HashMap getTransactionEnAttente() {
        return transactionEnAttente;
    }
}
