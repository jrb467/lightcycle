package server.util.commands;

import server.util.*;

/*
 * Command used to clear the prompt screen
 */
public class ClearCommand extends Command {
	public static String name = "clear";
	public static String help = "Name: clear \n>Clears the command prompt";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "clear")){
			handler.field.setPermanentField("");
			handler.field.setEditableField("");
			return true;
		}
		return false;
	}

}
