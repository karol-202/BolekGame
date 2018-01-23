package pl.karol202.bolekgame.game.main;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;

public abstract class ActionSimple implements Action
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
	
	abstract String getText();
}
