package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionLustrationResult;

public class ActionViewHolderLustrationResult extends ActionViewHolder<ActionLustrationResult>
{
	private TextView textAction;
	private ImageView imageBolek;
	private TextView textResult;
	
	private Context context;
	
	public ActionViewHolderLustrationResult(View view, Context context)
	{
		super(view);
		this.context = context;
		
		textAction = view.findViewById(R.id.text_action_lustration_result);
		imageBolek = view.findViewById(R.id.image_action_lustration_result);
		textResult = view.findViewById(R.id.text_action_lustration_result_result);
	}
	
	@Override
	public void bind(ActionLustrationResult action)
	{
		if(action.amIPresident())
			textAction.setText(context.getString(R.string.action_lustration_result_you, action.getLustratedPlayerName()));
		else textAction.setText(context.getString(R.string.action_lustration_result, action.getLustratedPlayerName(), action.getPresidentName()));
		
		if(action.wasPlayerBolek()) imageBolek.setImageBitmap(action.getBolekImage());
		else imageBolek.setVisibility(View.GONE);
		
		int stringId = action.wasPlayerBolek() ? R.string.text_lustration_result_bolek : R.string.text_lustration_result_not_bolek;
		textResult.setText(context.getString(stringId, action.getLustratedPlayerName()));
	}
}
