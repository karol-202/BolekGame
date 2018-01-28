package pl.karol202.bolekgame.game.main;

import pl.karol202.bolekgame.game.players.Player;

import java.util.Map;

public class VotingResult
{
	private boolean passed;
	private int upvotes;
	private int totalVotes;
	private Map<Player, Boolean> voters;
	
	public VotingResult(boolean passed, int upvotes, int totalVotes, Map<Player, Boolean> voters)
	{
		this.passed = passed;
		this.upvotes = upvotes;
		this.totalVotes = totalVotes;
		this.voters = voters;
	}
	
	public boolean isPassed()
	{
		return passed;
	}
	
	public int getUpvotes()
	{
		return upvotes;
	}
	
	public int getTotalVotes()
	{
		return totalVotes;
	}
	
	public Map<Player, Boolean> getVoters()
	{
		return voters;
	}
}
