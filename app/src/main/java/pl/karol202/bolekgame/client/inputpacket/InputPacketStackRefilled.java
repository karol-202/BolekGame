package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketStackRefilled implements InputGamePacket
{
	private int totalActs;
	
	@Override
	public void readData(DataBundle bundle)
	{
		totalActs = bundle.getInt("totalActs", 0);
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onStackRefill(totalActs);
	}
}
