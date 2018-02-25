package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.server.User;

public abstract class Player
{
	private User user;
	private Position position;
	
	Player(User user)
	{
		this.user = user;
		position = Position.NONE;
	}
	
	User getUser()
	{
		return user;
	}
	
	public String getName()
	{
		return user.getName();
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
