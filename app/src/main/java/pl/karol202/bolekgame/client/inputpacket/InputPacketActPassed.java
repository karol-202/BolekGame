package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketActPassed implements InputGamePacket
{
	private int lustrationPassed;
	private int antilustrationPassed;
	
	@Override
	public void readData(DataBundle bundle)
	{
		lustrationPassed = bundle.getInt("lustrationPassed", 0);
		antilustrationPassed = bundle.getInt("antilustrationPassed", 0);
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onActPass(lustrationPassed, antilustrationPassed);
	}
}
