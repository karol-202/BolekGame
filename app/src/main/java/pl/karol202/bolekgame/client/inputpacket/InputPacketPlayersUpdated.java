package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

import java.util.ArrayList;
import java.util.List;

public class InputPacketPlayersUpdated implements InputGamePacket
{
	private List<String> players;
	
	public InputPacketPlayersUpdated()
	{
		players = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("players", 0);
		for(int i = 0; i < length; i++) players.add(bundle.getString("player" + i, ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPlayersUpdated(players);
	}
}
