package packets;

public class Packet11CreateAccount extends Packet {
	private static final long serialVersionUID = 1L;
	private String user;
	private String pword;
	
	public Packet11CreateAccount(String user, String pword){
		super((byte)11);
		this.user = user;
		this.pword = pword;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPassword(){
		return pword;
	}
}
