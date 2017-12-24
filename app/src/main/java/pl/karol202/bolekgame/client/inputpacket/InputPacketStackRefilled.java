package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketStackRefilled implements InputGamePacket
{
	private int totalActs;
	
	@Override
	public void readData(DataBundle bundle)
	{
		totalActs = bundle.getInt("totalActs", 0);
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onStackRefill(totalActs);
	}
}
