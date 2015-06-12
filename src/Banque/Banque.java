package Banque;

import sun.misc.Lock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Gus on 6/4/2015.
 */
public class Banque {
     private  int totalMoneyInThaBank=0;
    Lock montantLock=new Lock() {
    };

    private ArrayList<Succursale> listSuccursale= new ArrayList<Succursale>();
    private ArrayList<ResponseServerThread>listConnection = new ArrayList<ResponseServerThread>();


     public synchronized Integer addSucursale(Succursale succursale){
            succursale.setId(listSuccursale.size()+1);
         listSuccursale.add(succursale);

         try {
             montantLock.lock();
             try{
                 totalMoneyInThaBank+=succursale.getMontant();

             }finally {

                 System.out.println(succursale.getNom()+" "+" a ajouté "+succursale.getMontant());
                 System.out.println("Montant total de la banque est de "+ totalMoneyInThaBank);
                 montantLock.unlock();
                 pushToClient(printSucursale());

             }
         } catch (InterruptedException e) {
             e.printStackTrace();
         }



         return succursale.getId();
    }



    public synchronized void addConnection( ResponseServerThread response){
        listConnection.add(response);


    }

    private void pushToClient(String message){
        Iterator itr = listConnection.iterator();
        while (itr.hasNext()){
            ResponseServerThread current=(ResponseServerThread)itr.next();
        current.sendMessage(message);
        }
         
    }

    private  String printSucursale( ){
        String listSucursaleSTR="";
        Iterator itr=listSuccursale.iterator();
        while (itr.hasNext()){
            Succursale currentSuccursale=(Succursale)itr.next();
            listSucursaleSTR+=currentSuccursale.toString();
            if(itr.hasNext()){
                listSucursaleSTR+=";";
            }
        }
        
        return  listSucursaleSTR;
    }




}
