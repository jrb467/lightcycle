package server.util;

import server.util.commands.Command;

/*
 * Designed to handle text input from the terminal prompt, 
 * and updates the display and variables accordingly
 * 
 */
public class InputHandler {
	public EditableField field;
	
	public InputHandler(EditableField f){
		field = f;
	}
	
	public void processString(String str){
		StringList cur = StringChecker.separate(str);
		for(Class<? extends Command> c: Command.commands){
			try {
				if((Boolean)c.getMethod("test", InputHandler.class, StringList.class).invoke(null, this, cur)){
					return;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		field.write("Not a valid command");
	}
}
