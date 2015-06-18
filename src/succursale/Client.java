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
    String portNumber;

    public HashMap<Integer, SuccursaleClient> getListeSuccursale() {
        return listeSuccursale;
    }

    public Client()throws IOException  {

		String serverHostname;
//        TODO une fois que ca marche mettre client en singleton et sortir le while immence et le mettre dans un autre thread
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


        String succursaleName;

        Integer montant;

        System.out.print ("Entree l'adresse de la banque: ");
        serverHostname=stdIn.readLine();
        if(serverHostname==null||serverHostname.equals("")){
            serverHostname="127.0.0.1";
        }
        System.out.print ("Entree e port de la succursale: ");

        portNumber=stdIn.readLine();
        if(portNumber==null||portNumber.equals("")){
            portNumber="10119";
        }
        System.out.print ("Entree le nom de la succursale: ");
        succursaleName=stdIn.readLine();
        if(succursaleName==null||succursaleName.equals("")){
            Random rand=new Random();

            succursaleName="Test "+(rand.nextInt(9)+1);
        }
        System.out.print ("Entree le montant de la succursale: ");
       String strMontant=stdIn.readLine();


        if(strMontant==null||strMontant.equals("")){

            Random rand=new Random();
            montant=rand.nextInt(1000000)+1000;

        }else{
            montant=Integer.parseInt(strMontant);
        }






            //TODO Deplacer ce code la dans le BanqueCOnnector

//TODO ajouter le menu ici et l'interaction avec l'usager, idealement creer une classe affichage ce serait pas mal plus clean
        stdIn.close();

        new Thread(
                new Menu(this)
        ).start();


        System.out.println ("Essai de se connecter a l'hote " +
		serverHostname + " au port 10118.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

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



        out.println(montant.toString()+","+succursaleName+","+portNumber);


        //out.println(montant.toString()+","+succursaleName);

        System.out.println("Liste des succursales");
        boolean firstRun=true;
        while(serverHostname!=null)
        {

            String recuTest=in.readLine();
            System.out.println(recuTest);
            String recu[]=recuTest.split(";");
            for(int index=0;index<recu.length;index++){
                String[] splitSuccursale=recu[index].split(",");

                if(!listeSuccursale.containsKey(Integer.parseInt(splitSuccursale[0]))){
                    if(splitSuccursale[1].equals(succursaleName)){

                        thisSuccrusale=new Succursale(InetAddress.getByName(splitSuccursale[3]),
                                Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        thisSuccrusale.setId(Integer.parseInt(splitSuccursale[0]));

                    }
                    else{
                  SuccursaleClient newSuccursale=new SuccursaleClient(InetAddress.getByName(splitSuccursale[3]),
                          Integer.parseInt(splitSuccursale[2]),splitSuccursale[1],splitSuccursale[4]);
                        newSuccursale.setId(Integer.parseInt(splitSuccursale[0]));

                    listeSuccursale.put(Integer.parseInt(splitSuccursale[0]),newSuccursale);


                }}



            }

            printHashMap(listeSuccursale);
            if(firstRun){
                System.out.println("Creating Connection");

                createConnection(listeSuccursale);
                new Thread(
                        new clientConnectionListener(this)

                ).start();
                firstRun=false;
                System.out.println("Starting transactionDispatcher");
                transactionDispatcher =new TransactionDispatcher(this);
                new Thread(
                        transactionDispatcher
                ).start();

            }


        }






        out.close();
        in.close();

        echoSocket.close();
    }

    public String getPortNumber() {
        return portNumber;
    }


    public void printSuccursale(){
        printHashMap(listeSuccursale);
    }

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

    private void createConnection(HashMap listeclient){


        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            SuccursaleClient currentClient=(SuccursaleClient)pair.getValue();

            if(currentClient.getConnectionThread()==null)
            {



                ResponseClientThread newConnectionThread = new ResponseClientThread(currentClient.getSuccursaleIPAdresse(), thisSuccrusale.getId(),Integer.parseInt(currentClient.getPort()),this);

                currentClient.setConnectionThread(newConnectionThread);

                new Thread(
                        newConnectionThread
                ).start();


            }



        }



        }


    public Succursale getThisSuccrusale() {
        return thisSuccrusale;
    }

    public TransactionDispatcher getTransactionDispatcher() {
        return transactionDispatcher;
    }
}

