package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketPong implements OutputPacket
{
	@Override
	public void saveData(DataBundle bundle) { }
	
	@Override
	public String getName()
	{
		return "PONG";
	}
}
