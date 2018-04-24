package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.Button;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePlayerOrActsChecking;

public class ActionViewHolderChoosePlayerOrActsChecking extends ActionViewHolder<ActionChoosePlayerOrActsChecking>
{
	private Button buttonPlayer;
	private Button buttonActs;
	
	public ActionViewHolderChoosePlayerOrActsChecking(View view)
	{
		super(view);
		buttonPlayer = view.findViewById(R.id.button_choose_player_checking);
		buttonActs = view.findViewById(R.id.button_choose_acts_checking);
	}
	
	@Override
	public void bind(ActionChoosePlayerOrActsChecking action)
	{
		buttonPlayer.setVisibility(action.isPlayersCheckingAvailable() ? View.VISIBLE : View.GONE);
		buttonPlayer.setEnabled(!action.isChosen() && !action.isCancelled());
		buttonPlayer.setOnClickListener(v -> action.choosePlayerChecking());
		
		buttonActs.setEnabled(!action.isChosen() && !action.isCancelled());
		buttonActs.setOnClickListener(v -> action.chooseActsChecking());
	}
}
