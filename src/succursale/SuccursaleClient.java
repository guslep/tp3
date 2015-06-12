package succursale;

import Banque.Succursale;

import java.net.InetAddress;

/**
 * Created by Gus on 6/11/2015.
 */
public class SuccursaleClient extends Succursale {
  private ResponseClientThread connectionThread;

    public SuccursaleClient(InetAddress succursaleIPAdresse, int montant, String nom,String port) {
        super(succursaleIPAdresse, montant, nom, port);
    }

    public ResponseClientThread getConnectionThread() {
        return connectionThread;
    }

    public void setConnectionThread(ResponseClientThread connectionThread) {
        this.connectionThread = connectionThread;
    }
}
