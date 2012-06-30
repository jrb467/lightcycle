package packets;

import java.util.ArrayList;

public class Packet3Clients extends Packet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> clients = new ArrayList<String>();
	
	public Packet3Clients(ArrayList<String> connections){
		super((byte)3);
		clients = connections;
	}
	
	public ArrayList<String> getClients(){
		return clients;
	}
}
