package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketPollIndexChange implements InputGamePacket
{
	private int pollIndex;
	
	@Override
	public void readData(DataBundle bundle)
	{
		pollIndex = bundle.getInt("pollIndex", 0);
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPollIndexChange(pollIndex);
	}
}
