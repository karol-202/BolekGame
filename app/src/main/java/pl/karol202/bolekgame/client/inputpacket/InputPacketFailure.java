package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.control.ControlLogic;
import pl.karol202.bolekgame.game.GameLogic;
import pl.karol202.bolekgame.server.ServerLogic;

public class InputPacketFailure implements InputControlPacket, InputServerPacket, InputGamePacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onFailure();
	}
	
	@Override
	public void execute(ServerLogic ui)
	{
		ui.onFailure();
	}
	
	@Override
	public void execute(ControlLogic ui)
	{
		ui.onFailure();
	}
}
