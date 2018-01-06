package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketVotingResult implements InputPacket
{
	private int upvotes;
	private int totalVotes;
	private boolean passed;
	private List<String> upvoters;
	
	InputPacketVotingResult()
	{
		upvoters = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		upvotes = bundle.getInt("upvotes", 0);
		totalVotes = bundle.getInt("totalVotes", 0);
		passed = bundle.getBoolean("passed", false);
		for(int i = 0; i < upvotes; i++) upvoters.add(bundle.getString("upvoter" + i, ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onVotingResult(upvotes, totalVotes, passed, upvoters);
	}
}
