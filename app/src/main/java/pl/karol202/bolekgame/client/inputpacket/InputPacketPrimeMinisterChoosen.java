package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPrimeMinisterChoosen implements InputPacket
{
	private String primeMinister;
	
	@Override
	public void readData(DataBundle bundle)
	{
		primeMinister = bundle.getString("primeMinister", "");
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPrimeMinisterChoose(primeMinister);
	}
}
