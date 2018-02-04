package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderRandomActPassed;

public class ActionRandomActPassed implements Action
{
	private Act act;
	
	public ActionRandomActPassed(Act act)
	{
		this.act = act;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_random_act_passed;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderRandomActPassed.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderRandomActPassed::new;
	}

	public Act getAct()
	{
		return act;
	}
}
