package client;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import packets.Packet18ConfirmRequest;

public class RequestHandler {
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static TronFrame tron;
	private static BufferedImage buffer;
	private static Graphics bufGraphics;
	
	public static void setFrame(TronFrame f){
		tron = f;
		buffer = new BufferedImage(210, 110, BufferedImage.TYPE_INT_ARGB);
		bufGraphics = buffer.getGraphics();
	}
	
	public static void start(){
		Thread handleThread = new Thread(){
			public void run(){
				while(true){
					if(tron != null){
						Point p = tron.getMousePosition();
						if(p != null && p.x > 350 && p.x < 550 && p.y > 47 && p.y < 147){
							if(requests.size() > 0) requests.get(0).update(true);
						}else{
							if(requests.size() > 0) requests.get(0).update(false);
						}
						paint(tron.getGraphics());
					}
					try{
						Thread.sleep(30);
					}catch (Exception e){}
				}
			}
		};
		handleThread.start();
	}
	
	public static void mousePress(int x, int y){
		if(requests.size() > 0){
			requests.get(0).mousePress(x-350, y-47);
		}
	}
	
	public static void paint(Graphics g){
		if(requests.size() > 0 && g != null){
			requests.get(0).paint(bufGraphics);
			g.drawImage(buffer, 350, 47, 210, 110, null);
		}
	}
	
	public static void add(Request q){
		requests.add(q);
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
