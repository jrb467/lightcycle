package server;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.*;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import packets.*;

import javax.swing.*;

import server.util.*;
import util.network.ConnectionData;
import util.network.Networked;

public class ServerGUI extends JFrame implements EditableField, Networked, WindowListener{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputDialogue;
	private JTextArea console;
	private JTextArea clientList;
	private InputHandler handler;
	private ServerSocket serverSocket;
	private ArrayList<ConnectionData> clients = new ArrayList<ConnectionData>();
	public ArrayList<Game> games = new ArrayList<Game>();
	private PacketHandler packetHandler;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
					frame.inputDialogue.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
        
		//set up frame dimensions and the content pane constraints
		setBounds(100, 100, 600, 500);
		addWindowListener(this);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][30:30:30]"));
		
		//initialize the frame
		setFrameStyle();
		initializeComponents(contentPane);
		initializeConnectionThread();
		
		//Make the input dialogue the default object for focus
		addWindowFocusListener(new WindowAdapter(){
			public void windowGainedFocus(WindowEvent e){
				inputDialogue.requestFocusInWindow();
			}
		});
		
		//create the input handler
		handler = new InputHandler(this);
		//create the packet handler
		packetHandler = new PacketHandler(this);
		UserManager.load();
	}
	
	//START frame initialization methods
	private void setFrameStyle(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e){}
	}
	
	private void initializeConnectionThread(){
		try {
			serverSocket = new ServerSocket(32042);
			Thread connectThread = new Thread(){
				public void run(){
					while(true){
						try {
							ConnectionData temp = new ConnectionData(getInstance(), serverSocket.accept());
							temp.setName("temp_"+(clients.size()+1)+"");
							clients.add(temp);
							updateClients();
							writeInterupt(temp.name+" connected from "+temp.address());
						} catch (Exception e){}
					}
				}
			};
			connectThread.start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//used so I don't have to create an entire class for the connect thread
	public ServerGUI getInstance(){
		return this;
	}
	
	private void initializeComponents(Container contentPane){
		//Sets up the split pane that contains the main console and the client list
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		splitPane.setContinuousLayout(true);
		contentPane.add(splitPane, "cell 0 0,grow");
		
		//initialize the actual fields
		initializeMainConsole(splitPane);
		initializeClientList(splitPane);
		initializeInputField(contentPane);
	}
	
	private void initializeMainConsole(JSplitPane splitPane){
		//sets up the scroll pane for the console
		JScrollPane consoleScrollPane = new JScrollPane();
		splitPane.setLeftComponent(consoleScrollPane);
		//adds in the text area
		console = new JTextArea();
		console.setEditable(false);
		consoleScrollPane.setViewportView(console);
	}
	
	private void initializeInputField(Container contentPane){
		inputDialogue = new JTextField();
		inputDialogue.setColumns(10);
		//Set up command processing when enter is typed
		inputDialogue.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					writeInput(inputDialogue.getText());
					handler.processString(getEditableField());
					setEditableField("");
				}
			}
		});
		contentPane.add(inputDialogue, "cell 0 1,growx");
	}
	
	private void initializeClientList(JSplitPane splitPane){
		//sets up the scrollpane for the client list
		JScrollPane clientScrollPane = new JScrollPane();
		splitPane.setRightComponent(clientScrollPane);
		//adds in the text area	
		clientList = new JTextArea();
		clientList.setEditable(false);
		clientScrollPane.setViewportView(clientList);
	}
	//END frame initialization methods
	
	//updates the client list
	public void updateClients(){
		clientList.setText("");
		for(ConnectionData c: clients){
			clientList.append(c.name+"\n");
		}
	}
	
	//START EditableField methods
	public void write(String s) {
		console.append((char)187+s+"\n");
	}
	
	public void writeInterupt(String s){
		if(console.getText().equals("") || console.getText().substring(console.getText().length()-1).equals("\n")){
			console.append((char)187+s+"\n");
		}else{
			console.append("\n"+(char)187+s+"\n");
		}
		scrollToBottom();
	}

	public String getEditableField() {
		return inputDialogue.getText();
	}

	public String getPermanentField() {
		return console.getText();
	}

	public void setEditableField(String s) {
		inputDialogue.setText(s);
	}

	public void setPermanentField(String s) {
		console.setText(s);
	}

	public void handleLineChange() {
		console.append((char)171+DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date())+(char)187+": ");
		scrollToBottom();
	}

	public void attemptToClose() {
		UserManager.save();
		System.exit(0);
	}
	
	public void writeInput(String s){
		handleLineChange();
		console.append(s+"\n");
		scrollToBottom();
	}
	
	private void scrollToBottom(){
		console.setCaretPosition(console.getText().length()-1);
	}
	//END EditableField methods

	//Networked method. Handles loss of connection to clients
	public void connectionSevered(ConnectionData connection) {
		clients.remove(connection);
		UserManager.getOnlineUsers().remove(UserManager.getUser(connection.name));
		connection.close();
		updateClients();
		writeInterupt(connection.name + " disconnected");
	}
	
	//Networked method. Handles client input
	public void handleInput(Object o, ConnectionData data){
		if(o instanceof Packet){
			writeInterupt("Packet Recieved: "+o.getClass().getCanonicalName());
			packetHandler.handlePacket((Packet)o, data);
		}else{
			if(data.name == null){
				data.name = o.toString();
			}else{
				write(data.name+": "+o.toString());
			}
		}
	}
	
	public void removeGame(Game g){
		games.remove(g);
	}
	
	public ArrayList<ConnectionData> getClients(){
		return clients;
	}
	
	public ConnectionData getFromName(String s){
		for(ConnectionData c: clients){
			if(c.name.equals(s)) return c;
		}
		return null;
	}
	
	public Game createGame(ConnectionData p1, ConnectionData p2){
		Game g = new Game(this,p1,p2);
		games.add(g);
		return g;
	}
	
	public boolean isInGame(ConnectionData s){
		for(Game g: games){
			if(s.equals(g.p1) || s.equals(g.p2)) return true;
		}
		return false;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		UserManager.save();
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		UserManager.save();
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
