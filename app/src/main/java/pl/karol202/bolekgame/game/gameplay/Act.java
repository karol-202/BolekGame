package pl.karol202.bolekgame.game.gameplay;

public enum Act
{
	LUSTRATION(0),
	ANTILUSTRATION(0);

	private int icon;

	Act(int icon)
	{
		this.icon = icon;
	}

	public static Act getActByName(String act)
	{
		if(act.equals(LUSTRATION.name())) return LUSTRATION;
		else if(act.equals(ANTILUSTRATION.name())) return ANTILUSTRATION;
		else return null;
	}

	public int getIcon()
	{
		return icon;
	}
}