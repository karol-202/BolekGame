package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.Role;

public class InputPacketRoleAssigned implements InputPacket
{
	private Role role;
	
	@Override
	public void readData(DataBundle bundle)
	{
		role = Role.getRoleByName(bundle.getString("role", ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onRoleAssigned(role);
	}
}
