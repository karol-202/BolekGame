package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.control.ControlUI;
import pl.karol202.bolekgame.ui.game.GameUI;
import pl.karol202.bolekgame.ui.server.ServerUI;

public class InputPacketFailure implements InputControlPacket, InputServerPacket, InputGamePacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onFailure();
	}
	
	@Override
	public void execute(ServerUI ui)
	{
		ui.onFailure();
	}
	
	@Override
	public void execute(ControlUI ui)
	{
		ui.onFailure();
	}
}
