package pl.karol202.bolekgame.client;

import com.crashlytics.android.Crashlytics;
import pl.karol202.bolekgame.client.inputpacket.InputPacket;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFactory;
import pl.karol202.bolekgame.client.inputpacket.InputPacketPing;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketEncoder;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketPong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Client
{
	public static final int API_VERSION = 4;
	
	private static final int PORT = 60606;
	private static final int TIMEOUT = 5000;
	
	private ClientListener clientListener;
	private boolean suspendPacketExecution;
	private Queue<InputPacket> packetBuffer;
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public Client()
	{
		packetBuffer = new LinkedList<>();
	}
	
	public boolean connect(String host)
	{
		return tryToInitConnection(host);
	}
	
	private boolean tryToInitConnection(String host)
	{
		try
		{
			return initConnection(host);
		}
		catch(Exception e)
		{
			socket = null;
			exception("Cannot connect to server.", e);
			return false;
		}
	}
	
	private boolean initConnection(String host) throws IOException
	{
		if(isConnected()) return true;
		if(host == null || host.isEmpty()) throw new IllegalArgumentException("Host cannot be null.");
		socket = new Socket();
		socket.connect(new InetSocketAddress(host, PORT), TIMEOUT);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		return true;
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
			if(packet == null) break;
			
			if(packet instanceof InputPacketPing) handlePingPacket();
			else if(!suspendPacketExecution) executePacket(packet);
			else packetBuffer.offer(packet);
		}
		closeSocket();
	}
	
	private InputPacket receivePacket() throws IOException
	{
		int length = Utils.readInt(inputStream);
		if(length <= 0)
		{
			Crashlytics.logException(new CorruptedPacketException("Packet has length lesser than 1: " + length));
			return null;
		}
		
		byte[] bytes = new byte[length];
		int bytesRead = inputStream.read(bytes);
		if(bytesRead != length)
		{
			Crashlytics.setString("Packet", new String(bytes));
			Crashlytics.logException(new CorruptedPacketException("Packet length mismatch. Expected: " + length + ", received: " + bytesRead));
			return null;
		}
		
		InputPacket packet = InputPacketFactory.createPacket(bytes);
		if(packet == null)
		{
			Crashlytics.setString("Packet", new String(bytes));
			Crashlytics.logException(new CorruptedPacketException("Packet is corrupted."));
			return null;
		}
		return packet;
	}
	
	private void handlePingPacket() throws IOException
	{
		writePacket(new OutputPacketPong());
	}
	
	private void executePacket(InputPacket packet)
	{
		if(clientListener != null) packet.execute(clientListener);
	}
	
	public synchronized void sendPacket(OutputPacket packet)
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
	
	private void closeSocket()
	{
		if(socket == null) return;
		if(clientListener != null) clientListener.onDisconnect();
		if(!isConnected()) return;
		try
		{
			//Temporary
			new ClientException("Closing socket(temporary exception)").printStackTrace();
			socket.close();
		}
		catch(IOException e)
		{
			new ClientException("Cannot close socket", e).printStackTrace();
		}
		socket = null;
	}
	
	private void exception(String message, Exception exception)
	{
		new ClientException(message, exception).printStackTrace();
		closeSocket();
	}
	
	public boolean isConnected()
	{
		return socket != null && socket.isConnected() && !socket.isClosed();
	}
	
	public synchronized void setClientListener(ClientListener listener)
	{
		this.clientListener = listener;
		if(listener == null) suspendPacketExecution();
		else resumePacketExecution();
	}
	
	private void suspendPacketExecution()
	{
		suspendPacketExecution = true;
	}
	
	private void resumePacketExecution()
	{
		suspendPacketExecution = false;
		while(!packetBuffer.isEmpty()) executePacket(packetBuffer.poll());
	}
}
