package pl.karol202.bolekgame.game;

public abstract class Player
{
	private String name;
	
	Player(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
