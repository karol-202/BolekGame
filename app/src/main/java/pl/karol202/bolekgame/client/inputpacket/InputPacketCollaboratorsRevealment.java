package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

import java.util.ArrayList;
import java.util.List;

public class InputPacketCollaboratorsRevealment implements InputGamePacket
{
	private List<String> collaborators;
	private String bolek;
	
	InputPacketCollaboratorsRevealment()
	{
		collaborators = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int length = bundle.getInt("collaborators", 0);
		for(int i = 0; i < length; i++) collaborators.add(bundle.getString("collaborator" + i, ""));
		bolek = bundle.getString("bolek", "");
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onCollaboratorsRevealment(collaborators, bolek);
	}
}
