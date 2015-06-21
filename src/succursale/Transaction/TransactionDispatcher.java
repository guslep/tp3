package succursale.Transaction;

import succursale.Client;
import succursale.SuccursaleClient;

import java.util.*;

/**
 * Created by Guillaume on 2015-06-12.
 */
public class TransactionDispatcher implements Runnable {

    private int transactionDelay=5;
    private int transactionIntervalMin =5;
    private int transactionIntervalMax =10;

    /**
     *
     * @param transactionIntervalMax temps Max entre les transactions
     * @param transactionIntervalMin Temps Minimum entre les transactions
     */

    public TransactionDispatcher(int transactionIntervalMax, int transactionIntervalMin) {
        this.transactionIntervalMax = transactionIntervalMax;
        this.transactionIntervalMin = transactionIntervalMin;

    }

    /**
     *
     * @param transactionDelay delai d'envoi lorsqu'une transaction est cree
     */
    public TransactionDispatcher( int transactionDelay) {

        this.transactionDelay = transactionDelay;
    }

    /**
     *
     * @param transactionIntervalMax temps Max entre les transactions
     * @param transactionIntervalMin Temps Minimum entre les transactions
     @param transactionDelay delai d'envoi lorsqu'une transaction est cree

     */

    public TransactionDispatcher(  int transactionDelay, int transactionIntervalMin, int transactionIntervalMax) {

        this.transactionDelay = transactionDelay;
        this.transactionIntervalMin = transactionIntervalMin;
        this.transactionIntervalMax = transactionIntervalMax;
    }

    public TransactionDispatcher() {

    }

    // TODO:  Ajouter une maniere de creer des transactions manuellement

    /**
     * Cree une transaction manuelle
     * @param idFrom id F de la succursale qui envoie
     * @param amount montant envoyé
     * @param idSuccursale id de la succcursale à qui on envoie
     */
    public void createManualTransaction(int idFrom,int amount, int idSuccursale){
         int whitdrew= Client.getInstance().getThisSuccrusale().doWHitdraw(amount);

        if(whitdrew>0) {
            Transaction transaction = new Transaction(idFrom, idSuccursale, amount);
            TimerTaskTransaction task=new TimerTaskTransaction(transaction);
            Timer timer=new Timer(true);
            timer.schedule(task,transactionDelay*1000);
            System.out.println("Transaction  created manually");

        }



    }




    private HashMap<UUID,Transaction> mapTransaction= new HashMap<UUID,Transaction>();

    /**
     * Cree une transaction de mani;ere aléatoire à des temps aléatoires
     */
    @Override
    public void run() {
        while (true){
            Transaction transaction=createNewRandomTransaction();
            if(transaction!=null){
                TimerTaskTransaction task=new TimerTaskTransaction(transaction);
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

    /**
     * Cree une transaction vers une succrusale aleatoire
     * @return
     */
    private Transaction createNewRandomTransaction(){
       HashMap listeSuccursale=Client.getInstance().getListeSuccursale();
        int succursaleNumber=listeSuccursale.size();
        if(succursaleNumber==0){
            return null;//si il n'y a pas d'autre succursale on ne fait pas de transaction
        }
        Random rand=new Random();

        int valueGenerated=rand.nextInt(Integer.MAX_VALUE);///random number
        int succursaleTotransfer= (valueGenerated %succursaleNumber)+1;//trouve quel succursale de la liste on veut
        System.out.println("succNBr " + succursaleNumber+" val"+ valueGenerated);

        System.out.println("creating new transaction to "+succursaleTotransfer +" in the list" );

        Iterator it = listeSuccursale.entrySet().iterator();
        SuccursaleClient succursaleChoisie=null;
        int index=1;
        while (it.hasNext()&&index<=succursaleTotransfer){//on trouve la X eme succursale ou X est la valeur random trouvé
            Map.Entry pair = (Map.Entry)it.next();
            if(index==succursaleTotransfer){

                succursaleChoisie=(SuccursaleClient) pair.getValue();
                System.out.println("creating new transaction to"+succursaleChoisie.getId() );
                System.out.println("max amount possible is "+Client.getInstance().getThisSuccrusale().getMontant() );

            }
            index++;

        }
        Transaction transaction=null;
        int montantTransfer= whitdrawRandomAmount();
        if(montantTransfer!=-1&&succursaleChoisie!=null){

            transaction=new Transaction(Client.getInstance().getThisSuccrusale().getId(),succursaleChoisie.getId(),montantTransfer);
            mapTransaction.put(transaction.getUUID(),transaction);

        }








    return transaction;
    }

    /**
     * effectu um retrai d'un montant aleatoire
     * @return
     */
    private int whitdrawRandomAmount(){
        int amountTransfer=0;

       int maxMontant= Client.getInstance().getThisSuccrusale().getMontant();
        if(maxMontant==0){
            return -1;///si il y a pas d'argent on arrête
        }
        Random rand=new Random();

        int amountWhitdrew=0;
        do {
            amountWhitdrew=Client.getInstance().getThisSuccrusale().doWHitdraw(rand.nextInt()%maxMontant+1);
        }
        while (amountWhitdrew==-1);





        return amountWhitdrew;
    }


    /**
     * tâche exécuté lotdwir l
     */
    private class TimerTaskTransaction extends TimerTask{

        Transaction transactionTocomplete;


        private TimerTaskTransaction(Transaction transactionTocomplete) {
            this.transactionTocomplete = transactionTocomplete;

        }

        /**
         * Lorsuqe le delai est passé
         */
        @Override
        public void run() {
            System.out.println("tasked wakeup");
            SuccursaleClient succursaleTotransfer=Client.getInstance().getListeSuccursale().get(transactionTocomplete.getIdTo());
            succursaleTotransfer.getConnectionThread().sendMessage(transactionTocomplete);
            mapTransaction.remove(transactionTocomplete.getUUID());

        }

    }


}
