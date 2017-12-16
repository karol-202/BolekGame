package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.server.ServerUI;

public class InputPacketLoggedOut implements InputServerPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(ServerUI ui)
	{
		ui.onLoggedOut();
	}
}
