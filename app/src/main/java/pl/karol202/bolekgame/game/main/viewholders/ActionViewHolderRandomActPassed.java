package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionRandomActPassed;

public class ActionViewHolderRandomActPassed extends ActionViewHolder<ActionRandomActPassed>
{
	private ImageView imageAct;
	private TextView textAct;

	public ActionViewHolderRandomActPassed(View view)
	{
		super(view);
		imageAct = view.findViewById(R.id.image_random_act);
		textAct = view.findViewById(R.id.text_action_random_act_passed_act);
	}

	@Override
	public void bind(ActionRandomActPassed action)
	{
		imageAct.setImageResource(action.getAct().getIcon());
		textAct.setText(action.getAct().getName());
	}
}
