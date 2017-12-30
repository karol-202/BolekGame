package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.server.ServerLogic;

public class InputPacketServerStatus implements InputServerPacket
{
	private boolean gameAvailable;
	
	@Override
	public void readData(DataBundle bundle)
	{
		gameAvailable = bundle.getBoolean("gameAvailable", false);
	}
	
	@Override
	public void execute(ServerLogic ui)
	{
		ui.onServerStatusUpdate(gameAvailable);
	}
}
