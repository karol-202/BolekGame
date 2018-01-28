package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.game.gameplay.Position;

public abstract class Player
{
	private String name;
	private Position position;
	
	Player(String name)
	{
		this.name = name;
		position = Position.NONE;
	}
	
	public String getName()
	{
		return name;
	}
	
	Position getPosition()
	{
		return position;
	}
	
	void setPosition(Position position)
	{
		this.position = position;
	}
}
