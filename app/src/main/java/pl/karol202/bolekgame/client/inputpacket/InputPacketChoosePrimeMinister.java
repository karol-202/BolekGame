package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketChoosePrimeMinister implements InputPacket
{
	private List<String> candidates;
	
	InputPacketChoosePrimeMinister()
	{
		candidates = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("candidates", 0);
		for(int i = 0; i < length; i++) candidates.add(bundle.getString("candidate" + i, ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onChoosePrimeMinisterRequest(candidates);
	}
}
