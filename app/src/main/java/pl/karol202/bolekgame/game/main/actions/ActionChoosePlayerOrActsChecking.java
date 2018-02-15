package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderChoosePlayerOrActsChecking;

public class ActionChoosePlayerOrActsChecking implements UpdatingAction
{
	public enum Choose
	{
		PLAYER_CHECKING, ACTS_CHECKING
	}
	
	public interface OnPlayerOrActsCheckingChoose
	{
		void onChoose(Choose choose);
	}
	
	private ContextSupplier contextSupplier;
	private OnPlayerOrActsCheckingChoose chooseListener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private boolean chosen;
	
	public ActionChoosePlayerOrActsChecking(ContextSupplier contextSupplier, OnPlayerOrActsCheckingChoose chooseListener)
	{
		this.contextSupplier = contextSupplier;
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
	
	public void choosePlayerChecking()
	{
		chooseListener.onChoose(Choose.PLAYER_CHECKING);
		chosen = true;
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public void chooseActsChecking()
	{
		chooseListener.onChoose(Choose.ACTS_CHECKING);
		chosen = true;
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
