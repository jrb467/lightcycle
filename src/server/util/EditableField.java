package server.util;

public interface EditableField {
	
	public void write(String s);
	
	public void writeInterupt(String s);
	
	public void writeInput(String s);
	
	public String getEditableField();
	
	public String getPermanentField();
	
	public void setEditableField(String s);
	
	public void setPermanentField(String s);
	
	public void handleLineChange();
	
	public void attemptToClose();

}
