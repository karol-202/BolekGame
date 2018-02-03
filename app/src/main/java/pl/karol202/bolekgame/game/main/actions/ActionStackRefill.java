package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionStackRefill extends SimpleAction
{
	private String text;
	
	public ActionStackRefill(ContextSupplier contextSupplier)
	{
		text = contextSupplier.getString(R.string.action_stack_refilled);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
