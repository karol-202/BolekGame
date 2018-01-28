package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionRoleAssigned extends SimpleAction
{
	private String text;
	
	public ActionRoleAssigned(ContextSupplier contextSupplier, Role role)
	{
		String roleInstr = contextSupplier.getString(role.getNameInstr());
		text = contextSupplier.getString(R.string.action_role_assigned, roleInstr);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
