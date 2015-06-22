package snapshot;

import succursale.ActiveSuccursale;

import java.util.Random;

public class AutoChandySnapshot implements Runnable{

	private static final int MIN_SLEEP_TIMER = 30;
	private static final int MAX_SLEEP_TIMER = 45;
	private static final int SECOND_TO_MILLISECOND = 1000;
	private int sleepTimer=0;
    private ChandyManager manager=null;
	//private int succursaleSnapShot;
	private Random randomNumber;
	private ChandySnapshot chandySnapshot;
	
	public AutoChandySnapshot(int nbSuccursale) {
		this.randomNumber = new Random();
	//	this.succursaleSnapShot = randomNumber.nextInt(nbSuccursale) + 1;
	}


    public AutoChandySnapshot(ChandyManager chandyManager) {
        manager=chandyManager;
        this.randomNumber = new Random();

    }

    @Override
	public void run() {


		while (true){
			this.sleepTimer = this.randomNumber.nextInt
					(MAX_SLEEP_TIMER - MIN_SLEEP_TIMER) + MIN_SLEEP_TIMER;
			try {
				Thread.sleep(sleepTimer * SECOND_TO_MILLISECOND);

                if(ActiveSuccursale.getInstance().getListeSuccursale().size()!=0){
                    System.out.println("Nouveau chandy !!!!!!!!!!!!!");
                    manager.creerSnapShot();
                }

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

			
		}
	}
}
