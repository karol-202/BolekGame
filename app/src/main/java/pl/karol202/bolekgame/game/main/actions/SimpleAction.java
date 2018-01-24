package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderSimple;

public abstract class SimpleAction implements Action
{
	@Override
	public final int getViewHolderLayout()
	{
		return R.layout.item_action_simple;
	}
	
	@Override
	public final Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderSimple.class;
	}
	
	@Override
	public final Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderSimple::new;
	}
	
	public abstract String getText();
}
