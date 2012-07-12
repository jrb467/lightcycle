package util.network;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class PrimitiveConnectionData {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private PrimitiveNetworked network;
	public String name;
	
	public PrimitiveConnectionData(PrimitiveNetworked network, Socket socket){
		this.socket = socket;
		this.network = network;
		try{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			Thread recieve = new Thread(){
				public void run(){
					while(true){
						try{
							int i = in.read();
							getNetwork().handleInput(i, getInstance());
						}catch (SocketException e) {
							getNetwork().connectionSevered(getInstance());
			                break;
			            } catch (EOFException e) {
			                getNetwork().connectionSevered(getInstance());
			                break;
			            }catch (Exception e){
			            	e.printStackTrace();
			            }
					}
				}
			};
			recieve.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String address(){
		return socket.getInetAddress().getHostAddress();
	}
	
	public int port(){
		return socket.getLocalPort();
	}
	
	public PrimitiveConnectionData getInstance(){
		return this;
	}
	
	public PrimitiveNetworked getNetwork(){
		return network;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean isClosed(){
		return socket.isClosed();
	}
	
	public synchronized void write(int i){
		try{
			out.write(i);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized void write(byte b){
		try{
			out.write(b);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean connected(){
		return out != null && in != null && socket != null;
	}
	
	public synchronized void close(){
		try{
			in.close();
			out.flush();
			socket.close();
		}catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	public void setNetworkHandler(PrimitiveNetworked network){
		this.network = network;
	}
}
