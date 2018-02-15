package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionActsCheckingResult;

public class ActionViewHolderActsCheckingResult extends ActionViewHolder<ActionActsCheckingResult>
{
	private ImageView imageAct1;
	private ImageView imageAct2;
	private ImageView imageAct3;
	private TextView textAct1;
	private TextView textAct2;
	private TextView textAct3;
	
	public ActionViewHolderActsCheckingResult(View view)
	{
		super(view);
		imageAct1 = view.findViewById(R.id.image_action_acts_checking_result_act_1);
		imageAct2 = view.findViewById(R.id.image_action_acts_checking_result_act_2);
		imageAct3 = view.findViewById(R.id.image_action_acts_checking_result_act_3);
		textAct1 = view.findViewById(R.id.text_action_acts_checking_result_1);
		textAct2 = view.findViewById(R.id.text_action_acts_checking_result_2);
		textAct3 = view.findViewById(R.id.text_action_acts_checking_result_3);
	}
	
	@Override
	public void bind(ActionActsCheckingResult action)
	{
		imageAct1.setImageResource(action.getAct(0).getIcon());
		imageAct2.setImageResource(action.getAct(1).getIcon());
		imageAct3.setImageResource(action.getAct(2).getIcon());
		textAct1.setText(action.getAct(0).getName());
		textAct2.setText(action.getAct(1).getName());
		textAct3.setText(action.getAct(2).getName());
	}
}
