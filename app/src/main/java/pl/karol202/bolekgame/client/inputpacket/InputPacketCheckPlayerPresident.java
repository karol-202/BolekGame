package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

import java.util.ArrayList;
import java.util.List;

public class InputPacketCheckPlayerPresident implements InputGamePacket
{
	private List<String> checkablePlayers;
	
	public InputPacketCheckPlayerPresident()
	{
		checkablePlayers = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("checkablePlayers", 0);
		for(int i = 0; i < length; i++) checkablePlayers.add(bundle.getString("checkablePlayer" + i, ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onCheckPlayerPresidentRequest(checkablePlayers);
	}
}
