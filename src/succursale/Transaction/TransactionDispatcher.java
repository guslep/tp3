package succursale.Transaction;

import succursale.Client;
import succursale.SuccursaleClient;

import java.util.*;

/**
 * Created by Guillaume on 2015-06-12.
 */
public class TransactionDispatcher implements Runnable {
   private Client client;
    private int transactionDelay=5;
    private int transactionIntervalMin =5;
    private int transactionIntervalMax =10;

    public TransactionDispatcher(int transactionIntervalMax, int transactionIntervalMin, Client client) {
        this.transactionIntervalMax = transactionIntervalMax;
        this.transactionIntervalMin = transactionIntervalMin;
        this.client = client;
    }

    public TransactionDispatcher(Client client, int transactionDelay) {
        this.client = client;
        this.transactionDelay = transactionDelay;
    }

    public TransactionDispatcher(Client client, int transactionDelay, int transactionIntervalMin, int transactionIntervalMax) {
        this.client = client;
        this.transactionDelay = transactionDelay;
        this.transactionIntervalMin = transactionIntervalMin;
        this.transactionIntervalMax = transactionIntervalMax;
    }

    // TODO:  Ajouter une maniere de creer des transactions manuellement




    private HashMap<UUID,Transaction> mapTransaction= new HashMap<UUID,Transaction>();
    public TransactionDispatcher(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true){
            Transaction transaction=createNewRandomTransaction();
            if(transaction!=null){
                TimerTaskTransaction task=new TimerTaskTransaction(transaction,client);
                Timer timer=new Timer(true);
                timer.schedule(task,transactionDelay*1000);
                System.out.println("Transaction scheduled");
            }





            Random rand=new Random();
            int delay=rand.nextInt()% transactionIntervalMin +(transactionIntervalMax-transactionIntervalMin)+1;
            try {
                synchronized (this){
                    this.wait(delay*1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }











    }


    private Transaction createNewRandomTransaction(){
       HashMap listeSuccursale=client.getListeSuccursale();
        int succursaleNumber=listeSuccursale.size();
        if(succursaleNumber==0){
            return null;//si il n'y a pas d'autre succursale on ne fait pas de transaction
        }
        Random rand=new Random();
       int succursaleTotransfer= rand.nextInt()% succursaleNumber+1;

        System.out.println("creating new transaction to "+succursaleTotransfer +" in the list" );

        Iterator it = listeSuccursale.entrySet().iterator();
        SuccursaleClient succursaleChoisie=null;
        int index=1;
        while (it.hasNext()||index<=succursaleTotransfer){
            if(index==succursaleTotransfer){
                Map.Entry pair = (Map.Entry)it.next();
                succursaleChoisie=(SuccursaleClient) pair.getValue();
                System.out.println("creating new transaction to"+succursaleChoisie.getId() );
                System.out.println("max amount possible is "+client.getThisSuccrusale().getMontant() );

            }
            index++;

        }
        Transaction transaction=null;
        int montantTransfer= whitdrawRandomAmount();
        if(montantTransfer!=-1&&succursaleChoisie!=null){

            transaction=new Transaction(client.getThisSuccrusale().getId(),succursaleChoisie.getId(),montantTransfer);
            mapTransaction.put(transaction.getUUID(),transaction);

        }








    return transaction;
    }


    private int whitdrawRandomAmount(){
        int amountTransfer=0;

       int maxMontant= client.getThisSuccrusale().getMontant();
        if(maxMontant==0){
            return -1;///si il y a pas d'argent on arrête
        }
        Random rand=new Random();

        int amountWhitdrew=0;
        do {
            amountWhitdrew=client.getThisSuccrusale().doWHitdraw(rand.nextInt()%maxMontant+1);
        }
        while (amountWhitdrew==-1);





        return amountWhitdrew;
    }


    private class TimerTaskTransaction extends TimerTask{

        Transaction transactionTocomplete;
        Client client;

        private TimerTaskTransaction(Transaction transactionTocomplete, Client client) {
            this.transactionTocomplete = transactionTocomplete;
            this.client = client;
        }

        @Override
        public void run() {
            System.out.println("tasked wakeup");
            SuccursaleClient succursaleTotransfer=this.client.getListeSuccursale().get(transactionTocomplete.getIdTo());
            succursaleTotransfer.getConnectionThread().sendMessage(transactionTocomplete);
            mapTransaction.remove(transactionTocomplete.getUUID());

        }

    }


}
