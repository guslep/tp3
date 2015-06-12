package Banque;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;

/**
 * Created by Gus on 6/4/2015.
 */
public class Succursale {

    private int id;
    private InetAddress succursaleIPAdresse;
    private int montant;
    private String nom;
    private String port;

    public Succursale(InetAddress succursaleIPAdresse, int montant, String nom, String portNumber) {
        this.succursaleIPAdresse = succursaleIPAdresse;
        this.montant = montant;
        this.nom = nom;
        this.port=portNumber;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public InetAddress getSuccursaleIPAdresse() {
        return succursaleIPAdresse;
    }

    public int getMontant() {
        return montant;
    }

    public String getNom() {
        return nom;
    }
    public String toString(){

        return id+","+nom+","+montant+","+succursaleIPAdresse.getHostAddress()+","+port;

    }
}
