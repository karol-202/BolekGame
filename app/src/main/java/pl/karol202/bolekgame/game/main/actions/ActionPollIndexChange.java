package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderPollIndexChange;

public class ActionPollIndexChange implements Action
{
	private int pollIndex;
	
	public ActionPollIndexChange(int pollIndex)
	{
		this.pollIndex = pollIndex;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_poll_index_change;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderPollIndexChange.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderPollIndexChange::new;
	}
	
	public int getPollIndex()
	{
		return pollIndex;
	}
	
	public boolean shouldDisplayHint()
	{
		return pollIndex == 2;
	}
}
