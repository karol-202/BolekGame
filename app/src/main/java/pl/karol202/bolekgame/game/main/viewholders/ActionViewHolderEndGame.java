package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.Button;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionEndGame;

public class ActionViewHolderEndGame extends ActionViewHolder<ActionEndGame>
{
	private Button buttonEndGame;
	
	public ActionViewHolderEndGame(View view)
	{
		super(view);
		buttonEndGame = view.findViewById(R.id.button_end_game);
	}
	
	@Override
	public void bind(ActionEndGame action)
	{
		buttonEndGame.setOnClickListener(v -> action.endGame());
	}
}
