package succursale;

import Banque.Succursale;
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

/**
 * Created by Guillaume on 2015-06-19.
 */
public class banqueListner implements Runnable{

    private boolean finish=false;
    PrintWriter out = null;
    BufferedReader in = null;
    private boolean firstRun=true;
    Socket echoSocket = null;
    String succursaleName;
    public banqueListner(String serverHostname,Integer montant,String succursaleName,String portNumber ) {


        System.out.println ("Essai de se connecter a l'hote " +
                serverHostname + " au port 10118.");
        this.succursaleName=succursaleName;




        try {
            echoSocket = new Socket(serverHostname, 10118);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: " + serverHostname);
            System.exit(1);
        }



        out.println(montant.toString() + "," + succursaleName + "," + portNumber);


        //out.println(montant.toString()+","+succursaleName);

        System.out.println("Liste des succursales");

    }

    /**
     * Ecoute pour les message envoyé par le serveur
     */
    @Override
    public void run()
    { HashMap<Integer,SuccursaleClient> listeSuccursale;
        while(!finish)
        {

            String recuTest= null;
            try {
                recuTest = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(recuTest);
            String recu[]=recuTest.split(";");
            listeSuccursale=Client.getInstance().getListeSuccursale();
            for(int index=0;index<recu.length;index++){
                String[] splitSuccursale=recu[index].split(",");

                if(!listeSuccursale.containsKey(Integer.parseInt(splitSuccursale[0]))){
                    if(splitSuccursale[1].equals(succursaleName)){

                        Succursale thisSuccrusale= null;
                        try {
                            thisSuccrusale = new Succursale(InetAddress.getByName(splitSuccursale[3]),
                                    Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        thisSuccrusale.setId(Integer.parseInt(splitSuccursale[0]));
                        Client.getInstance().setThisSuccrusale(thisSuccrusale);

                    }
                    else{
                        SuccursaleClient newSuccursale= null;
                        try {
                            newSuccursale = new SuccursaleClient(InetAddress.getByName(splitSuccursale[3]),
                                    Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        newSuccursale.setId(Integer.parseInt(splitSuccursale[0]));

                        listeSuccursale.put(Integer.parseInt(splitSuccursale[0]),newSuccursale);


                    }}



            }
            Client.getInstance().setListeSuccursale(listeSuccursale);

            Client.getInstance().printSuccursale();
            if(firstRun){
                System.out.println("Creating Connection");

                createConnection(listeSuccursale);
                new Thread(
                        new clientConnectionListener()

                ).start();
                firstRun=false;
                System.out.println("Starting transactionDispatcher");

                TransactionDispatcher transactionDispatcher =new TransactionDispatcher();
                Client.getInstance().setTransactionDispatcher(transactionDispatcher);
                new Thread(
                        transactionDispatcher
                ).start();

            }


        }


        try {
            in.close();
            out.close();
            echoSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void createConnection(HashMap listeclient){


        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            SuccursaleClient currentClient=(SuccursaleClient)pair.getValue();

            if(currentClient.getConnectionThread()==null)
            {


                    Succursale thisSuccrusale=Client.getInstance().getThisSuccrusale();
                ResponseClientThread newConnectionThread = new ResponseClientThread(currentClient.getSuccursaleIPAdresse(), thisSuccrusale.getId(),Integer.parseInt(currentClient.getPort()));

                currentClient.setConnectionThread(newConnectionThread);

                new Thread(
                        newConnectionThread
                ).start();


            }



        }



    }
}
