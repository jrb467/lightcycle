package server.util;

import java.util.ArrayList;

public class StringList extends ArrayList<String> {
	private static final long serialVersionUID = 1L;
	
	public StringList(){
		super();
	}
	
	public String get(int index){
		if(size() <= index){
			return "null";
		}
		return super.get(index);
	}
}
