package succursale;

import Banque.Succursale;
import snapshot.ChandyManager;
import succursale.Transaction.TransactionDispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ActiveSuccursale {



    Succursale thisSuccrusale;
    TransactionDispatcher transactionDispatcher;
    static HashMap<Integer,SuccursaleClient> listeSuccursale=new HashMap<Integer, SuccursaleClient>();
    private String portNumber;
    private ChandyManager chandyManager;
    private int montantBanque;


    public HashMap<Integer, SuccursaleClient> getListeSuccursale() {
        return listeSuccursale;
    }

    static ActiveSuccursale instance;

    private ActiveSuccursale(){
        chandyManager=new ChandyManager();
    }

    public static ActiveSuccursale getInstance(){

        if (instance==null){
            instance= new ActiveSuccursale();
        }
     return  instance;
    }


    /**
     * print la liste des clients
     */
    public void printSuccursale(){
        printHashMap(listeSuccursale);
    }


    /***
     * imprime un hashmap recu en parametre
     * @param listeclient
     */
    private static void printHashMap(HashMap listeclient){
        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            SuccursaleClient currentClient=(SuccursaleClient)pair.getValue();

            System.out.println("Id: " + currentClient.getId());
            System.out.print( "Nom succursale: "+currentClient.getNom());
            System.out.print( " Montant : "+currentClient.getMontant());
            System.out.println( " Adresse IP: "+currentClient.getSuccursaleIPAdresse().getHostAddress()+"\n");

        }

    }




    public Succursale getThisSuccrusale() {
        return thisSuccrusale;
    }

    public TransactionDispatcher getTransactionDispatcher() {
        return transactionDispatcher;
    }

    public void setTransactionDispatcher(TransactionDispatcher transactionDispatcher) {
        this.transactionDispatcher = transactionDispatcher;
    }

    public static void setListeSuccursale(HashMap<Integer, SuccursaleClient> listeSuccursale) {
        ActiveSuccursale.listeSuccursale = listeSuccursale;
    }

    public void setThisSuccrusale(Succursale thisSuccrusale) {
        this.thisSuccrusale = thisSuccrusale;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public ChandyManager getChandyManager() {
        return chandyManager;
    }

    public void setChandyManager(ChandyManager chandyManager) {
        this.chandyManager = chandyManager;
    }

    public int getMontantBanque() {
        return montantBanque;
    }

    public void setMontantBanque(int montantBanque) {
        this.montantBanque = montantBanque;
    }

    //TODO retirer les system.out et les envoyer dans les logs à la places

//TODO banque envoie sont montant lorsqu'une nouvelle sucursale join
    //TODO ajouter les erreurs aka perte d'argent
    //TODO ajouter commande de snapshot


}

