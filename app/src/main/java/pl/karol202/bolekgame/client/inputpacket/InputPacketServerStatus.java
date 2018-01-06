package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketServerStatus implements InputPacket
{
	private boolean gameAvailable;
	
	@Override
	public void readData(DataBundle bundle)
	{
		gameAvailable = bundle.getBoolean("gameAvailable", false);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onServerStatusUpdate(gameAvailable);
	}
}
