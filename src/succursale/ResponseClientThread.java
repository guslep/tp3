package succursale;

import succursale.Transaction.Message;
import succursale.Transaction.SynchMessage;
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
    ObjectOutputStream messageSender;
    Socket echoSocket = null;

    ObjectInputStream messageReader ;
    public ResponseClientThread(Socket sucursaleSocket,Client clientSuccursale) {
        this.clientSuccursale=clientSuccursale;
        echoSocket = sucursaleSocket;
        try {
            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
             messageReader = new ObjectInputStream((echoSocket.getInputStream()));

            SynchMessage message=null;

            try {
                message = (SynchMessage) messageReader.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("id recu: " + message.getIdSuccursale());


              this.clientSuccursale.getListeSuccursale().get(message.getIdSuccursale()).setConnectionThread(this);
              this.clientSuccursale.printSuccursale();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ResponseClientThread(InetAddress succursaleIPAdresse,Integer idSuccursale,Integer portNumber,Client clientSuccursale ) {
        this.clientSuccursale=clientSuccursale;


        try {
            echoSocket = new Socket(succursaleIPAdresse,portNumber);

            messageSender = new ObjectOutputStream(echoSocket.getOutputStream());
            messageReader = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Hote inconnu: " + succursaleIPAdresse);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ne pas se connecter au serveur: " + succursaleIPAdresse);
            System.exit(1);
        }
        try {
            messageSender.writeObject(new SynchMessage(idSuccursale));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("La succursale vient d'initier une connexion.....");
        //  BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.messageReader));


    }

    @Override
    public void run() {

        System.out.println ("connexion reussie");
        System.out.println ("Attente de l'entree.....");



        Message messageReceived;
        try {
            while ((messageReceived =(Message)messageReader.readObject() ) != null)
            {
               if(Transaction.class.isInstance(messageReceived)){
                   Transaction transaction=(Transaction) messageReceived;
                   System.out.println("New transaction received from "+ transaction.getIdFrom()+"To: "+transaction.getIdTo()+"Montant: "+transaction.getMontant() );


                   clientSuccursale.getThisSuccrusale().receiveDeposit(((Transaction) messageReceived).getMontant());


               }else{//ici on varépondre à un message de chandi lamport
                   //clientSuccursale.getTransactionDispatcher().notify();

               }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void sendMessage(Message messageTosSend){

        try {

            messageSender.writeObject(messageTosSend);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
