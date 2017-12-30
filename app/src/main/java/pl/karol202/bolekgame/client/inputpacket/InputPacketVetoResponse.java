package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketVetoResponse implements InputGamePacket
{
	private boolean accepted;
	
	@Override
	public void readData(DataBundle bundle)
	{
		accepted = bundle.getBoolean("accepted", false);
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onVetoResponse(accepted);
	}
}
