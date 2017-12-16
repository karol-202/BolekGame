package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.Role;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketRoleAssigned implements InputGamePacket
{
	private Role role;
	
	@Override
	public void readData(DataBundle bundle)
	{
		role = Role.getRoleByName(bundle.getString("role", ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onRoleAssigned(role);
	}
}
