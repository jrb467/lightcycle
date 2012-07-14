package client;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import packets.Packet18ConfirmRequest;

public class RequestHandler {
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static TronFrame tron;
	
	public static void setFrame(TronFrame f){
		tron = f;
	}
	
	public static void start(){
		Thread handleThread = new Thread(){
			public void run(){
				while(true){
					Point p = tron.getMousePosition();
					if(p != null && p.x > 350 && p.x < 550 && p.y > 20 && p.y < 120){
						if(requests.size() > 0) requests.get(0).update(true);
					}else{
						if(requests.size() > 0) requests.get(0).update(false);
					}
					tron.repaint();
					try{
						Thread.sleep(30);
					}catch (Exception e){}
				}
			}
		};
		handleThread.start();
	}
	
	public static void paint(Graphics g){
		if(requests.size() > 0){
			requests.get(0).paint(g.create(350, 20, 210, 110));
		}
	}
	
	public static void accept(){
		if(requests.size() > 0){
			if(tron != null){
				tron.server.write(new Packet18ConfirmRequest(requests.get(0).getUser()));
			}
			requests.remove(0);
		}
	}
	
	public static void decline(){
		requests.remove(0);
	}

}
