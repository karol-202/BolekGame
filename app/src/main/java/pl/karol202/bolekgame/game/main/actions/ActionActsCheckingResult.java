package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderActsCheckingResult;

public class ActionActsCheckingResult implements Action
{
	private Act[] acts;
	
	public ActionActsCheckingResult(Act[] acts)
	{
		this.acts = acts;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_acts_checking_result;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderActsCheckingResult.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderActsCheckingResult::new;
	}
	
	public Act getAct(int index)
	{
		return acts[index];
	}
}
