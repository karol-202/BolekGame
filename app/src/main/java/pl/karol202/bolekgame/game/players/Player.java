package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.server.User;

public abstract class Player<U extends User>
{
	private U user;
	private Position position;
	
	Player(U user)
	{
		this.user = user;
		position = Position.NONE;
	}
	
	U getUser()
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
