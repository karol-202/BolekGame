package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketUsersUpdate implements InputPacket
{
	private List<String> users;
	
	InputPacketUsersUpdate()
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
	public void execute(ClientListener listener)
	{
		listener.onUsersUpdate(users);
	}
}
