package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPlayerLeaved extends SimpleAction
{
	private String text;
	
	public ActionPlayerLeaved(ContextSupplier contextSupplier, String player)
	{
		text = contextSupplier.getString(R.string.action_player_leaved, player);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
