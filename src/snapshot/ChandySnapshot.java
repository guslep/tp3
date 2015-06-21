package snapshot;

import java.awt.List;
import Banque.Succursale;

public class ChandySnapshot {

	private int montantBanque;
	private int nombreSuccursaleAttendu;
	private List listeCanal;
	private boolean snapshotCompleter;
	private Succursale [] tableauSuccursale;
	
	public ChandySnapshot(){
		
	}
	
	public int getMontantBanque() {
		return montantBanque;
	}

	public void setMontantBanque(int montantBanque) {
		this.montantBanque = montantBanque;
	}

	public int getNombreSuccursaleAttendu() {
		return nombreSuccursaleAttendu;
	}

	public void setNombreSuccursaleAttendu(int nombreSuccursaleAttendu) {
		this.nombreSuccursaleAttendu = nombreSuccursaleAttendu;
	}

	public List getListeCanal() {
		return listeCanal;
	}

	public void setListeCanal(List listeCanal) {
		this.listeCanal = listeCanal;
	}

	public boolean isSnapshotCompleter() {
		return snapshotCompleter;
	}

	public void setSnapshotCompleter(boolean snapshotCompleter) {
		this.snapshotCompleter = snapshotCompleter;
	}

	public Succursale[] getTableauSuccursale() {
		return tableauSuccursale;
	}

	public void setTableauSuccursale(Succursale[] tableauSuccursale) {
		this.tableauSuccursale = tableauSuccursale;
	}

	private void addMessage(){
		
	}
}
