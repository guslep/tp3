package snapshot;

import java.awt.List;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import Banque.Succursale;
import succursale.ActiveSuccursale;
import succursale.Transaction.Message;
import succursale.Transaction.Transaction;

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

        HashMap<UUID,Transaction> copyTransactionAttente=new HashMap<UUID, Transaction>();

        Iterator mapPIterator = ActiveSuccursale.getInstance().getTransactionDispatcher().getMapTransaction().entrySet().iterator();



        while (mapPIterator.hasNext()) {

            Map.Entry pair = (Map.Entry) mapPIterator.next();
            Transaction currentTransaction = (Transaction) pair.getValue();
            Transaction  copyTransaction=new Transaction(currentTransaction.getIdFrom(),currentTransaction.getIdTo(),currentTransaction.getMontant());
            copyTransactionAttente.put(((UUID)pair.getKey()),copyTransaction);






        }


        transactionEnAttente= copyTransactionAttente;

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
