package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import packets.Packet;
import packets.Packet11CreateAccount;
import packets.Packet13CreateAccountSucess;

import util.inputcheck.*;
import util.inputcheck.constraints.*;
import util.network.ConnectionData;
import util.network.Networked;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class CreateAccountDialogue extends JDialog implements WindowFocusListener, Networked{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private VerifiedField username;
	private VerifiedPasswordField pword;
	private VerifiedPasswordField pwordConfirm;
	private JButton ok;
	private ConnectionData server;
	private LoginBox box;

	/**
	 * Create the dialog.
	 */
	public CreateAccountDialogue(ConnectionData s, LoginBox b) {
		server = s;
		box = b;
		server.setNetworkHandler(this);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 240);
		addWindowFocusListener(this);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[100px:100px:100px][grow]", "[][15px:15px:15px][][15px:15px:15px][][15px:15px:15px]"));
		
		setLocation(b.getX()+(b.getWidth()-getWidth())/2, b.getY()+(b.getHeight()-getHeight())/2);
		
		{
			JLabel lblNewLabel = new JLabel("Username:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center");
		}
		{
			username = new VerifiedField();
			contentPanel.add(username, "cell 1 0,growx");
			username.setColumns(10);
			username.addConstraint(new LengthConstraint("Password must 6 or more characters", ">5"));
			username.addConstraint(new LengthConstraint("Password must 16 or less characters", "<17"));
			username.addConstraint(new CharacterConstraint("Password cannot contain symbols", "@","!","#","$","%","^","&","*","/", " "));
		}
		{
			JLabel lblNewLabel_3 = new JLabel("");
			lblNewLabel_3.setForeground(Color.RED);
			lblNewLabel_3.setBorder(new EmptyBorder(0, 10, 0, 0));
			lblNewLabel_3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			contentPanel.add(lblNewLabel_3, "cell 1 1");
			username.setLabel(lblNewLabel_3);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Password:");
			contentPanel.add(lblNewLabel_1, "cell 0 2,alignx center");
		}
		{
			pword = new VerifiedPasswordField();
			contentPanel.add(pword, "cell 1 2,growx");
			pword.setColumns(10);
			pword.addConstraint(new LengthConstraint("Password must 6 or more characters", ">5"));
			pword.addConstraint(new LengthConstraint("Password must 16 or less characters", "<17"));
			pword.addConstraint(new CharacterConstraint("Password cannot contain symbols", "@","!","#","$","%","^","&","*","/", " "));
		}
		{
			JLabel lblNewLabel_4 = new JLabel("");
			lblNewLabel_4.setBorder(new EmptyBorder(0, 10, 0, 0));
			lblNewLabel_4.setForeground(Color.RED);
			lblNewLabel_4.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			contentPanel.add(lblNewLabel_4, "cell 1 3");
			pword.setLabel(lblNewLabel_4);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Confirm Password:");
			lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			contentPanel.add(lblNewLabel_2, "cell 0 4,alignx center");
		}
		{
			pwordConfirm = new VerifiedPasswordField();
			contentPanel.add(pwordConfirm, "cell 1 4,growx");
			pwordConfirm.setColumns(10);
			pwordConfirm.addConstraint(new Constraint(){
				public String test(String s){
					if(s.equals(new String(pword.getPassword()))) return null;
					else return "Passwords do not match";
				}
			});
			pword.addCaretListener(pwordConfirm);
		}
		{
			JLabel lblNewLabel_5 = new JLabel("");
			lblNewLabel_5.setForeground(Color.RED);
			lblNewLabel_5.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
			lblNewLabel_5.setBorder(new EmptyBorder(0, 10, 0, 0));
			contentPanel.add(lblNewLabel_5, "cell 1 5");
			pwordConfirm.setLabel(lblNewLabel_5);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				ok = new JButton("OK");
				ok.setActionCommand("OK");
				ok.setAction(new AbstractAction("OK"){
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e){
						server.write(new Packet11CreateAccount(username.getText(), new String(pword.getPassword())));
					}
				});
				ok.setEnabled(false);
				buttonPane.add(ok);
				getRootPane().setDefaultButton(ok);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.setAction(new AbstractAction("Cancel"){
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e){
						server.setNetworkHandler(box);
						box.setEnabled(true);
						dispose();
					}
				});
			}
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		Thread testVerified = new Thread(){
			public void run(){
				while(isDisplayable()){
					if(!(username.isValidEntry() && pword.isValidEntry() && pwordConfirm.isValidEntry())){
						ok.setEnabled(false);
					}else{
						ok.setEnabled(true);
					}
					try{	Thread.sleep(100);	}catch(Exception e){}
				}
			}
		};
		testVerified.start();
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowLostFocus(WindowEvent arg0) {
		toFront();
	}

	@Override
	public void connectionSevered(ConnectionData connection) {
		setEnabled(false);
		setAlwaysOnTop(false);
		JOptionPane.showMessageDialog(this, "Disconnected from server", "Network Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	@Override
	public void handleInput(Object o, ConnectionData connection) {
		if(o instanceof Packet){
			switch(((Packet)o).getID()){
			case 13:
				if(((Packet13CreateAccountSucess)o).sucessful()){
					setAlwaysOnTop(false);
					removeWindowFocusListener(this);
					JOptionPane.showMessageDialog(box, "Account Created", "Account Created", JOptionPane.INFORMATION_MESSAGE);
					addWindowFocusListener(this);
					server.setNetworkHandler(box);
					box.setEnabled(true);
					dispose();
				}else{
					setAlwaysOnTop(false);
					removeWindowFocusListener(this);
					JOptionPane.showMessageDialog(this, "Failed to create account", "Creation Failure", JOptionPane.ERROR_MESSAGE);
					addWindowFocusListener(this);
				}
				break;
			case 15:
				break;
			}
		}
	}

}
