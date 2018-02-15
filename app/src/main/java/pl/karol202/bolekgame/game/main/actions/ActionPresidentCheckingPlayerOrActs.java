package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentCheckingPlayerOrActs extends SimpleAction
{
	private String text;
	
	public ActionPresidentCheckingPlayerOrActs(ContextSupplier contextSupplier, String president)
	{
		text = contextSupplier.getString(R.string.action_president_checking_player_or_acts, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
