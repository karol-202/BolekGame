package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.server.ServerLogic;

import java.util.ArrayList;
import java.util.List;

public class InputPacketGameStart implements InputServerPacket
{
	private List<String> players;
	
	InputPacketGameStart()
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
	public void execute(ServerLogic ui)
	{
		ui.onGameStart(players);
	}
}
