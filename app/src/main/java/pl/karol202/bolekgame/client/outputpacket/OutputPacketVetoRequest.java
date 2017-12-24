package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketVetoRequest implements OutputPacket
{
	@Override
	public void saveData(DataBundle bundle) { }
	
	@Override
	public String getName()
	{
		return "VETOREQUEST";
	}
}
