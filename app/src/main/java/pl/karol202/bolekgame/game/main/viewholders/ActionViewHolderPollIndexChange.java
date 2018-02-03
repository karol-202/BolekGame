package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionPollIndexChange;

public class ActionViewHolderPollIndexChange extends ActionViewHolder<ActionPollIndexChange>
{
	private TextView textPollIndex;
	private TextView textPollIndexHint;
	
	public ActionViewHolderPollIndexChange(View view)
	{
		super(view);
		textPollIndex = view.findViewById(R.id.text_action_poll_index_change_value);
		textPollIndexHint = view.findViewById(R.id.text_action_poll_index_change_hint);
	}
	
	@Override
	public void bind(ActionPollIndexChange action)
	{
		textPollIndex.setText(String.valueOf(action.getPollIndex()));
		textPollIndexHint.setVisibility(action.shouldDisplayHint() ? View.VISIBLE : View.GONE);
	}
}
