package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;
import pl.karol202.bolekgame.ui.server.ServerUI;

import java.util.ArrayList;
import java.util.List;

public class InputPacketUsersUpdate implements InputServerPacket, InputGamePacket
{
	private List<String> users;
	
	public InputPacketUsersUpdate()
	{
		users = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("length", 0);
		for(int i = 0; i < length; i++) users.add(bundle.getString("user" + i, ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onUsersUpdate(users);
	}
	
	@Override
	public void execute(ServerUI ui)
	{
		ui.onUsersUpdate(users);
	}
}
