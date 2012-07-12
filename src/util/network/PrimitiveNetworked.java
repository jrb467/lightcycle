package util.network;

public interface PrimitiveNetworked {
	
	public void connectionSevered(PrimitiveConnectionData connection);
	
	public void handleInput(int i, PrimitiveConnectionData connection);
}
