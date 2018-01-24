package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.SimpleAction;

public class ActionViewHolderSimple extends ActionViewHolder<SimpleAction>
{
	private TextView text;
	
	public ActionViewHolderSimple(View view)
	{
		super(view);
		text = view.findViewById(R.id.text_action_simple);
	}
	
	@Override
	public void bind(SimpleAction action)
	{
		text.setText(action.getText());
	}
}
