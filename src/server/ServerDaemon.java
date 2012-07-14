package server;

import java.net.ServerSocket;
import java.util.ArrayList;
import packets.*;
import util.network.ConnectionData;
import util.network.Networked;

public class ServerDaemon implements Networked{
	private ServerSocket serverSocket;
	private ArrayList<ConnectionData> clients = new ArrayList<ConnectionData>();
	public ArrayList<Game> games = new ArrayList<Game>();
	private PacketHandler packetHandler;

	/**
	 * Create the frame.
	 */
	public ServerDaemon() {
		initializeConnectionThread();
		packetHandler = new PacketHandler(this);
		UserManager.load();
	}
	
	private void initializeConnectionThread(){
		try {
			serverSocket = new ServerSocket(32042);
			Thread connectThread = new Thread(){
				public void run(){
					while(true){
						try {
							ConnectionData temp = new ConnectionData(getInstance(), serverSocket.accept());
							temp.setName("temp_"+(clients.size()+1)+"");
							clients.add(temp);
						} catch (Exception e){}
					}
				}
			};
			connectThread.start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//used so I don't have to create an entire class for the connect thread
	public ServerDaemon getInstance(){
		return this;
	}

	public void close() {
		UserManager.save();
		System.exit(0);
	}

	//Networked method. Handles loss of connection to clients
	public void connectionSevered(ConnectionData connection) {
		clients.remove(connection);
		UserManager.getOnlineUsers().remove(UserManager.getUser(connection.name));
		connection.close();
	}
	
	//Networked method. Handles client input
	public void handleInput(Object o, ConnectionData data){
		if(o instanceof Packet){
			packetHandler.handlePacket((Packet)o, data);
		}
	}
	
	public void removeGame(Game g){
		games.remove(g);
	}
	
	public ArrayList<ConnectionData> getClients(){
		return clients;
	}
	
	public ConnectionData getFromName(String s){
		for(ConnectionData c: clients){
			if(c.name.equals(s)) return c;
		}
		return null;
	}
	
	public Game createGame(ConnectionData p1, ConnectionData p2){
		Game g = new Game(this,p1,p2);
		games.add(g);
		return g;
	}
	
	public boolean isInGame(ConnectionData s){
		for(Game g: games){
			if(s.equals(g.p1) || s.equals(g.p2)) return true;
		}
		return false;
	}
}
