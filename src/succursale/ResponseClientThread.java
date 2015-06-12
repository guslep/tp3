package succursale;

import Banque.Succursale;
import succursale.Transaction.Message;
import succursale.Transaction.Transaction;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Gus on 6/11/2015.
 */
public class ResponseClientThread implements Runnable{



    Client clientSuccursale;

    Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    public ResponseClientThread(Socket sucursaleSocket,Client clientSuccursale) {
        this.clientSuccursale=clientSuccursale;
        echoSocket = sucursaleSocket;
        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            String inputLine;

            inputLine = in.readLine();
                System.out.println("id recu: " + inputLine);


              this.clientSuccursale.getListeSuccursale().get(Integer.parseInt(inputLine)).setConnectionThread( this);
              this.clientSuccursale.printSuccursale();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ResponseClientThread(InetAddress succursaleIPAdresse,Integer idSuccursale,Integer portNumber,Client clientSuccursale ) {
        this.clientSuccursale=clientSuccursale;


        try {
            echoSocket = new Socket(succursaleIPAdresse,portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + succursaleIPAdresse);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: " + succursaleIPAdresse);
            System.exit(1);
        }
        out.println(idSuccursale);
        System.out.println("La succursale vient d'initier une connexion.....");
        //  BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


    }

    @Override
    public void run() {

        System.out.println ("connexion reussie");
        System.out.println ("Attente de l'entree.....");
        ObjectInputStream messagereader=null;

        try {
            messagereader = new ObjectInputStream(echoSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message messageReceived;
        try {
            while ((messageReceived =(Message)messagereader.readObject() ) != null)
            {
               if(Transaction.class.isInstance(messageReceived)){
                   //on devrait updater le client avec le nouveau montant recu


               }else{

               }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
