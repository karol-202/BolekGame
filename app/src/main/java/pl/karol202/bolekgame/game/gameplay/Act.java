package pl.karol202.bolekgame.game.gameplay;

import pl.karol202.bolekgame.R;

public enum Act
{
	LUSTRATION(R.string.act_lustration, R.drawable.act_lustration),
	ANTILUSTRATION(R.string.act_antilustration, R.drawable.act_antilustration);

	private int name;
	private int icon;
	
	Act(int name, int icon)
	{
		this.name = name;
		this.icon = icon;
	}
	
	public static Act getActByName(String act)
	{
		if(act.equals(LUSTRATION.name())) return LUSTRATION;
		else if(act.equals(ANTILUSTRATION.name())) return ANTILUSTRATION;
		else return null;
	}
	
	public int getName()
	{
		return name;
	}
	
	public int getIcon()
	{
		return icon;
	}
}