package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketPresidentCheckingPlayer implements InputGamePacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPresidentCheckingPlayer();
	}
}
