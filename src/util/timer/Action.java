package util.timer;



public class Action {
	private ActionHandler handler;
	private String event;
	private float time;
	
	public Action(ActionHandler h, String info, float time){
		handler = h;
		event = info;
		this.time = time;
	}
	
	public void act(){
		handler.handleEvent(this);
	}
	
	public String getEventInfo(){
		return event;
	}
	
	public float getActionTime(){
		return time;
	}
}
