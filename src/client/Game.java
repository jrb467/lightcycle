package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.network.*;
import util.timer.*;
import packets.*;

public class Game extends JPanel implements ActionHandler, Networked, Runnable{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage buffer;
	private Graphics2D graphics;
	private int fps = 30;
	public static final int DELAYS_PER_YIELD = 10;
	private Font font;
	private Font countDownFont;
	private BufferedImage image;
	
	private Thread runThread;
	
	private byte[][] spaces;
	private final byte BLOCK_SIZE = 5;
	
	private String p1Name;
	private String p2Name;
	
	private byte p1X = 90;
	private byte p1Y = 60;
	private byte p1Direction = 3;
	private byte p2X = 30;
	private byte p2Y = 60;
	private byte p2Direction = 1;
	private boolean isP1;
	
	private int p1Wins = 0;
	private int p2Wins = 0;
	
	private String winner = "";
	
	private Timer countdown;
	private String countDownTime = "";
	
	private TextLayout temp;
	
	private Tron tron;
	
	private boolean running = true;
	private boolean inGame = false;
	
	public Game(Tron tron, boolean isP1, String p1, String p2){
		this.tron = tron;
		this.isP1 = isP1;
		p1Name = p1;
		p2Name = p2;
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				keyPress(e.getKeyCode());
			}
		});
		tron.server.setNetworkHandler(this);
		spaces = new byte[120][120];
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("misc/sf.ttf"));
			font = font.deriveFont(Font.PLAIN, 18f);
			countDownFont = font.deriveFont(Font.PLAIN, 300);
		} catch (Exception e){
			System.err.println("Error finding font file");
			e.printStackTrace();
		}
		runThread = new Thread(this);
	}
	
	public void start(){
		if(runThread != null && isFocusable()){
			requestFocusInWindow();
			runThread.start();
		}
	}
	
	public void keyPress(int keycode){
		if(keycode == KeyEvent.VK_UP){
			if(isP1) {
				if(p1Direction != 2){
					p1Direction = 0;
					tron.server.write(p1Direction);
				}
			}else {
				if(p2Direction != 2){
					p2Direction = 0;
					tron.server.write(p2Direction);
				}
			}
		}else if(keycode == KeyEvent.VK_RIGHT){
			if(isP1) {
				if(p1Direction != 3){
					p1Direction = 1;
					tron.server.write(p1Direction);
				}
			}else {
				if(p2Direction != 3){
					p2Direction = 1;
					tron.server.write(p2Direction);
				}
			}
		}else if(keycode == KeyEvent.VK_DOWN){
			if(isP1) {
				if(p1Direction != 0){
					p1Direction = 2;
					tron.server.write(p1Direction);
				}
			}else {
				if(p2Direction != 0){
					p2Direction = 2;
					tron.server.write(p2Direction);
				}
			}
		}else if(keycode == KeyEvent.VK_LEFT){
			if(isP1) {
				if(p1Direction != 1){
					p1Direction = 3;
					tron.server.write(p1Direction);
				}
			}else {
				if(p2Direction != 1){
					p2Direction = 3;
					tron.server.write(p2Direction);
				}
			}
		}else if(keycode == KeyEvent.VK_R && inGame == false){
			tron.server.write(new Packet2Ready());
		}else if(keycode == KeyEvent.VK_ESCAPE){
			tron.server.write(new Packet8QuitGame());
			tron.server.setNetworkHandler(tron);
			tron.goToMain();
			inGame = false;
			running = false;
		}
	}

	public void connectionSevered(ConnectionData connection) {
		inGame = false;
		JOptionPane.showMessageDialog(this, "Disconnected from server", "Network Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	@Override
	public void handleInput(Object o, ConnectionData connection) {
		if(o instanceof Packet){
			switch(((Packet)o).getID()){
			case 1:
				p1Wins = ((Packet1Win)o).p1Wins;
				p2Wins = ((Packet1Win)o).p2Wins;
				winner = ((Packet1Win)o).winner;
				inGame = false;
				break;
			case 6:
				restart();
				break;
			case 7:
				JOptionPane.showMessageDialog(this, "Partner Disconnected", "Disconnect", JOptionPane.ERROR_MESSAGE);
				tron.server.setNetworkHandler(tron);
				tron.goToMain();
				running = false;
				inGame = false;
				break;
			}
		}else{
			p1X = (byte)(((Integer)o & -33554432) >>> 25);
			p1Y = (byte)(((Integer)o & 33292288) >>> 18);
			p1Direction = (byte)(((Integer)o & 196608) >>> 16);
			p2X = (byte)(((Integer)o & 65024) >>> 9);
			p2Y = (byte)(((Integer)o & 508) >>> 2);
			p2Direction = (byte)((Integer)o & 3);
			update();
		}
	}
	
	public void restart(){
		clearBoard();
		resetPlayers();
		resetDirections();
		update();
		countdown = new Timer(5);
		countdown.addAction(new Action(this, "3", 0));
		countdown.addAction(new Action(this, "2", .7f));
		countdown.addAction(new Action(this, "1", 1.4f));
		countdown.addAction(new Action(this, "start", 2.1f));
		countdown.start();
	}

	private void clearBoard(){
		for(int r = 0; r < spaces.length; r++){
			for(int c = 0; c < spaces[r].length; c++){
				spaces[r][c] = 0;
			}
		}
	}
	
	private void resetPlayers(){
		p1X = 90;
		p1Y = 60;
		p2X = 30;
		p2Y = 60;
	}
	
	private void resetDirections(){
		p1Direction = 3;
		p2Direction = 1;
	}
	
	private void update(){
		spaces[p1X][p1Y] = 1;
		spaces[p2X][p2Y] = 2;
	}
	
	private Color getColor(int x, int y){
		if(spaces[x][y] == 0){
			return Color.black;
		}else if(spaces[x][y] == 1){
			return Color.red;
		}else if(spaces[x][y] == 2){
			return Color.blue;
		}else if(spaces[x][y] == 3){
			return Color.green;
		}
		return Color.yellow;
	}
	
	private void draw(){
		if(image == null){
			image = (BufferedImage)createImage(600,600);
		}else{
			Graphics2D g = (Graphics2D)image.getGraphics();
			for(int x = 0; x < spaces.length; x++){
				for(int y = 0; y < spaces[x].length; y++){
					g.setColor(getColor(x,y));
					g.fillRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
			if(!inGame){
				g.setColor(Color.white);
				if(!countDownTime.equals("")){
					g.setFont(countDownFont);
					temp = new TextLayout(countDownTime, countDownFont, g.getFontRenderContext());
					g.drawString(countDownTime, (int)(600-temp.getBounds().getWidth())/2, (int)(600+temp.getBounds().getHeight())/2);
				}else{
					g.setFont(font);
					if(!winner.equals("")){
						temp = new TextLayout(winner+" Won", font, g.getFontRenderContext());
						g.drawString(winner+" Won", (int)(600-temp.getBounds().getWidth())/2, 100);
					}
					temp = new TextLayout("Hit 'r' to restart", font, g.getFontRenderContext());
					g.drawString("Hit 'r' to restart", (int)(600-temp.getBounds().getWidth())/2, 115);
					temp = new TextLayout("Hit escape to quit", font, g.getFontRenderContext());
					g.drawString("Hit escape to quit", (int)(600-temp.getBounds().getWidth())/2, 130);
				}
				g.setFont(font);
				g.setColor(Color.white);
				g.drawString(p1Name+" (Red): "+p1Wins, 10,30);
				g.drawString(p2Name+" (Blue): "+p2Wins, 10,45);
			}
		}
		if(buffer == null){
			buffer = (BufferedImage)createImage(tron.getWidth(), tron.getHeight());
		}else{
			graphics = (Graphics2D)buffer.getGraphics();
			graphics.setColor(Color.gray);
			graphics.fillRect(0, 0, tron.getWidth(), tron.getHeight());
			graphics.drawImage(image, 20, 9, null);
			graphics.setColor(Color.white);
			graphics.drawRect(19, 8, 601, 601);
		}
	}

	@Override
	public void handleEvent(Action a) {
		if(a.getEventInfo().equals("start")){
			inGame = true;
			countDownTime = "";
			countdown = null;
		}else{
			countDownTime = a.getEventInfo();
		}
	}
	
	private void paintGame(){
		//paints buffer to screen
		Graphics g;
		try{
			g = this.getGraphics();
			//if both can be painted
			if((g != null) && (buffer != null)){
				g.drawImage(buffer, 0, 0, null);
			}else{}
			if(g != null) g.dispose();
		}catch (Exception e){
			System.err.println("Graphics error: ");
			e.printStackTrace();
		}
	}

	public void run() {
		long before, after, timeDiff, sleepTime;  //used in maintaining desired fps/ups
		long overSleepTime = 0L; //amount of time machine overslept
		int delays = 0; //counts number of delays(when machine didn't sleep)
		sleepTime = (long)1000L/fps;
		int period = 1000000000/fps;
		
		while(running){
			before = System.nanoTime();
			draw();
			paintGame();
			after = System.nanoTime();
			timeDiff = after-before;
			sleepTime = (long)(period - timeDiff) - overSleepTime; //calculate desired time to sleep
			if(sleepTime>0){
				try{
					Thread.sleep(sleepTime/1000000L);  //sleep
				}catch (InterruptedException e){}
				overSleepTime = (long)(System.nanoTime()-after)-sleepTime; //calculate how long the machine overslept
			}else{
				overSleepTime = 0L;
				//allow other essential threads to execute
				if(++delays >= DELAYS_PER_YIELD){
					Thread.yield();
					delays = 0;
				}
			}
		}
		tron.server.setNetworkHandler(tron);
	}

}
