package pl.karol202.bolekgame.game.main.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.bolekgame.game.main.actions.Action;

public abstract class ActionViewHolder<A extends Action> extends RecyclerView.ViewHolder
{
	ActionViewHolder(View view)
	{
		super(view);
	}
	
	public abstract void bind(A action);
}
