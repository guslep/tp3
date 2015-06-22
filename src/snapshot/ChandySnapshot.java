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

        addThisSuccursaleSnapshot(ActiveSuccursale.getInstance().getThisSuccrusale());
        System.out.println(ActiveSuccursale.getInstance().getThisSuccrusale().getMontant()  );


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


        }
    }

    private void addThisSuccursaleSnapshot(Succursale succursale){
       Succursale dummySuccursale=new Succursale(succursale.getSuccursaleIPAdresse(),succursale.getMontant(),succursale.getNom(),succursale.getPort());
        tableauSuccursale[succursale.getId()]=dummySuccursale;
        System.out.println("Succursale money"+succursale.getMontant());
        creerCanaux(ActiveSuccursale.getInstance().getTransactionDispatcher().getMapTransaction());


    }




    private void creerCanaux( HashMap mapPendingTransaction){

        Iterator mapPIterator =mapPendingTransaction.entrySet().iterator();
        System.out.println(mapPendingTransaction.size()+" creating canal for "+ id.toString());

        System.out.println(mapPendingTransaction.size()+" transaction en attente");


        while (mapPIterator.hasNext()) {

            Map.Entry pair = (Map.Entry) mapPIterator.next();
            Transaction currentTransaction = (Transaction) pair.getValue();

            int sender=Math.min(currentTransaction.getIdFrom(),currentTransaction.getIdTo());
            int to=Math.max(currentTransaction.getIdFrom(), currentTransaction.getIdTo());
            String clef=Integer.toString(sender)+"-"+Integer.toString(to);
            if(listeCanal.get(clef)==null){
                listeCanal.put(clef,new Canal(sender,to,currentTransaction.getMontant()));
                System.out.println("Creating canal"+clef +" contenant " +currentTransaction.getMontant());
                System.out.println("Canal " +clef +" contains "+listeCanal.get(clef).getMontant());

            }else{
                listeCanal.get(clef).addMontant(currentTransaction.getMontant());
                System.out.println("adding to " + clef + " contenant " + currentTransaction.getMontant());
                System.out.println("Canal " +clef +" contains "+listeCanal.get(clef).getMontant());
            }

        }



    }





    public void manageResponse(messageResponseChandy response){
        System.out.println("Processing response for " + response.getIdSnapshot());
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
    		System.out.print("Canal " + pair.getKey().toString());
    		System.out.println(": " + ((Canal)pair.getValue()).getMontant() + "$");
    		
    		montantTotalSnapshot += ((Canal)pair.getValue()).getMontant();
    	}
    	if (montantTotalSnapshot == this.montantBanque){
    		System.out.println("ETAT GLOBAL COHERENT");
    	} else{
    		System.out.println("ETAT GLOBAL INCOHERENT");
    	}

        System.out.println(this.montantBanque);
        System.out.println(montantTotalSnapshot);
    }
}
