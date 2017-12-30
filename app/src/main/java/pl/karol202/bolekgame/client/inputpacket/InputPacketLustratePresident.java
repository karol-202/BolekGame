package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketLustratePresident implements InputGamePacket
{
	@Override
	public void readData(DataBundle bundle)
	{ }
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onLustratePresidentRequest();
	}
}
