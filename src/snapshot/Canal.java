package snapshot;

public class Canal {
	
	private String emetteur;
	private String recepteur;
	private int montant;
	
	public Canal (String e, String r, int m) {
		this.emetteur = e;
		this.recepteur = r;
		this.montant = m;
	}
	
	public String getEmetteur(){
		return this.emetteur;
	}
	
	public String getRecepteur(){
		return this.recepteur;
	}
	
	public int getMontant(){
		return this.montant;
	}

	public void setEmetteur(String e){
		this.emetteur = e;
	}
	
	public void setRecepteur(String r){
		this.recepteur = r;
	}
	
	public void setMontant(int m){
		this.montant = m;
	}
	
	public void addMontant(int m){
		this.montant += m;
	}
	
	public String toString(){
		String messageCanal = "Le canal entre l'émetteur " +
		this.emetteur + " et le récepteur " + this.recepteur +
		" contient " + this.montant + "$";
		return messageCanal;
	}
}
