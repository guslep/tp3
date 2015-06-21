package snapshot;

import java.awt.List;
import java.util.*;

import Banque.Succursale;
import succursale.ActiveSuccursale;
import succursale.SuccursaleClient;
import succursale.Transaction.Message;
import succursale.Transaction.Transaction;

public class ChandySnapshot extends Observable{

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
        envoieDemandeSnapshot();

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
        addThisSuccursaleSnapshot(ActiveSuccursale.getInstance().getThisSuccrusale());
        Message snapshotRequest=new messageRequestChandy("Snapshot Request",this.id.toString());
      HashMap listeSuccursale= ActiveSuccursale.getInstance().getListeSuccursale();
        Iterator iterateurSuccursale=listeSuccursale.entrySet().iterator();
        while (iterateurSuccursale.hasNext()){
            Map.Entry pair = (Map.Entry) iterateurSuccursale.next();
            SuccursaleClient succursale=(SuccursaleClient)pair.getValue();
            succursale.getConnectionThread().sendMessage(snapshotRequest);


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
        int index=1;
        boolean snapshotFinished=true;
        while(index<tableauSuccursale.length){
            if(tableauSuccursale[index]==null)
            {index=tableauSuccursale.length;
                snapshotFinished=false;
            }
            index++;
        }
        if(snapshotFinished){

            showSnapshotResult();
            notifyObservers(this.getId());
        }

    }

    private void showSnapshotResult(){
    	Iterator canauxIterator = this.listeCanal.entrySet().iterator();
    	int montantTotalSnapshot = 0;
    	
    	System.out.println("Succursale d'origine de la capture: #" + 
    			ActiveSuccursale.getInstance().getThisSuccrusale().getId());
    	
    	for (int i=1; i<this.tableauSuccursale.length; i++){
    		System.out.println("Succursale #" + i + ": " + 
    				tableauSuccursale[i].getMontant() + "$");
    		
    		montantTotalSnapshot += tableauSuccursale[i].getMontant();
    	}
    	
    	while (canauxIterator.hasNext()){
    		Map.Entry pair = (Map.Entry) canauxIterator.next();
    		System.out.print("Canal S" + pair.getKey().toString());
    		System.out.println(": " + ((Canal)pair.getValue()).getMontant() + "$");
    		
    		montantTotalSnapshot += ((Canal)pair.getValue()).getMontant();
    	}
    	if (montantTotalSnapshot == this.montantBanque){
    		System.out.println("�TAT GLOBAL COH�RENT");
    	} else{
    		System.out.println("�TAT GLOBAL INCOH�RENT");
    	}

        System.out.println(this.montantBanque);
        System.out.println(montantTotalSnapshot);
    }
}
