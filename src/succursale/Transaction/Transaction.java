package succursale.Transaction;

import java.io.Serializable;

/**
 * Created by Gus on 6/11/2015.
 */
public class Transaction extends Message implements Serializable  {

    private int idFrom;
    private int idTo;
    private int montant;

    public Transaction(int idFrom, int idTo, int montant) {
       super("Transaction");
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.montant = montant;
    }

}
