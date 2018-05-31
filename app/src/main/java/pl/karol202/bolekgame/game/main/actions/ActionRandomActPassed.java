package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;

public class ActionRandomActPassed extends ActionActPassed
{
	public ActionRandomActPassed(Act act, int actIndex)
	{
		super(act, actIndex);
	}
	
	@Override
	public int getActionText()
	{
		return R.string.action_random_act_passed;
	}
}
