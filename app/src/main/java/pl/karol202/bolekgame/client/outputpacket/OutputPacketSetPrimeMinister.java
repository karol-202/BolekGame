package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketSetPrimeMinister implements OutputPacket
{
	private String primeMinister;
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("primeMinister", primeMinister);
	}
	
	@Override
	public String getName()
	{
		return "SETPRIMEMINISTER";
	}
}
