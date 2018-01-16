package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketMessage implements InputPacket
{
	private String sender;
	private String message;
	
	@Override
	public void readData(DataBundle bundle)
	{
		sender = bundle.getString("sender", "");
		message = bundle.getString("message", "");
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onMessage(sender, message);
	}
}
