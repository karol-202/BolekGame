package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPresidentChoosingActs implements InputPacket
{
	@Override
	public void readData(DataBundle bundle) { }
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPresidentChoosingActs();
	}
}
