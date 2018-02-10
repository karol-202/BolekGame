package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionActPassed;

public class ActionViewHolderActPassed extends ActionViewHolder<ActionActPassed>
{
	private TextView textActPassed;
	private ImageView imageAct;
	private TextView textAct;

	public ActionViewHolderActPassed(View view)
	{
		super(view);
		textActPassed = view.findViewById(R.id.text_action_act_passed);
		imageAct = view.findViewById(R.id.image_action_act_passed_act);
		textAct = view.findViewById(R.id.text_action_act_passed_act);
	}

	@Override
	public void bind(ActionActPassed action)
	{
		textActPassed.setText(action.getActionText());
		imageAct.setImageResource(action.getAct().getIcon());
		textAct.setText(action.getAct().getName());
	}
}
