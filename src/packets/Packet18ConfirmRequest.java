package packets;


public class Packet18ConfirmRequest extends Packet {
	private static final long serialVersionUID = 1L;
	private String user;
	
	public Packet18ConfirmRequest(String user){
		super((byte)18);
		this.user = user;
	}
	
	public String getUser(){
		return user;
	}
}
