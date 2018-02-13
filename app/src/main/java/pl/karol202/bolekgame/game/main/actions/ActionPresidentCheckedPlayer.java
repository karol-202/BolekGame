package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentCheckedPlayer extends SimpleAction
{
	private String text;
	
	public ActionPresidentCheckedPlayer(ContextSupplier contextSupplier, String president, String chechedPlayer, boolean amICheckedPlayer)
	{
		if(amICheckedPlayer) text = contextSupplier.getString(R.string.action_president_checked_player_you, president);
		else text = contextSupplier.getString(R.string.action_president_checked_player, president, chechedPlayer);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
