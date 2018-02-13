package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentCheckingPlayer extends SimpleAction
{
	private String text;
	
	public ActionPresidentCheckingPlayer(ContextSupplier contextSupplier, String president)
	{
		text = contextSupplier.getString(R.string.action_president_checking_player, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
