package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPollIndexChange implements InputPacket
{
	private int pollIndex;
	
	@Override
	public void readData(DataBundle bundle)
	{
		pollIndex = bundle.getInt("pollIndex", 0);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPollIndexChange(pollIndex);
	}
}
