package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketPrimeMinisterVote implements OutputPacket
{
	private int vote;
	
	public OutputPacketPrimeMinisterVote(int vote)
	{
		this.vote = vote;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("vote", vote);
	}
	
	@Override
	public String getName()
	{
		return "PRIMEMINISTERVOTE";
	}
}
