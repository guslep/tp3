package succursale;

import Banque.Succursale;
import org.omg.PortableServer.THREAD_POLICY_ID;
import succursale.Transaction.Menu;
import succursale.Transaction.TransactionDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class Client {



    Succursale thisSuccrusale;
    TransactionDispatcher transactionDispatcher;
    static HashMap<Integer,SuccursaleClient> listeSuccursale=new HashMap<Integer, SuccursaleClient>();
    private String portNumber;


    public HashMap<Integer, SuccursaleClient> getListeSuccursale() {
        return listeSuccursale;
    }

    static Client instance;

    private Client(){

    }

    public static Client getInstance(){

        if (instance==null){
            instance= new Client();
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
        Client.listeSuccursale = listeSuccursale;
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
}

