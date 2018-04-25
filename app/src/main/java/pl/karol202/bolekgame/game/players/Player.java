package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.server.User;

public abstract class Player<U extends User>
{
	private U user;
	private Role role;
	
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
	
	public Role getRole()
	{
		return role;
	}
	
	void setRole(Role role)
	{
		this.role = role;
	}
}
