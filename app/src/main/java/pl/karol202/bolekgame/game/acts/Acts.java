package pl.karol202.bolekgame.game.acts;

import pl.karol202.bolekgame.game.gameplay.Act;

public class Acts
{
	private boolean ignoreStackRefill;
	
	private int lustrationActs;
	private int antilustrationActs;
	
	private Act[] currentActs;
	
	private int pollIndex;
	
	public Acts()
	{
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
	
	public void setCurrentActs(Act[] currentActs)
	{
		this.currentActs = currentActs;
	}
	
	public boolean isIgnoringStackRefill()
	{
		return ignoreStackRefill;
	}
	
	public int getLustrationActs()
	{
		return lustrationActs;
	}
	
	public int getAntilustrationActs()
	{
		return antilustrationActs;
	}
	
	public int getPollIndex()
	{
		return pollIndex;
	}
	
	public Act[] getCurrentActs()
	{
		return currentActs;
	}
}
