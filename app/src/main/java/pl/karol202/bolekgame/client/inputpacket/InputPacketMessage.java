package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketMessage implements InputPacket
{
	private String sender;
	private String message;
	private boolean newMessage;
	
	@Override
	public void readData(DataBundle bundle)
	{
		sender = bundle.getString("sender", "");
		message = bundle.getString("message", "");
		newMessage = bundle.getBoolean("newMessage", false);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onMessage(sender, message, newMessage);
	}
}
