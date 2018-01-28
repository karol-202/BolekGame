package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketCheckPlayerPresident implements InputPacket
{
	private boolean update;
	private List<String> checkablePlayers;
	
	InputPacketCheckPlayerPresident()
	{
		checkablePlayers = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		update = bundle.getBoolean("update", false);
		int length = bundle.getInt("checkablePlayers", 0);
		for(int i = 0; i < length; i++) checkablePlayers.add(bundle.getString("checkablePlayer" + i, ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onCheckPlayerPresidentRequest(update, checkablePlayers);
	}
}
