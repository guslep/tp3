package succursale.Transaction;

import java.io.Serializable;

/**
 * Created by Guillaume on 2015-06-12.
 */
public class SynchMessage extends Message implements Serializable{
    int idSuccursale;

    public SynchMessage(int idSuccursale) {
        super();
        this.idSuccursale = idSuccursale;
    }

    public int getIdSuccursale() {
        return idSuccursale;
    }
}
