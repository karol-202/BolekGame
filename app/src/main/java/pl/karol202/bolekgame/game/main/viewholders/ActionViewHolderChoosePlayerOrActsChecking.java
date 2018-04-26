package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePlayerOrActsChecking;

public class ActionViewHolderChoosePlayerOrActsChecking extends ActionViewHolder<ActionChoosePlayerOrActsChecking>
{
	private TextView textChoose;
	private Button buttonPlayer;
	private Button buttonActs;
	
	public ActionViewHolderChoosePlayerOrActsChecking(View view)
	{
		super(view);
		textChoose = view.findViewById(R.id.text_choose_player_or_acts_checking);
		buttonPlayer = view.findViewById(R.id.button_choose_player_checking);
		buttonActs = view.findViewById(R.id.button_choose_acts_checking);
	}
	
	@Override
	public void bind(ActionChoosePlayerOrActsChecking action)
	{
		textChoose.setText(action.isPlayersCheckingAvailable() ? R.string.action_choose_player_or_acts_checking : R.string.action_choose_acts_checking);
		
		buttonPlayer.setVisibility(action.isPlayersCheckingAvailable() ? View.VISIBLE : View.GONE);
		buttonPlayer.setEnabled(!action.isChosen() && !action.isCancelled());
		buttonPlayer.setOnClickListener(v -> action.choosePlayerChecking());
		
		buttonActs.setEnabled(!action.isChosen() && !action.isCancelled());
		buttonActs.setOnClickListener(v -> action.chooseActsChecking());
	}
}
