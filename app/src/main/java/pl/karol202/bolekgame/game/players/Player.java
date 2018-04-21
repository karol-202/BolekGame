package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.server.User;

public abstract class Player<U extends User>
{
	private U user;
	
	Player(U user)
	{
		this.user = user;
	}
	
	U getUser()
	{
		return user;
	}
	
	public String getName()
	{
		return user.getName();
	}
}
