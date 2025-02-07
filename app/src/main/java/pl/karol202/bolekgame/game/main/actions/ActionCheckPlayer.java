package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderCheckPlayer;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class ActionCheckPlayer implements UpdatingAction, CancellableAction
{
	public interface OnPlayerCheckListener
	{
		void onPlayerCheck(Player player);
	}
	
	private ContextSupplier contextSupplier;
	private OnPlayerCheckListener playerCheckListener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private boolean cancelled;
	private boolean description;
	private List<Player> playersToCheck;
	private Player checkedPlayer;
	private boolean hasResult;
	private boolean result;
	
	public ActionCheckPlayer(ContextSupplier contextSupplier, OnPlayerCheckListener playerCheckListener, boolean description, List<Player> playersToCheck)
	{
		this.contextSupplier = contextSupplier;
		this.playerCheckListener = playerCheckListener;
		
		this.description = description;
		this.playersToCheck = playersToCheck;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_scenes;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderCheckPlayer.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderCheckPlayer(v, contextSupplier.getContext());
	}
	
	@Override
	public void cancel()
	{
		 cancelled = true;
	}
	
	@Override
	public void setUpdateCallback(ActionManager.ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
	
	public void checkPlayer(Player player)
	{
		if(!playersToCheck.contains(player) || checkedPlayer != null || cancelled) return;
		checkedPlayer = player;
		if(playerCheckListener != null) playerCheckListener.onPlayerCheck(player);
	}
	
	public void onCheckingResult(boolean result)
	{
		hasResult = true;
		this.result = result;
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public boolean hasDescription()
	{
		return description;
	}
	
	public List<Player> getPlayersToCheck()
	{
		return playersToCheck;
	}
	
	public void setPlayersToCheck(List<Player> playersToCheck)
	{
		this.playersToCheck = playersToCheck;
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public String getCheckedPlayerName()
	{
		return checkedPlayer.getName();
	}
	
	public boolean hasResult()
	{
		return hasResult;
	}
	
	public boolean getResult()
	{
		return result;
	}
}
