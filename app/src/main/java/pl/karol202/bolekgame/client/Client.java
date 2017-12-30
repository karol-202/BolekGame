package pl.karol202.bolekgame.client;

import pl.karol202.bolekgame.client.inputpacket.*;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketEncoder;
import pl.karol202.bolekgame.control.ControlLogic;
import pl.karol202.bolekgame.game.GameLogic;
import pl.karol202.bolekgame.server.ServerLogic;

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
	
	private ControlLogic controlLogic;
	private ServerLogic serverLogic;
	private GameLogic gameLogic;
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
		if(packet instanceof InputControlPacket && controlLogic != null) ((InputControlPacket) packet).execute(controlLogic);
		if(packet instanceof InputServerPacket && serverLogic != null) ((InputServerPacket) packet).execute(serverLogic);
		if(packet instanceof InputGamePacket && gameLogic != null) ((InputGamePacket) packet).execute(gameLogic);
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
	
	public void setControlLogic(ControlLogic controlLogic)
	{
		this.controlLogic = controlLogic;
	}
	
	public void setServerLogic(ServerLogic serverLogic)
	{
		this.serverLogic = serverLogic;
	}
	
	public void setGameLogic(GameLogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
	
	public void setOnDisconnectListener(OnDisconnectListener listener)
	{
		this.onDisconnectListener = listener;
	}
}
