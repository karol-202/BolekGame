package pl.karol202.bolekgame.game.gameplay;

import pl.karol202.bolekgame.R;

public enum Role
{
	MINISTER(R.string.role_minister, R.string.role_minister_instr),
	COLLABORATOR(R.string.role_collaborator, R.string.role_collaborator_instr),
	BOLEK(R.string.role_bolek, R.string.role_bolek_instr);
	
	private int name;
	private int nameInstr;
	
	Role(int name, int nameInstr)
	{
		this.name = name;
		this.nameInstr = nameInstr;
	}
	
	public static Role getRoleByName(String name)
	{
		for(Role role : values()) if(role.name().equals(name)) return role;
		return null;
	}
	
	public int getName()
	{
		return name;
	}
	
	public int getNameInstr()
	{
		return nameInstr;
	}
}
