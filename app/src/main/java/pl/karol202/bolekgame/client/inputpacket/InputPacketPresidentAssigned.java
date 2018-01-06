package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPresidentAssigned implements InputPacket
{
	private String president;
	
	@Override
	public void readData(DataBundle bundle)
	{
		president = bundle.getString("president", "");
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPresidentAssignment(president);
	}
}
