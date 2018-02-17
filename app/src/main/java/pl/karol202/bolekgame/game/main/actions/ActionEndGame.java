package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderEndGame;

public class ActionEndGame implements Action
{
	public interface OnGameEndListener
	{
		void onGameEnd();
	}
	
	private OnGameEndListener listener;
	
	public ActionEndGame(OnGameEndListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_end_game;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderEndGame.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderEndGame::new;
	}
	
	public void endGame()
	{
		if(listener != null) listener.onGameEnd();
	}
}
