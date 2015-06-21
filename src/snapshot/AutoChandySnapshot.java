package snapshot;

import java.util.Random;

public class AutoChandySnapshot implements Runnable{

	private static final int MIN_SLEEP_TIMER = 15;
	private static final int MAX_SLEEP_TIMER = 45;
	private static final int SECOND_TO_MILLISECOND = 1000;
	private int sleepTimer;
	private int succursaleSnapShot;
	private Random randomNumber;
	private ChandySnapshot chandySnapshot;
	
	public AutoChandySnapshot(int nbSuccursale) {
		this.randomNumber = new Random();
		this.succursaleSnapShot = randomNumber.nextInt(nbSuccursale) + 1;
	}
	
	@Override
	public void run() {
		while (true){
			this.sleepTimer = this.randomNumber.nextInt
					(MAX_SLEEP_TIMER - MIN_SLEEP_TIMER) + MIN_SLEEP_TIMER;
			try {
				Thread.sleep(sleepTimer * SECOND_TO_MILLISECOND);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			chandySnapshot = new ChandySnapshot();
			
		}
	}
}
