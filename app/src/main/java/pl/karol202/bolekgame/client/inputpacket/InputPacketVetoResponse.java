package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketVetoResponse implements InputPacket
{
	private boolean accepted;
	
	@Override
	public void readData(DataBundle bundle)
	{
		accepted = bundle.getBoolean("accepted", false);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onVetoResponse(accepted);
	}
}
