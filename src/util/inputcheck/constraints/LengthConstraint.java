package util.inputcheck.constraints;


public class LengthConstraint implements Constraint{
	private byte op;
	private int num;
	private String message;
	
	public LengthConstraint(String message, String s){
		this.message = message;
		if(s.length() < 2){
			op = 0;
			num = 0;
		}else{
			switch(s.charAt(0)){
			case('<'):
				op = 1;
				break;
			case('>'):
				op = 2;
				break;
			case('='):
				op = 0;
				break;
			}
			s = s.substring(1);
			num = Integer.parseInt(s);
		}
	}
	public String test(String s) {
		switch(op){
		case 0:
			if(s.length() == num) return null; else return message;
		case 1:
			if(s.length() < num) return null; else return message;
		case 2:
			if(s.length() > num) return null; else return message;
		default:
			return "Error: incorrect operand";
		}
	}

}
