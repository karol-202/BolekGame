package pl.karol202.bolekgame.client;

import pl.karol202.bolekgame.Utils;
import pl.karol202.bolekgame.client.inputpacket.*;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketEncoder;
import pl.karol202.bolekgame.ui.control.ControlUI;
import pl.karol202.bolekgame.ui.game.GameUI;
import pl.karol202.bolekgame.ui.server.ServerUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{
	public interface OnDisconnectListener
	{
		void onDisconnect();
	}
	
	private static final int PORT = 6006;
	
	private ControlUI controlUI;
	private ServerUI serverUI;
	private GameUI gameUI;
	private OnDisconnectListener onDisconnectListener;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public boolean connect(String host)
	{
		return tryToInitConnection(host);
	}
	
	private boolean tryToInitConnection(String host)
	{
		try
		{
			initConnection(host);
			return true;
		}
		catch(IOException e)
		{
			exception("Cannot connect to server.", e);
			return false;
		}
	}
	
	private void initConnection(String host) throws IOException
	{
		socket = new Socket(host, PORT);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}
	
	public void run()
	{
		new Thread(this::tryToListen).start();
	}
	
	private void tryToListen()
	{
		try
		{
			listen();
		}
		catch(IOException e)
		{
			exception("Error while listening.", e);
		}
	}
	
	private void listen() throws IOException
	{
		if(!isConnected()) return;
		while(isConnected())
		{
			InputPacket packet = receivePacket();
			if(packet != null) executePacket(packet);
			else break;
		}
		closeSocket();
	}
	
	private InputPacket receivePacket() throws IOException
	{
		int length = Utils.readInt(inputStream);
		if(length <= 0) return null;
		byte[] bytes = new byte[length];
		int bytesRead = inputStream.read(bytes);
		if(bytesRead != length) return null;
		
		return InputPacketFactory.createPacket(bytes);
	}
	
	private void executePacket(InputPacket packet)
	{
		if(packet instanceof InputControlPacket && controlUI != null) ((InputControlPacket) packet).execute(controlUI);
		else if(packet instanceof InputServerPacket && serverUI != null) ((InputServerPacket) packet).execute(serverUI);
		else if(packet instanceof InputGamePacket && gameUI != null) ((InputGamePacket) packet).execute(gameUI);
	}
	
	public void sendPacket(OutputPacket packet)
	{
		try
		{
			writePacket(packet);
		}
		catch(IOException e)
		{
			exception("Cannot send packet.", e);
		}
	}
	
	private void writePacket(OutputPacket packet) throws IOException
	{
		byte[] bytes = OutputPacketEncoder.encodePacket(packet);
		if(bytes == null || bytes.length == 0 && !isConnected()) return;
		outputStream.write(Utils.writeInt(bytes.length));
		outputStream.write(bytes);
	}
	
	public void disconnect()
	{
		closeSocket();
	}
	
	private void closeSocket()
	{
		if(socket == null) return;
		if(onDisconnectListener != null) onDisconnectListener.onDisconnect();
		if(!isConnected()) return;
		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			new ClientException("Cannot close socket", e).printStackTrace();
		}
	}
	
	public boolean isConnected()
	{
		return socket != null &&socket.isConnected() && !socket.isClosed();
	}
	
	private void exception(String message, Exception exception)
	{
		new ClientException(message, exception).printStackTrace();
		closeSocket();
	}
	
	public void setControlUI(ControlUI controlUI)
	{
		this.controlUI = controlUI;
		this.serverUI = null;
		this.gameUI = null;
	}
	
	public void setServerUI(ServerUI serverUI)
	{
		this.controlUI = null;
		this.serverUI = serverUI;
		this.gameUI = null;
	}
	
	public void setGameUI(GameUI gameUI)
	{
		this.controlUI = null;
		this.serverUI = null;
		this.gameUI = gameUI;
	}
	
	public void setOnDisconnectListener(OnDisconnectListener listener)
	{
		this.onDisconnectListener = listener;
	}
}
