package succursale.Transaction;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Gus on 6/11/2015.
 */
public class Transaction extends Message implements Serializable  {
    private java.util.UUID UUID;
    private int idFrom;
    private int idTo;
    private int montant;

    /**
     *
     * @param idFrom Id de la succursale qui envoie la transaction
     * @param idTo Id à qui on envoie le message
     * @param montant montant de la trnasaction
     */
    public Transaction(int idFrom, int idTo, int montant) {
       super("Transaction");
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.montant = montant;
        UUID=java.util.UUID.randomUUID();
    }

    public int getIdFrom() {
        return idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public int getMontant() {
        return montant;
    }

    public UUID getUUID() {
        return UUID;
    }
}
