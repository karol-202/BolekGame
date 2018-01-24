package pl.karol202.bolekgame.game.main.actions;

import android.content.Context;
import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager.ActionUpdateCallback;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderChoosePrimeMinister;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class ActionChoosePrimeMinister implements UpdatingAction
{
	public interface OnPrimeMinisterCandidateChooseListener
	{
		void onPrimeMinisterCandidateChoose(Player player);
	}
	
	private Context context;
	private OnPrimeMinisterCandidateChooseListener listener;
	private ActionUpdateCallback updateCallback;
	
	private List<Player> candidates;
	private Player choosenCandidate;
	
	public ActionChoosePrimeMinister(Context context, OnPrimeMinisterCandidateChooseListener listener, List<Player> candidates)
	{
		this.context = context;
		this.listener = listener;
		this.candidates = candidates;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_choose_prime_minister;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderChoosePrimeMinister.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderChoosePrimeMinister(v, context);
	}
	
	@Override
	public void setUpdateCallback(ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
	
	private void chooseCandidate(Player player)
	{
		if(!candidates.contains(player) || choosenCandidate != null) return;
		choosenCandidate = player;
		if(updateCallback != null) updateCallback.updateAction();
		if(listener != null) listener.onPrimeMinisterCandidateChoose(player);
	}
	
	public List<Player> getCandidates()
	{
		return candidates;
	}
	
	public Player getChoosenCandidate()
	{
		return choosenCandidate;
	}
	
	public OnPrimeMinisterCandidateChooseListener getOnPrimeMinisterCandidateChooseListener()
	{
		return this::chooseCandidate;
	}
}
