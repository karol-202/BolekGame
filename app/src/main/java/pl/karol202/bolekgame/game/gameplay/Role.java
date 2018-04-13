package pl.karol202.bolekgame.game.gameplay;

import pl.karol202.bolekgame.R;

public enum Role
{
	MINISTER(R.string.role_minister, R.string.role_minister_instr, R.drawable.role_minister),
	COLLABORATOR(R.string.role_collaborator, R.string.role_collaborator_instr, R.drawable.role_collaborator),
	BOLEK(R.string.role_bolek, R.string.role_bolek_instr, R.drawable.role_bolek);
	
	private int name;
	private int nameInstr;
	private int image;
	
	Role(int name, int nameInstr, int image)
	{
		this.name = name;
		this.nameInstr = nameInstr;
		this.image = image;
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
	
	public int getImage()
	{
		return image;
	}
}
