package util;

import java.awt.event.MouseAdapter;

import javax.swing.JButton;

public abstract class ButtonAdapter extends MouseAdapter {
	protected JButton button;
	
	public ButtonAdapter(JButton button){
		super();
		this.button = button;
	}
}
