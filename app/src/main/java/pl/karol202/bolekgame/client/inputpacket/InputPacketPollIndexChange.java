package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketPollIndexChange implements InputGamePacket
{
	private int pollIndex;
	
	@Override
	public void readData(DataBundle bundle)
	{
		pollIndex = bundle.getInt("pollIndex", 0);
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onPollIndexChange(pollIndex);
	}
}
