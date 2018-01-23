package pl.karol202.bolekgame.game.main;

import android.view.View;
import java8.util.function.Function;

public interface Action
{
	int getViewHolderLayout();
	
	Class<? extends ActionViewHolder> getViewHolderClass();
	
	Function<View, ? extends ActionViewHolder> getViewHolderCreator();
}
