package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentLustrating extends SimpleAction
{
	private String text;
	
	public ActionPresidentLustrating(ContextSupplier contextSupplier, String president)
	{
		text = contextSupplier.getString(R.string.action_president_lustrating, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
