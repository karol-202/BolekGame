package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketStackRefilled implements InputPacket
{
	private int totalActs;
	
	@Override
	public void readData(DataBundle bundle)
	{
		totalActs = bundle.getInt("totalActs", 0);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onStackRefill(totalActs);
	}
}
