package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.server.ServerLogic;

public class InputPacketLoggedOut implements InputServerPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(ServerLogic ui)
	{
		ui.onLoggedOut();
	}
}
