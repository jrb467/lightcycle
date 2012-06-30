package client;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import packets.Packet18ConfirmRequest;

public class RequestPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public ArrayList<Request> requests = new ArrayList<Request>();
	private Tron tron;
	
	public RequestPanel(Tron tron){
		this.tron = tron;
		setLayout(new BorderLayout());
		JLabel label = new JLabel("");
		ImageIcon icon = new ImageIcon("misc/tron.jpg", "Tron logo");
		label.setIcon(icon);
		add(label, BorderLayout.CENTER);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(requests.size() > 0){
					requests.get(0).mousePress(e.getX()-350, e.getY()-20);
				}
			}
		});
		Thread handleThread = new Thread(){
			public void run(){
				while(true){
					Point p = getMousePosition();
					if(p != null && p.x > 350 && p.x < 550 && p.y > 20 && p.y < 120){
						if(requests.size() > 0) requests.get(0).update(true);
					}else{
						if(requests.size() > 0) requests.get(0).update(false);
					}
					repaint();
					try{
						Thread.sleep(30);
					}catch (Exception e){}
				}
			}
		};
		handleThread.start();
	}
	
	public void accept(){
		tron.server.write(new Packet18ConfirmRequest(requests.get(0).getUser()));
		requests.remove(0);
	}
	
	public void decline(){
		requests.remove(0);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		if(requests.size() > 0){
			requests.get(0).paint(g.create(350, 20, 210, 110));
		}
	}

}
