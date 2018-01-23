package pl.karol202.bolekgame.game.gameplay;

public enum Act
{
	LUSTRATION, ANTILUSTRATION;
	
	public static Act getActByName(String act)
	{
		if(act.equals(LUSTRATION.name())) return LUSTRATION;
		else if(act.equals(ANTILUSTRATION.name())) return ANTILUSTRATION;
		else return null;
	}
}