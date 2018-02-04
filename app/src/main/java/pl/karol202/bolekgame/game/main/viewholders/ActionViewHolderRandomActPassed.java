package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.ImageView;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionRandomActPassed;

public class ActionViewHolderRandomActPassed extends ActionViewHolder<ActionRandomActPassed>
{
	private ImageView imageAct;

	public ActionViewHolderRandomActPassed(View view)
	{
		super(view);
		imageAct = view.findViewById(R.id.image_random_act);
	}

	@Override
	public void bind(ActionRandomActPassed action)
	{
		imageAct.setImageResource(action.getAct().getIcon());
	}
}
