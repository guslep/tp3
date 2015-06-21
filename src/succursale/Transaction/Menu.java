package succursale.Transaction;

import succursale.ActiveSuccursale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gus on 6/17/15.
 */
public class Menu implements Runnable{

/*
Intercept les inputs du client
 */
    public Menu() {

    }

    @Override
    public void run() {
        System.out.println("System started type help so see the command list");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Boolean exit=false;
        while (!exit){
            try {
                String command=stdIn.readLine();
                if(command.equals("help")){
                    executeHelp();
                }else{
                    String[] action=command.split(" ");

                   if (action[0].equals("list")) {
                        listSuccursale();
                    } else if (action[0].equals("amount")) {
                        executeAmount();
                    } else if (action[0].equals("transfer")) {
                        executeTransfer(command);
                    } else {
                        System.out.println("Error unknown command");
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    /**
     * effectue un transfert manuel
     * @param commande commande entré manuelle ment pasr l'utulisateur
     */
    private void executeTransfer(String commande){
        String leftTrimM[]=commande.split("--m ");
        String amount[]=leftTrimM[1].split(" ");
        String montantChoisie=amount[0];
        int montantTransfer=Integer.parseInt(montantChoisie);

        String leftTrimS[]=commande.split("--s ");
        String id[]=leftTrimS[1].split(" ");
        int idSuccursale=Integer.parseInt(id[0]);
        if(montantTransfer>0&&idSuccursale>=0){
//            TODO ajouter dans client une méthode pour creer une transaction manuelement
//            TODO ajouter la creation du thread avant le while degeu

            ActiveSuccursale.getInstance().getTransactionDispatcher().createManualTransaction(ActiveSuccursale.getInstance().getThisSuccrusale().getId(),montantTransfer,idSuccursale);
        }



    }

    /**
     * Commande amount
     */
    private  void executeAmount(){
        System.out.println("Montant disponible pour envoyer");
        System.out.println(ActiveSuccursale.getInstance().getThisSuccrusale().getMontant());

    }

    /**
     * Commande list
     *
     */
    private void listSuccursale(){
        ActiveSuccursale.getInstance().printSuccursale();
    }
    /**
    Commande help
     */
    private void executeHelp(){
        System.out.println("list");
        System.out.println("Affiche la liste des succursales");
        System.out.println("amount");
        System.out.println("Affiche le montant disponible de la succursale");
        System.out.println("transfer");
        System.out.println("Envoie le montant specifié vers la succursale spécifié");
        System.out.println("Paramètre");
        System.out.println("--m montant ");
        System.out.println("--s id de la succursale");






    }
}
