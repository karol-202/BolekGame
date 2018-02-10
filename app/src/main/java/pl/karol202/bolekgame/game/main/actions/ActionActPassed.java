package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderActPassed;

public class ActionActPassed implements Action
{
	private Act act;
	
	public ActionActPassed(Act act)
	{
		this.act = act;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_act_passed;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderActPassed.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderActPassed::new;
	}
	
	public int getActionText()
	{
		return R.string.action_act_passed;
	}

	public Act getAct()
	{
		return act;
	}
}
