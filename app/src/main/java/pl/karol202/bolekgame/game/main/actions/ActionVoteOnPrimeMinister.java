package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager.ActionUpdateCallback;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.VotingResult;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderVoteOnPrimeMinister;
import pl.karol202.bolekgame.game.players.Player;

public class ActionVoteOnPrimeMinister implements UpdatingAction
{
	public interface OnVoteListener
	{
		void onVote(boolean vote);
	}
	
	private ActionUpdateCallback updateCallback;
	
	private ContextSupplier contextSupplier;
	private Player primeMinister;
	private OnVoteListener voteListener;
	private boolean voted;
	private boolean vote;
	private VotingResult votingResult;
	
	public ActionVoteOnPrimeMinister(ContextSupplier contextSupplier, Player primeMinister, OnVoteListener voteListener)
	{
		this.contextSupplier = contextSupplier;
		this.primeMinister = primeMinister;
		this.voteListener = voteListener;
	}
	
	public void onVote(boolean vote)
	{
		this.voted = true;
		this.vote = vote;
		updateCallback.updateAction();
	}
	
	public void onVotingEnd(VotingResult votingResult)
	{
		this.votingResult = votingResult;
		updateCallback.updateAction();
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_vote_on_prime_minister;
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
	public void setUpdateCallback(ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
	
	public String getPrimeMinisterName()
	{
		return primeMinister.getName();
	}
	
	public OnVoteListener getOnVoteListener()
	{
		return voteListener;
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
