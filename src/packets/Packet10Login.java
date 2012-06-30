package packets;

public class Packet10Login extends Packet {
	private static final long serialVersionUID = 1L;
	private String username;
	private String pword;
	
	public Packet10Login(String user, String pass){
		super((byte)10);
		username = user;
		pword = pass;
	}
	
	public String getUser(){
		return username;
	}
	
	public String getPassword(){
		return pword;
	}
}
