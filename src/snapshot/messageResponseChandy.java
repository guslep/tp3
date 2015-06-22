package snapshot;

import java.awt.List;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import Banque.Succursale;
import succursale.ActiveSuccursale;
import succursale.Transaction.Message;

public class messageResponseChandy extends Message implements Serializable{

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
        System.out.println("nb transac "+transactionEnAttente.size()+" montant "+montant+" id "+idSuccursale);
        succrusale=new Succursale(null,montant,thisSuccursale.getNom(),thisSuccursale.getPort());
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
