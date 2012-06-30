package client;

import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import packets.Packet;
import packets.Packet10Login;
import packets.Packet12LoginSucess;

import util.inputcheck.*;
import util.inputcheck.constraints.*;
import util.network.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LoginBox extends JFrame implements Networked, WindowListener{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VerifiedField textField;
	private VerifiedPasswordField passwordField;
	private JButton login;
	private JCheckBox rememberMe;
	private File loginDat = new File("misc/login.dat");
	private ObjectInputStream loginIn;
	private ObjectOutputStream loginOut;
	
	private ConnectionData server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new LoginBox();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginBox(ConnectionData s){
		server = s;
		server.setNetworkHandler(this);
		init();
	}
	
	public LoginBox() {
		Socket s = new Socket();
		try{
			s.connect(new InetSocketAddress("69.207.156.48", 32042), 1000);
		}catch (IOException e){
			JOptionPane.showMessageDialog(this, "Disconnected from server", "Network Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		server = new ConnectionData(this, s);
		init();
	}
	
	private void init(){
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e){}
		setBounds(100, 100, 400, 250);
		setResizable(false);
		addWindowListener(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[90px:90px:90px][grow]", "[][15px:15px:15px][][15px:15px:15px][][][][]"));
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width-getWidth())/2, (screen.height-getHeight())/2);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, "cell 0 0,alignx center,aligny center");
		
		textField = new VerifiedField();
		contentPane.add(textField, "cell 1 0,growx");
		textField.setColumns(10);
		textField.addConstraint(new LengthConstraint("Cannot contain fewer than 6 characters", ">5"));
		textField.addConstraint(new LengthConstraint("Cannot contain more than 16 characters", "<17"));
		textField.addConstraint(new CharacterConstraint("Cannot contain symbols", "@","!","#","$","%","^","&","*","/", " "));
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setBorder(new EmptyBorder(0, 10, 0, 0));
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		contentPane.add(lblNewLabel_2, "cell 1 1");
		textField.setLabel(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_1, "cell 0 2,alignx center");
		
		passwordField = new VerifiedPasswordField();
		contentPane.add(passwordField, "cell 1 2,growx");
		passwordField.addConstraint(new LengthConstraint("Cannot contain fewer than 6 characters", ">5"));
		passwordField.addConstraint(new LengthConstraint("Cannot contain more than 16 characters", "<17"));
		passwordField.addConstraint(new CharacterConstraint("Cannot contain symbols", "@","!","#","$","%","^","&","*","/", " "));
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBorder(new EmptyBorder(0, 10, 0, 0));
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblNewLabel_3.setForeground(Color.RED);
		contentPane.add(lblNewLabel_3, "cell 1 3");
		passwordField.setLabel(lblNewLabel_3);
		
		rememberMe = new JCheckBox("Remember Me");
		contentPane.add(rememberMe, "cell 1 4,alignx center,aligny center");
		
		login = new JButton("Login");
		login.setMaximumSize(new Dimension(2000, 2000));
		login.setEnabled(false);
		login.setAction(new AbstractAction("Login"){
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				server.write(new Packet10Login(textField.getText(), new String(passwordField.getPassword())));
			}
		});
		getRootPane().setDefaultButton(login);
		contentPane.add(login, "cell 0 5 2 1,growx,aligny center");
		
		JButton btnNewButton_1 = new JButton("Create Profile");
		contentPane.add(btnNewButton_1, "cell 0 6 2 1,growx,aligny center");
		btnNewButton_1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				setEnabled(false);
				Thread h = new Thread(){
					public void run(){
						CreateAccountDialogue create = new CreateAccountDialogue(server, getInstance());
						server.setNetworkHandler(create);
					}
				};
				h.start();
			}
		});
		
		try {
			loginIn = new ObjectInputStream(new FileInputStream(loginDat));
			
			Object o = loginIn.readObject();
			textField.setText((String)o);
			o = loginIn.readObject();
			passwordField.setText((String)o);
			rememberMe.setSelected(true);
			loginIn.close();
		} catch (Exception e){
			if(loginIn != null){
				try{loginIn.close();}catch(Exception e1){}
			}
			loginIn = null;
			textField.setText("");
			passwordField.setText("");
			rememberMe.setSelected(false);
		}
		setVisible(true);
		
		Thread testVerified = new Thread(){
			public void run(){
				while(isDisplayable()){
					if(!(textField.isValidEntry() && passwordField.isValidEntry())){
						login.setEnabled(false);
					}else{
						login.setEnabled(true);
					}
					try{	Thread.sleep(100);	}catch(Exception e){}
				}
			}
		};
		testVerified.start();
	}
	
	public void close(){
		if(rememberMe.isSelected()){
			try{
				loginOut = new ObjectOutputStream(new FileOutputStream(loginDat));
				loginOut.writeObject(textField.getText());
				loginOut.writeObject(new String(passwordField.getPassword()));
				loginOut.flush();
				loginOut.close();
			}catch(Exception e){
				loginOut = null;
			}
		}else{
			try{
				loginOut = new ObjectOutputStream(new FileOutputStream(loginDat));
				loginOut.close();
			}catch(Exception e){
				loginOut = null;
			}
		}
		server = null;
	}
	
	public LoginBox getInstance(){
		return this;
	}

	@Override
	public void connectionSevered(ConnectionData connection) {
		JOptionPane.showMessageDialog(this, "Disconnected from server", "Network Error", JOptionPane.ERROR_MESSAGE);
		close();
		System.exit(0);
	}

	@Override
	public void handleInput(Object o, ConnectionData connection) {
		if(o instanceof Packet){
			switch(((Packet)o).getID()){
			case 12:
				if(((Packet12LoginSucess)o).sucessful()){
					new Tron(server, getInstance());
					close();
					dispose();
				}else{
					JOptionPane.showMessageDialog(this, "Login Failed", "Login Failure", JOptionPane.ERROR_MESSAGE);
				}
				break;
			case 4:
				connectionSevered(connection);
				break;
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0){
		close();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		close();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

}
