package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;

public interface Action
{
	int getViewHolderLayout();
	
	Class<? extends ActionViewHolder> getViewHolderClass();
	
	Function<View, ? extends ActionViewHolder> getViewHolderCreator();
}
