package pl.karol202.bolekgame.game;

public enum Role
{
	MINISTER, COLLABORATOR, BOLEK;
	
	public static Role getRoleByName(String name)
	{
		for(Role role : values()) if(role.name().equals(name)) return role;
		return null;
	}
}
