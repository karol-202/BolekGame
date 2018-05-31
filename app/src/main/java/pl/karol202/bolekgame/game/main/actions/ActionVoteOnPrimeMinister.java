package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager.ActionUpdateCallback;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.VotingResult;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderVoteOnPrimeMinister;

public class ActionVoteOnPrimeMinister implements UpdatingAction, CancellableAction
{
	public interface OnVoteListener
	{
		void onVote(boolean vote);
	}
	
	private ContextSupplier contextSupplier;
	private ActionUpdateCallback updateCallback;
	private OnVoteListener voteListener;
	
	private boolean cancelled;
	private String primeMinister;
	private boolean voted;
	private boolean vote;
	private VotingResult votingResult;
	
	public ActionVoteOnPrimeMinister(ContextSupplier contextSupplier, String primeMinister, OnVoteListener voteListener)
	{
		this.contextSupplier = contextSupplier;
		this.voteListener = voteListener;
		
		this.primeMinister = primeMinister;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_scenes;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderVoteOnPrimeMinister.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderVoteOnPrimeMinister(v, contextSupplier.getContext());
	}
	
	@Override
	public void cancel()
	{
		cancelled = true;
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void vote(boolean vote)
	{
		if(cancelled) return;
		this.voted = true;
		this.vote = vote;
		if(voteListener != null) voteListener.onVote(vote);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public void onVotingEnd(VotingResult votingResult, boolean update)
	{
		this.votingResult = votingResult;
		if(update && updateCallback != null) updateCallback.updateAction();
	}
	
	@Override
	public void setUpdateCallback(ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
	
	public String getPrimeMinisterName()
	{
		return primeMinister;
	}
	
	public boolean isVoted()
	{
		return voted;
	}

	public boolean getVote()
	{
		return vote;
	}
	
	public boolean isVotingEnded()
	{
		return votingResult != null;
	}
	
	public VotingResult getVotingResult()
	{
		return votingResult;
	}
}
