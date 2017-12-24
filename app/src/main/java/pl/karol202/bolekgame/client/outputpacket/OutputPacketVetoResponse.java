package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketVetoResponse implements OutputPacket
{
	private boolean accepted;
	
	public OutputPacketVetoResponse(boolean accepted)
	{
		this.accepted = accepted;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("accepted", accepted);
	}
	
	@Override
	public String getName()
	{
		return "VETORESPONSE";
	}
}
