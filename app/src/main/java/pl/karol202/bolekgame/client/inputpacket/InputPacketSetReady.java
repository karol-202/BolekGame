package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.server.ServerUI;

public class InputPacketSetReady implements InputServerPacket
{
	private String username;
	
	@Override
	public void readData(DataBundle bundle)
	{
		username = bundle.getString("username", "");
	}
	
	@Override
	public void execute(ServerUI ui)
	{
		ui.onSetReady(username);
	}
}
