package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketLustratePresident implements InputPacket
{
	private boolean update;
	private List<String> availablePlayers;
	
	InputPacketLustratePresident()
	{
		availablePlayers = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		update = bundle.getBoolean("update", false);
		int length = bundle.getInt("availablePlayers", 0);
		for(int i = 0; i < length; i++) availablePlayers.add(bundle.getString("availablePlayer" + i, ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onLustratePresidentRequest(update, availablePlayers);
	}
}
