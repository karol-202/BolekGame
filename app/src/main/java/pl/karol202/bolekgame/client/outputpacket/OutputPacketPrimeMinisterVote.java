package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketPrimeMinisterVote implements OutputPacket
{
	private boolean vote;
	
	public OutputPacketPrimeMinisterVote(boolean vote)
	{
		this.vote = vote;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putBoolean("vote", vote);
	}
	
	@Override
	public String getName()
	{
		return "PRIMEMINISTERVOTE";
	}
}
