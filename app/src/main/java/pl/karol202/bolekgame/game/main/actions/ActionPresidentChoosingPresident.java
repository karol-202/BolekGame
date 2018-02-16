package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentChoosingPresident extends SimpleAction
{
	private String text;
	
	public ActionPresidentChoosingPresident(ContextSupplier contextSupplier, String president)
	{
		text = contextSupplier.getString(R.string.action_president_choosing_president, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
