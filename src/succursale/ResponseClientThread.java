package succursale;

import snapshot.messageRequestChandy;
import snapshot.messageResponseChandy;
import succursale.Transaction.Message;
import succursale.Transaction.SynchMessage;
import succursale.Transaction.Transaction;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gus on 6/11/2015.
 */
public class ResponseClientThread implements Runnable{




    ObjectOutputStream messageSender;
    Socket echoSocket = null;

    ObjectInputStream messageReader ;
    public ResponseClientThread(Socket sucursaleSocket) {

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


             ActiveSuccursale.getInstance().getListeSuccursale().get(message.getIdSuccursale()).setConnectionThread(this);
              ActiveSuccursale.getInstance().printSuccursale();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param succursaleIPAdresse adresse ip ;a qui on ce connecte
     * @param idSuccursale id de la succursale qu'on contacte
     * @param portNumber port sur lequel on contacte les succursales
     */
    public ResponseClientThread(InetAddress succursaleIPAdresse,Integer idSuccursale,Integer portNumber ) {



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


                   ActiveSuccursale.getInstance().getThisSuccrusale().receiveDeposit(((Transaction) messageReceived).getMontant());


               }else if(messageRequestChandy.class.isInstance(messageReceived)){//ici on varépondre à un message de chandi lamport
                   //clientSuccursale.getTransactionDispatcher().notify();


                   messageRequestChandy received=(messageRequestChandy) messageReceived;
                   messageResponseChandy response=new messageResponseChandy(received.getIdSnapShot());
                   System.out.println(response.getTransactionEnAttente().size() +" transaction avant 20 secondes de delai "+response.getSuccrusale().getMontant());
                   Iterator mapPIterator =response.getTransactionEnAttente().entrySet().iterator();



                   while (mapPIterator.hasNext()) {

                       Map.Entry pair = (Map.Entry) mapPIterator.next();
                       Transaction currentTransaction = (Transaction) pair.getValue();

                       System.out.println("Transaction in  canal de  " +currentTransaction.getMontant());




                   }


                   TimerTaskSend timerTask=new TimerTaskSend(response);
                   Timer timer=new Timer();
                  timer.schedule(timerTask,20*1000);
                  // sendMessage(response);




               }else if(messageResponseChandy.class.isInstance(messageReceived)){
                   messageResponseChandy response=(messageResponseChandy)messageReceived;
                   ActiveSuccursale.getInstance().getChandyManager().dispatchChandyResponse(response);


               }else{

               }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * envoie une message à une succursale
     * @param messageTosSend
     */
    public void sendMessage(Message messageTosSend){

        try {

            messageSender.writeObject(messageTosSend);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private class TimerTaskSend extends TimerTask {

        Message chandyMessage;


        private TimerTaskSend( Message chandyMessage) {

            this.chandyMessage = chandyMessage;

        }

        /**
         * Lorsuqe le delai est passé
         */
        @Override
        public void run() {
            messageResponseChandy test=(messageResponseChandy )chandyMessage;

            Iterator mapPIterator =test.getTransactionEnAttente().entrySet().iterator();



            while (mapPIterator.hasNext()) {

                Map.Entry pair = (Map.Entry) mapPIterator.next();
                Transaction currentTransaction = (Transaction) pair.getValue();

                System.out.println("Transaction in  canal de  " +currentTransaction.getMontant());




            }

            System.out.println("Sending response "+(test.getSuccrusale().getMontant()+" size "+test.getTransactionEnAttente().size()));


            sendMessage(chandyMessage);

        }

    }
}
