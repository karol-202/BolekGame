package pl.karol202.bolekgame.game.gameplay;

import pl.karol202.bolekgame.R;

public enum Role
{
	MINISTER(R.string.role_minister, R.string.role_minister_instr, R.string.role_minister_abbr, R.drawable.role_minister),
	COLLABORATOR(R.string.role_collaborator, R.string.role_collaborator_instr, R.string.role_collaborator_abbr, R.drawable.role_collaborator),
	BOLEK(R.string.role_bolek, R.string.role_bolek_instr, R.string.role_bolek_abbr, R.drawable.role_bolek);
	
	private int name;
	private int nameInstr;
	private int abbr;
	private int image;
	
	Role(int name, int nameInstr, int abbr, int image)
	{
		this.name = name;
		this.nameInstr = nameInstr;
		this.abbr = abbr;
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
	
	public int getAbbr()
	{
		return abbr;
	}
	
	public int getImage()
	{
		return image;
	}
}
