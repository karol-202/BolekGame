package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketActPassed implements InputPacket
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
	public void execute(ClientListener listener)
	{
		listener.onActPass(lustrationPassed, antilustrationPassed);
	}
}
