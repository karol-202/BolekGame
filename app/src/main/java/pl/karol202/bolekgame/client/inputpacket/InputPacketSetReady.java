package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketSetReady implements InputPacket
{
	private String username;
	
	@Override
	public void readData(DataBundle bundle)
	{
		username = bundle.getString("username", "");
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onSetReady(username);
	}
}
