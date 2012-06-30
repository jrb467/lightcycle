package server.util.commands;

import server.util.*;

/*
 * Command used to close the prompt
 */
public class CloseCommand extends Command{
	public static String name = "close";
	public static String help = "Name: close \n>Closes the prompt";
	
	public static boolean test(InputHandler handler, StringList words){
		if(words.size() > 0 && words.get(0).equals("close")){
			handler.field.attemptToClose();
			return true;
		}
		return false;
	}
}
