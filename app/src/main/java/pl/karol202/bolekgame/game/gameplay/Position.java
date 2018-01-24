package pl.karol202.bolekgame.game.gameplay;

import pl.karol202.bolekgame.R;

public enum Position
{
	NONE(R.string.position_none),
	PRESIDENT(R.string.position_president),
	PRIME_MINISTER(R.string.position_prime_minister);
	
	private int name;
	
	Position(int name)
	{
		this.name = name;
	}
	
	public int getName()
	{
		return name;
	}
}
