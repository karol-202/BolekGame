package pl.karol202.bolekgame.game.acts;

import pl.karol202.bolekgame.game.gameplay.Act;

public class Acts
{
	private int playersAmount;
	
	private boolean ignoreStackRefill;
	
	private int lustrationActs;
	private int antilustrationActs;
	
	private int pollIndex;
	
	public Acts(int playersAmount)
	{
		this.playersAmount = playersAmount;
		ignoreStackRefill = true;
	}
	
	public void refillStack()
	{
		ignoreStackRefill = false;
	}
	
	public Act updateActs(int lustrationActs, int antilustrationActs)
	{
		Act act = null;
		if(lustrationActs == this.lustrationActs + 1) act = Act.LUSTRATION;
		else if(antilustrationActs == this.antilustrationActs + 1) act = Act.ANTILUSTRATION;
		if(act != null) passAct(act);
		return act;
	}
	
	private void passAct(Act act)
	{
		if(act == Act.LUSTRATION) lustrationActs++;
		else if(act == Act.ANTILUSTRATION) antilustrationActs++;
	}
	
	public void updatePollIndex(int pollIndex)
	{
		this.pollIndex = pollIndex;
	}
	
	public boolean isIgnoringStackRefill()
	{
		return ignoreStackRefill;
	}
	
	int getLustrationActs()
	{
		return lustrationActs;
	}
	
	int getAntilustrationActs()
	{
		return antilustrationActs;
	}
	
	int getPollIndex()
	{
		return pollIndex;
	}
	
	boolean isPlayerCheckingAvailable()
	{
		return true;//playersAmount >= 8;
	}
	
	boolean isPlayerOrActsCheckingAvailable()
	{
		return true;//playersAmount >= 6;
	}
}
