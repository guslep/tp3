package succursale;

import Banque.Succursale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Client {
    static Integer succursaleId;
    static HashMap<Integer,SuccursaleClient> listeSuccursale=new HashMap<Integer, SuccursaleClient>();
    String portNumber;

    public HashMap<Integer, SuccursaleClient> getListeSuccursale() {
        return listeSuccursale;
    }

    public Client()throws IOException  {

		String serverHostname;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


        String succursaleName;

        Integer montant;

        System.out.print ("Entree l'adresse de la banque: ");
        serverHostname=stdIn.readLine();
        System.out.print ("Entree e port de la succursale: ");
        portNumber=stdIn.readLine();
        System.out.print ("Entree le nom de la succursale: ");
        succursaleName=stdIn.readLine();
        System.out.print ("Entree le montant de la succursale: ");
        montant=Integer.parseInt(stdIn.readLine());









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

      //  BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        out.println(montant.toString()+","+succursaleName+","+portNumber);


        //out.println(montant.toString()+","+succursaleName);

        System.out.println("Liste des succursales");
        boolean firstRun=true;
        while(serverHostname!=null){

            String recuTest=in.readLine();
            System.out.println(recuTest);
            String recu[]=recuTest.split(";");
            for(int index=0;index<recu.length;index++){
                String[] splitSuccursale=recu[index].split(",");

                if(!listeSuccursale.containsKey(Integer.parseInt(splitSuccursale[0]))){
                    if(splitSuccursale[1].equals(succursaleName)){
                        succursaleId=Integer.parseInt(splitSuccursale[0]);

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
                createConnection(listeSuccursale);
                new Thread(
                        new clientConnectionListener(this)

                ).start();
                firstRun=false;

            }


        }






        out.close();
        in.close();
        stdIn.close();
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

    private static void createConnection(HashMap listeclient){


        Iterator it = listeclient.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();
            SuccursaleClient currentClient=(SuccursaleClient)pair.getValue();

            if(currentClient.getConnectionThread()==null)
            {



                ResponseClientThread newConnectionThread = new ResponseClientThread(currentClient.getSuccursaleIPAdresse(), succursaleId,Integer.parseInt(currentClient.getPort()),this);

                currentClient.setConnectionThread(newConnectionThread);

                new Thread(
                        newConnectionThread
                ).start();


            }



        }



        }


}

