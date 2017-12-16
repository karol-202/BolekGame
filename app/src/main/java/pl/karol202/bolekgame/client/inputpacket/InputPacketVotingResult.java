package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

import java.util.ArrayList;
import java.util.List;

public class InputPacketVotingResult implements InputGamePacket
{
	private int upvotes;
	private int totalVotes;
	private boolean passed;
	private List<String> upvoters;
	
	public InputPacketVotingResult()
	{
		upvoters = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		upvotes = bundle.getInt("upvotes", 0);
		totalVotes = bundle.getInt("totalVotes", 0);
		passed = bundle.getInt("passed", 0) == 1;
		for(int i = 0; i < upvotes; i++) upvoters.add(bundle.getString("upvoter" + i, ""));
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onVotingResult(upvotes, totalVotes, passed, upvoters);
	}
}
