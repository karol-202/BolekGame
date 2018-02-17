package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPresidentAssigned extends SimpleAction
{
	private String text;
	
	public ActionPresidentAssigned(ContextSupplier contextSupplier, String president, boolean amIPresident, boolean extraordinary)
	{
		if(extraordinary)
		{
			if(amIPresident) text = contextSupplier.getString(R.string.action_president_chosen_president_you);
			else text = contextSupplier.getString(R.string.action_president_chosen_presiednt, president);
		}
		else
		{
			if(amIPresident) text = contextSupplier.getString(R.string.action_president_assigned_you);
			else text = contextSupplier.getString(R.string.action_president_assigned, president);
		}
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
