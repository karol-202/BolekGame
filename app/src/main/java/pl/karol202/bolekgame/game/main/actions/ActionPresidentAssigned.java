package pl.karol202.bolekgame.game.main.actions;

import android.content.Context;
import pl.karol202.bolekgame.R;

public class ActionPresidentAssigned extends SimpleAction
{
	private String text;
	
	public ActionPresidentAssigned(Context context)
	{
		text = context.getString(R.string.action_president_assigned_you);
	}
	
	public ActionPresidentAssigned(Context context, String president)
	{
		text = context.getString(R.string.action_president_assigned, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
