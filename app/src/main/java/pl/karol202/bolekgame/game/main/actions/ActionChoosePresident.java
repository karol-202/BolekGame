package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderChoosePresident;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class ActionChoosePresident implements UpdatingAction, CancellableAction
{
	public interface OnPresidentChooseListener
	{
		void onPresidentChoose(Player president);
	}
	
	private ContextSupplier contextSupplier;
	private OnPresidentChooseListener listener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private boolean cancelled;
	private List<Player> candidates;
	private Player choosenCandidate;
	
	public ActionChoosePresident(ContextSupplier contextSupplier, OnPresidentChooseListener listener, List<Player> candidates)
	{
		this.contextSupplier = contextSupplier;
		this.listener = listener;
		
		this.candidates = candidates;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_choose_president;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderChoosePresident.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderChoosePresident(v, contextSupplier.getContext());
	}
	
	@Override
	public void cancel()
	{
		cancelled = true;
	}
	
	private void choosePresident(Player player)
	{
		if(!candidates.contains(player) || choosenCandidate != null || cancelled) return;
		choosenCandidate = player;
		if(listener != null) listener.onPresidentChoose(player);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public OnPresidentChooseListener getOnPresidentChooseListener()
	{
		return this::choosePresident;
	}
	
	public List<Player> getCandidates()
	{
		return candidates;
	}
	
	public void setCandidates(List<Player> candidates)
	{
		this.candidates = candidates;
	}
	
	public Player getChoosenCandidate()
	{
		return choosenCandidate;
	}
	
	@Override
	public void setUpdateCallback(ActionManager.ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
}
