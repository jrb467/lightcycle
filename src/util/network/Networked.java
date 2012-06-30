package util.network;


public interface Networked {
	
	public void connectionSevered(ConnectionData connection);
	
	public void handleInput(Object o, ConnectionData connection);
}
