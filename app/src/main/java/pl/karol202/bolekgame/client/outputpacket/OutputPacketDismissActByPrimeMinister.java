package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketDismissActByPrimeMinister implements OutputPacket
{
	private String act;
	
	public OutputPacketDismissActByPrimeMinister(String act)
	{
		this.act = act;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("act", act);
	}
	
	@Override
	public String getName()
	{
		return "DISMISSACTBYPRIMEMINISTER";
	}
}
