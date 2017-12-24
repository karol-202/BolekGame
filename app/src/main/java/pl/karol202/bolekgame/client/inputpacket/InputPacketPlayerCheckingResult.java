package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketPlayerCheckingResult implements InputGamePacket
{
	private int result;
	
	@Override
	public void readData(DataBundle bundle)
	{
		result = bundle.getInt("result", -1);
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPlayerCheckingResult(result);
	}
}
