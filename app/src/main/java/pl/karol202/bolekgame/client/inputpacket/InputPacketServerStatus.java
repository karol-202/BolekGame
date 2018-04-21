package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketServerStatus implements InputPacket
{
	private boolean gameAvailable;
	private int minUsers;
	
	@Override
	public void readData(DataBundle bundle)
	{
		gameAvailable = bundle.getBoolean("gameAvailable", false);
		minUsers = bundle.getInt("minUsers", 5);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onServerStatusUpdate(gameAvailable, minUsers);
	}
}
