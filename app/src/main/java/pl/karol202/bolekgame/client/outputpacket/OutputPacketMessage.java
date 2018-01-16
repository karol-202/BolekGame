package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketMessage implements OutputPacket
{
	private String message;
	
	public OutputPacketMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("message", message);
	}
	
	@Override
	public String getName()
	{
		return "MESSAGE";
	}
}
