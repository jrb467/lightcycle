package util.inputcheck.constraints;


public class CharacterConstraint implements Constraint {
	private String[] chars;
	private String message;
	
	public CharacterConstraint(String errorMessage, String... c){
		chars = c;
		message = errorMessage;
	}
	
	public String test(String s) {
		for(String c: chars){
			if(s.contains(c)) return message;
		}
		return null;
	}

}
