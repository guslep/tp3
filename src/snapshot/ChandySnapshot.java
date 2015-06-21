package snapshot;

import java.awt.List;
import java.util.*;

import Banque.Succursale;
import succursale.ActiveSuccursale;
import succursale.SuccursaleClient;
import succursale.Transaction.Message;
import succursale.Transaction.Transaction;

public class ChandySnapshot {

	private int montantBanque;
	private int nombreSuccursaleAttendu;
	private HashMap<String,Canal> listeCanal;
	private boolean snapshotCompleter;
	private Succursale [] tableauSuccursale;
    private UUID id;
	
	public ChandySnapshot(){




         montantBanque= ActiveSuccursale.getInstance().getMontantBanque();
        nombreSuccursaleAttendu=ActiveSuccursale.getInstance().getListeSuccursale().size()+2;
        snapshotCompleter=false;
        tableauSuccursale=new Succursale[nombreSuccursaleAttendu];
        listeCanal=new HashMap<String,Canal>();
        id=java.util.UUID.randomUUID();

		
	}



	
	public int getMontantBanque() {
		return montantBanque;
	}

	public void setMontantBanque(int montantBanque) {
		this.montantBanque = montantBanque;
	}

	public int getNombreSuccursaleAttendu() {
		return nombreSuccursaleAttendu;
	}

	public void setNombreSuccursaleAttendu(int nombreSuccursaleAttendu) {
		this.nombreSuccursaleAttendu = nombreSuccursaleAttendu;
	}



	public boolean isSnapshotCompleter() {
		return snapshotCompleter;
	}

	public void setSnapshotCompleter(boolean snapshotCompleter) {
		this.snapshotCompleter = snapshotCompleter;
	}

	public Succursale[] getTableauSuccursale() {
		return tableauSuccursale;
	}

	public void setTableauSuccursale(Succursale[] tableauSuccursale) {
		this.tableauSuccursale = tableauSuccursale;
	}

	private void addMessage(){
		
	}

    public UUID getId() {
        return id;
    }

    private void envoieDemandeSnapshot(){

        Message snapshotRequest=new messageRequestChandy("Snapshot Request",this.id.toString());
      HashMap listeSuccursale= ActiveSuccursale.getInstance().getListeSuccursale();
        Iterator iterateurSuccursale=listeSuccursale.entrySet().iterator();
        while (iterateurSuccursale.hasNext()){
            Map.Entry pair = (Map.Entry) iterateurSuccursale.next();
            SuccursaleClient succursale=(SuccursaleClient)pair.getValue();
            succursale.getConnectionThread().sendMessage(snapshotRequest);
            addThisSuccursaleSnapshot(ActiveSuccursale.getInstance().getThisSuccrusale());

        }


    }

    private void addThisSuccursaleSnapshot(Succursale succursale){
        tableauSuccursale[succursale.getId()]=succursale;
        creerCanaux(ActiveSuccursale.getInstance().getTransactionDispatcher().getMapTransaction());


    }




    private void creerCanaux( HashMap mapPendingTransaction){




        Iterator mapPIterator =mapPendingTransaction.entrySet().iterator();
        while (mapPIterator.hasNext()) {

            Map.Entry pair = (Map.Entry) mapPIterator.next();
            Transaction currentTransaction = (Transaction) pair.getValue();

            int sender=Math.min(currentTransaction.getIdFrom(),currentTransaction.getIdTo());
            int to=Math.max(currentTransaction.getIdFrom(), currentTransaction.getIdTo());
            String clef=Integer.toString(sender)+"-"+Integer.toString(to);
            if(listeCanal.get(clef)==null){
                listeCanal.put(clef,new Canal(sender,to,currentTransaction.getMontant()));

            }else{
                listeCanal.get(clef).addMontant(currentTransaction.getMontant());

            }

        }


        }





    public void manageResponse(messageResponseChandy response){

        tableauSuccursale[response.getSuccrusale().getId()]=response.getSuccrusale();
        creerCanaux(response.getTransactionEnAttente());


    }
}
