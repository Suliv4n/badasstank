package fr.sulivan.badasstank.util;

public class Timer {
	private Runnable onFinish;
	private boolean start = false;
	private int time = 0; //ms
	private int current = time;
	
	public Timer(int time){
		this.time = time;
		this.current = time;
	}
	
	public void update(int delta){
		if(start){
			current = Math.max(current - delta, 0);
			start = false;
			
			if(current == 0){
				if(onFinish != null){
					onFinish.run();
				}
				start = false;
			}
		}
	}
	
	public void setOnFinish(Runnable action){
		this.onFinish = action;
	}
	
	public void start(){
		start = true;
	}
	
	public void pause(){
		start = false;
	}
	
	public void stop(){
		current = time;
		start = false;
	}
}
