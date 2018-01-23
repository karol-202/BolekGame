package pl.karol202.bolekgame.game.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class ActionViewHolder<A extends Action> extends RecyclerView.ViewHolder
{
	ActionViewHolder(View view)
	{
		super(view);
	}
	
	abstract void bind(A action);
}
