package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.WinCause;
import pl.karol202.bolekgame.game.main.actions.ActionWin;

public class ActionViewHolderWin extends ActionViewHolder<ActionWin>
{
	private TextView textAction;
	private TextView textWinCause;
	
	public ActionViewHolderWin(View view)
	{
		super(view);
		textAction = view.findViewById(R.id.text_action_win);
		textWinCause = view.findViewById(R.id.text_action_win_cause);
	}
	
	@Override
	public void bind(ActionWin action)
	{
		textAction.setText(action.isMinistersWin() ? R.string.action_win_ministers : R.string.action_win_collaborators);
		
		int winCauseText = -1;
		if(action.isMinistersWin() && action.getWinCause() == WinCause.ACTS_PASSED) winCauseText = R.string.text_win_cause_acts_ministers;
		else if(action.isMinistersWin() && action.getWinCause() == WinCause.BOLEK) winCauseText = R.string.text_win_cause_bolek_ministers;
		else if(!action.isMinistersWin() && action.getWinCause() == WinCause.ACTS_PASSED) winCauseText = R.string.text_win_cause_acts_collaborators;
		else if(!action.isMinistersWin() && action.getWinCause() == WinCause.BOLEK) winCauseText = R.string.text_win_cause_bolek_collaborators;
		if(winCauseText != -1) textWinCause.setText(winCauseText);
	}
}
