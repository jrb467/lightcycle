package util.timer;

import java.util.ArrayList;

public class Timer extends Thread{
	private long startTime;
	private float timeElapsed;
	private boolean alive;
	private ArrayList<Action> actions;
	private float maxDuration;
	
	public Timer(float max){
		alive = true;
		maxDuration = max;
		actions = new ArrayList<Action>();
	}
	
	public void run(){
		startTime = System.nanoTime();
		while(alive){
			timeElapsed = (float)(System.nanoTime()-startTime)/1000000000;
			for(int i = 0; i < actions.size(); i++){
				if(getAction(i).getActionTime() <= timeElapsed){
					getAction(i).act();
					removeAction(i);
				}
			}
			if(maxDuration < timeElapsed){
				alive = false;
			}
			try{
				Thread.sleep(100);
			}catch (Exception e){}
		}
	}
	
	public synchronized Action getAction(int index){
		return actions.get(index);
	}
	
	public void finish(){
		alive = false;
	}

	public synchronized void addAction(Action a){
		actions.add(a);
	}
	
	private synchronized void removeAction(int i){
		actions.remove(i);
	}
}
