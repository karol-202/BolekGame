package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.server.ServerUI;

public class InputPacketServerStatus implements InputServerPacket
{
	private boolean gameAvailable;
	
	@Override
	public void readData(DataBundle bundle)
	{
		gameAvailable = bundle.getBoolean("gameAvailable", false);
	}
	
	@Override
	public void execute(ServerUI ui)
	{
		ui.onServerStatusUpdate(gameAvailable);
	}
}
