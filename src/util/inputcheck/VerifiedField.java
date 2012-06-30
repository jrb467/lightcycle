package util.inputcheck;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import util.inputcheck.constraints.Constraint;

public class VerifiedField extends JTextField implements CaretListener{
	private static final long serialVersionUID = 1L;
	private Image check = getImage("misc/check.gif");
	private Image x = getImage("misc/x.png");
	private boolean valid = false;
	private String validmessage;
	private JLabel errorLabel;
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	
	
	public VerifiedField(){
		super();
		this.addCaretListener(this);
		validateField();
	}
	
	public VerifiedField(JLabel label){
		super();
		errorLabel = label;
		this.addCaretListener(this);
		validateField();
	}
	
	public void addConstraint(Constraint c){
		constraints.add(c);
		validateField();
	}
	
	public void removeConstraint(Constraint c){
		constraints.remove(c);
		validateField();
	}
	
	public void setLabel(JLabel label){
		errorLabel = label;
		if(valid){
			if(errorLabel != null){
				errorLabel.setText("Valid Entry");
				errorLabel.setForeground(Color.green.darker());
			}
		}else{
			if(errorLabel != null){
				errorLabel.setText(validmessage);
				errorLabel.setForeground(Color.red);
			}
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics g2 = g.create();
		int padding = (getHeight()-16)/2;
		if(valid)
			g2.drawImage(check, this.getWidth()-16-padding, padding, null);
		else
			g2.drawImage(x, this.getWidth()-16-padding, padding, null);
		g2.dispose();
	}
	
	private Image getImage(String url){
		try {
			return ImageIO.read(new File(url));
		} catch (IOException e) {
			return null;
		}
	}
	
	private void validateField(){
		String s = getText();
		valid = true;
		for(Constraint c: constraints){
			if((validmessage = c.test(s)) != null){
				valid = false;
				break;
			}
		}
		if(valid){
			if(errorLabel != null){
				errorLabel.setText("Valid Entry");
				errorLabel.setForeground(Color.green.darker());
			}
		}else{
			if(errorLabel != null){
				errorLabel.setText(validmessage);
				errorLabel.setForeground(Color.red);
			}
		}
	}
	
	public String getMessage(){
		return validmessage;
	}
	
	public boolean isValidEntry(){
		return valid;
	}

	public void caretUpdate(CaretEvent arg0) {
		validateField();
	}
}
