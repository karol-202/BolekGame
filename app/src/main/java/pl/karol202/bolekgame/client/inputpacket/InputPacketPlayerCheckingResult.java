package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPlayerCheckingResult implements InputPacket
{
	private int result;
	
	@Override
	public void readData(DataBundle bundle)
	{
		result = bundle.getInt("result", -1);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPlayerCheckingResult(result);
	}
}
