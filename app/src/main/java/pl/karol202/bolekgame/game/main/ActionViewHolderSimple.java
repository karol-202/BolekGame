package pl.karol202.bolekgame.game.main;

import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

class ActionViewHolderSimple extends ActionViewHolder<ActionSimple>
{
	private TextView text;
	
	ActionViewHolderSimple(View view)
	{
		super(view);
		text = view.findViewById(R.id.text_action_simple);
	}
	
	@Override
	void bind(ActionSimple action)
	{
		text.setText(action.getText());
	}
}
