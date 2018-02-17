package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderLustrate;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class ActionLustrate implements UpdatingAction
{
	public interface OnLustrationListener
	{
		void onLustration(Player player);
	}
	
	private ContextSupplier contextSupplier;
	private OnLustrationListener lustrationListener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private List<Player> availablePlayers;
	private Player lustratedPlayer;
	
	public ActionLustrate(ContextSupplier contextSupplier, OnLustrationListener lustrationListener, List<Player> availablePlayers)
	{
		this.contextSupplier = contextSupplier;
		this.lustrationListener = lustrationListener;
		
		this.availablePlayers = availablePlayers;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_lustrate;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderLustrate.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderLustrate(v, contextSupplier.getContext());
	}
	
	private void lustrate(Player player)
	{
		if(!availablePlayers.contains(player) || lustratedPlayer != null) return;
		lustratedPlayer = player;
		if(lustrationListener != null) lustrationListener.onLustration(player);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public OnLustrationListener getLustrationListener()
	{
		return this::lustrate;
	}
	
	public List<Player> getAvailablePlayers()
	{
		return availablePlayers;
	}
	
	public void setAvailablePlayers(List<Player> availablePlayers)
	{
		this.availablePlayers = availablePlayers;
	}
	
	public Player getLustratedPlayer()
	{
		return lustratedPlayer;
	}
	
	@Override
	public void setUpdateCallback(ActionManager.ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
}
