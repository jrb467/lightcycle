package server;

import packets.*;
import util.network.*;
import util.timer.*;

public class Game extends Thread implements ActionHandler, Networked{
	public ConnectionData p1;
	public ConnectionData p2;
	private int playerInfo;
	private int p1Wins;
	private int p2Wins;
	public boolean p1Ready;
	public boolean p2Ready;
	private Timer delay;
	
	private ServerGUI gui;
	
	private byte[][] spaces;
	private boolean running;
	private boolean inGame;
	
	private static final int DELAYS_PER_YIELD = 10;
	
	private final short ups = 30;

	public Game(ServerGUI gui, ConnectionData p1, ConnectionData p2){
		this.p1 = p1;
		this.p2 = p2;
		p1Wins = 0;
		p2Wins = 0;
		p1Ready = false;
		p2Ready = false;
		this.gui = gui;
		setP1Dir(3);
		setP2Dir(1);
		setP1X(90);
		setP1Y(60);
		setP2X(30);
		setP2Y(60);
		spaces = new byte[120][120];
		running = true;
		inGame = false;
		start();
	}
	
	public void run(){
		long before, after, timeDiff, sleepTime;  //used in maintaining desired fps/ups
		long overSleepTime = 0L; //amount of time machine overslept
		int delays = 0; //counts number of delays(when machine didn't sleep)
		sleepTime = (long)1000L/ups;
		int period = 1000000000/ups;
		
		while(running){
			//update/render/paint the game
			before = System.nanoTime();
			if(inGame){
				update();
			}else{
				testReadiness();
			}
			after = System.nanoTime();
			timeDiff = after-before;
			sleepTime = (long)(period - timeDiff) - overSleepTime; //calculate desired time to sleep
			if(sleepTime>0){
				try{
					Thread.sleep(sleepTime/1000000L);  //sleep
				}catch (InterruptedException e){
					e.printStackTrace();
				}
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
		p1.setNetworkHandler(gui);
		p2.setNetworkHandler(gui);
		gui.removeGame(this);
	}
	
	private void testReadiness(){
		if(delay == null){
			if(p1Ready && p2Ready){
				p1.write(new Packet6GameStart());
				p2.write(new Packet6GameStart());
				delay = new Timer(5);
				delay.addAction(new Action(this, "start", 2.1f));
				delay.start();
				p1Ready = false;
				p2Ready = false;
				restart();
			}
		}
		
	}
	
	private void update(){
		switch(getP1Dir()){
		case 0:
			setP1X(getP1X());
			setP1Y(getP1Y()-1);
			break;
		case 1:
			setP1X(getP1X()+1);
			setP1Y(getP1Y());
			break;
		case 2:
			setP1X(getP1X());
			setP1Y(getP1Y()+1);
			break;
		case 3: 
			setP1X(getP1X()-1);
			setP1Y(getP1Y());
			break;
		}

		switch(getP2Dir()){
		case 0:
			setP2X(getP2X());
			setP2Y(getP2Y()-1);
			break;
		case 1:
			setP2X(getP2X()+1);
			setP2Y(getP2Y());
			break;
		case 2:
			setP2X(getP2X());
			setP2Y(getP2Y()+1);
			break;
		case 3: 
			setP2X(getP2X()-1);
			setP2Y(getP2Y());
			break;
		}
		if(getP1X() == getP2X() && getP1Y() == getP2Y()){
			inGame = false;
			p1.write(new Packet1Win("No one", p1Wins, p2Wins));
			p2.write(new Packet1Win("No one", p1Wins, p2Wins));
			return;
		}else if(getP1X() > 119 || getP1X() < 0 || getP1Y() > 119 || getP1Y() < 0 || getFilled(getP1X(), getP1Y())){
			if(!(getP2X() > 119 || getP2X() < 0 || getP2Y() > 119 || getP2Y() < 0 || getFilled(getP2X(), getP2Y()))){
				p2Wins++;
				p1.write(new Packet1Win(p2.name+" (Blue)", p1Wins, p2Wins));
				p2.write(new Packet1Win(p2.name+" (Blue)", p1Wins, p2Wins));
				UserManager.getUser(p1.name).gamePlayed();
				UserManager.getUser(p2.name).gamePlayed();
				UserManager.getUser(p2.name).gameWon();
			}else{
				p1.write(new Packet1Win("No one", p1Wins, p2Wins));
				p2.write(new Packet1Win("No one", p1Wins, p2Wins));
			}
			inGame = false;
			return;
		}else if(getP2X() > 119 || getP2X() < 0 || getP2Y() > 119 || getP2Y() < 0 || getFilled(getP2X(), getP2Y())){
			if(!(getP1X() > 119 || getP1X() < 0 || getP1Y() > 119 || getP1Y() < 0 || getFilled(getP1X(), getP1Y()))){
				p1Wins++;
				p1.write(new Packet1Win(p1.name+" (Red)", p1Wins, p2Wins));
				p2.write(new Packet1Win(p1.name+" (Red)", p1Wins, p2Wins));
				UserManager.getUser(p1.name).gamePlayed();
				UserManager.getUser(p1.name).gameWon();
				UserManager.getUser(p2.name).gamePlayed();
			}else{
				p1.write(new Packet1Win("No one", p1Wins, p2Wins));
				p2.write(new Packet1Win("No one", p1Wins, p2Wins));
			}
			inGame = false;
			return;
		}
		setBit(getP1X(), getP1Y(), (byte)1);
		setBit(getP2X(), getP2Y(), (byte)1);
		
		p1.write(playerInfo);
		p2.write(playerInfo);
	}
	
	private void setP1X(int x){
		playerInfo = playerInfo & 33554431;
		playerInfo = playerInfo | (x << 25);
	}
	
	private  void setP1Y(int y){
		playerInfo = playerInfo & -33292289;
		playerInfo = playerInfo | (y << 18);
	}
	
	private  void setP1Dir(int dir){
		playerInfo = playerInfo & -196609;
		playerInfo = playerInfo | (dir << 16);
	}
	
	private  void setP2X(int x){
		playerInfo = playerInfo & -65025;
		playerInfo = playerInfo | (x << 9);
	}
	private  void setP2Y(int y){
		playerInfo = playerInfo & -509;
		playerInfo = playerInfo | (y << 2);
	}
	private  void setP2Dir(int dir){
		playerInfo = playerInfo & -4;
		playerInfo = playerInfo | dir;
	}
	private  byte getP1X(){
		return (byte)((playerInfo & -33554432) >>> 25);
	}
	private  byte getP1Y(){
		return (byte)((playerInfo & 33292288) >>> 18);
	}
	private  byte getP1Dir(){
		return (byte)((playerInfo & 196608) >>> 16);
	}
	private  byte getP2X(){
		return (byte)((playerInfo & 65024) >>> 9);
	}
	private  byte getP2Y(){
		return (byte)((playerInfo & 508) >>> 2);
	}
	private  byte getP2Dir(){
		return (byte)(playerInfo & 3);
	}
	
	private boolean getFilled(int x, int y){
		return spaces[x][y] == 1;
	}
	
	private void setBit(int x, int y, byte val){
		spaces[x][y] = val;
	}

	public void handleEvent(Action a) {
		if(a.getEventInfo().equals("start")){
			try{
				Thread.sleep(200);
			}catch (Exception e){
				e.printStackTrace();
			}
			inGame = true;
			delay = null;
		}
	}
	
	public ConnectionData getOtherPlayer(ConnectionData player){
		if(player.equals(p1)){
			return p2;
		}
		return p1;
	}

	@Override
	public void connectionSevered(ConnectionData connection) {
		inGame = false;
		if(connection.equals(p1)){
			p2.write(new Packet7PartnerDisconnect());
			p2.setNetworkHandler(gui);
		}else{
			p1.write(new Packet7PartnerDisconnect());
			p1.setNetworkHandler(gui);
		}
		gui.connectionSevered(connection);
	}

	public void handleInput(Object o, ConnectionData connection) {
		if(o instanceof Packet){
			switch(((Packet)o).getID()){
			case 2:
				if(connection.equals(p1)){
					p1Ready = true;
				}else{
					p2Ready = true;
				}
				break;
			case 4:
				connectionSevered(connection);
				break;
			case 8:
				if(connection.equals(p1)){
					p2.write(new Packet7PartnerDisconnect());
				}else{
					p1.write(new Packet7PartnerDisconnect());
				}
				running = false;
				inGame = false;
				break;
			}
		}else{
			if(connection.equals(p1)){
				setP1Dir((Byte)o);
			}else{
				setP2Dir((Byte)o);
			}
		}
		
	}
	public void restart(){
		clearBoard();
		resetPlayers();
		resetDirections();
		colorSpaces();
		p1.write(new Packet6GameStart());
		p2.write(new Packet6GameStart());
		delay = new Timer(5);
		delay.addAction(new Action(this, "start", 2.1f));
		delay.start();
		p1Ready = false;
		p2Ready = false;
	}

	private void clearBoard(){
		for(int r = 0; r < spaces.length; r++){
			for(int c = 0; c < spaces[r].length; c++){
				spaces[r][c] = 0;
			}
		}
	}
	
	private void resetPlayers(){
		setP1X(90);
		setP1Y(60);
		setP2X(30);
		setP2Y(60);
	}
	
	private void resetDirections(){
		setP1Dir(3);
		setP2Dir(1);
	}

	public void colorSpaces(){
		setBit(90, 60, (byte)1);
		setBit(30,60,(byte)1);
	}
}
