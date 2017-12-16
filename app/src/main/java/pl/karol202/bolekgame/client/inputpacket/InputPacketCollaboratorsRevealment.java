package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

import java.util.ArrayList;
import java.util.List;

public class InputPacketCollaboratorsRevealment implements InputGamePacket
{
	private List<String> collaborators;
	
	public InputPacketCollaboratorsRevealment()
	{
		collaborators = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("collaborators", 0);
		for(int i = 0; i < length; i++) collaborators.add(bundle.getString("collaborator" + i, ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onCollaboratorsRevealment(collaborators);
	}
}
