package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

import java.util.ArrayList;
import java.util.List;

public class InputPacketChoosePrimeMinister implements InputGamePacket
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
	public void execute(GameLogic ui)
	{
		ui.onChoosePrimeMinisterRequest(candidates);
	}
}
