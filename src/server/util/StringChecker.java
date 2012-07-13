package server.util;

import java.util.Scanner;

public class StringChecker {
	
	//Utility method to separate words and create a stringlist of them
	public static StringList separate(String input){
		StringList returnValue = new StringList();
		Scanner scan = new Scanner(input);
		while(scan.hasNext()){
			returnValue.add(scan.next());
		}
		scan.close();
		return returnValue;
	}
	
	//tests a word from a stringlist to see if it matches the test case
	public static boolean testWord(StringList words, int index, String testCase){
		if(words.get(index).equals(testCase) && words.size() > index){
			return true;
		}
		return false;
	}
	
	//tests a word from a stringlist against various actionvars(each one performs a different action if selected)
	//Returns the index of the selected var
	public static int testWord(StringList words, int index, String[] validOptions){
		if(words.size() <= index) return -1;
		for(int i = 0; i < validOptions.length; i++){
			if(words.get(index).equals(validOptions[i])) return i;
		}
		return -1;
	}
	
	/*
	 * Recursive method used to test a string against a format (such as an IP address). Format rules follow:
	 * 
	 * # = required digit
	 * x = required letter
	 * . = required period
	 * 0 = optional digit
	 * v = optional letter
	 */
 	public static boolean testFormat(String word, String format, int index, int formIndex){
		if(index > word.length()-1 && formIndex > format.length()-1) return true;
		if(formIndex > format.length()-1 || index > word.length()-1) return false;
		switch(format.charAt(formIndex)){
		case '#':
			if(!Character.isDigit(word.charAt(index))) return false;
			return testFormat(word, format, index+1, formIndex+1);
		case 'x':
			if(!Character.isLetter(word.charAt(index))) return false;
			return testFormat(word, format, index+1, formIndex+1);
		case '0':
			if(!Character.isDigit(word.charAt(index))){
				return testFormat(word, format, index, formIndex+1);
			}else{
				return testFormat(word, format, index, formIndex+1) || testFormat(word, format, index+1, formIndex+1);
			}
		case 'v':
			if(!Character.isLetter(word.charAt(index))){
				return testFormat(word, format, index+1, formIndex+1);
			}else{
				return testFormat(word, format, index, formIndex+1) || testFormat(word, format, index+1, formIndex+1);
			}
		default:
			if(word.charAt(index) != format.charAt(formIndex)) return false;
			return testFormat(word, format, index+1, formIndex+1);
		}
	}
	
 	//test format method with a StringList paramater
	public static boolean testFormat(StringList words, int index, String format){
		if(words.size() <= index){
			return false;
		}
		String word = words.get(index);
		return testFormat(word, format, 0, 0);
	}

}
