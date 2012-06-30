package packets;

import java.io.Serializable;

public class Packet implements Serializable{
    private static final long serialVersionUID = 1L;
    public byte id;

    public Packet(byte id){
        this.id = id;
    }
    
    public byte getID(){
    	return id;
    }
}
