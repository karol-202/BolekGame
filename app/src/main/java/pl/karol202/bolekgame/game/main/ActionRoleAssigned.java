package pl.karol202.bolekgame.game.main;

import android.content.Context;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;

public class ActionRoleAssigned extends ActionSimple
{
	private String text;
	
	public ActionRoleAssigned(Context context, Role role)
	{
		text = context.getString(R.string.action_role_assigned, context.getString(role.getNameInstr()));
	}
	
	@Override
	String getText()
	{
		return text;
	}
}
