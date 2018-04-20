package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderChoosePlayerOrActsChecking;

public class ActionChoosePlayerOrActsChecking implements UpdatingAction, CancellableAction
{
	public enum Choose
	{
		PLAYER_CHECKING, ACTS_CHECKING
	}
	
	public interface OnPlayerOrActsCheckingChoose
	{
		void onChoose(Choose choose);
	}
	
	private OnPlayerOrActsCheckingChoose chooseListener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private boolean cancelled;
	private boolean chosen;
	
	public ActionChoosePlayerOrActsChecking(OnPlayerOrActsCheckingChoose chooseListener)
	{
		this.chooseListener = chooseListener;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_choose_player_or_acts_checking;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderChoosePlayerOrActsChecking.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderChoosePlayerOrActsChecking::new;
	}
	
	@Override
	public void cancel()
	{
		cancelled = true;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void choosePlayerChecking()
	{
		if(cancelled) return;
		chosen = true;
		if(chooseListener != null) chooseListener.onChoose(Choose.PLAYER_CHECKING);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public void chooseActsChecking()
	{
		if(cancelled) return;
		chosen = true;
		if(chooseListener != null) chooseListener.onChoose(Choose.ACTS_CHECKING);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public boolean isChosen()
	{
		return chosen;
	}
	
	@Override
	public void setUpdateCallback(ActionManager.ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
}
