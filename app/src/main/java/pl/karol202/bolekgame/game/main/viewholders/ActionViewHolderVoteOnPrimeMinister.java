package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.VotingResult;
import pl.karol202.bolekgame.game.main.actions.ActionVoteOnPrimeMinister;
import pl.karol202.bolekgame.view.ItemDivider;
import pl.karol202.bolekgame.view.VotesBar;

public class ActionViewHolderVoteOnPrimeMinister extends ActionViewHolder<ActionVoteOnPrimeMinister>
{
	private abstract class State
	{
		View sceneView;
		Scene scene;
		
		State(LayoutInflater inflater)
		{
			sceneView = inflater.inflate(getSceneLayout(), scenesRoot, false);
			scene = new Scene(scenesRoot, sceneView);
		}
		
		abstract void bind(ActionVoteOnPrimeMinister action);
		
		abstract int getSceneLayout();
		
		Scene getScene()
		{
			return scene;
		}
	}
	
	private class StateVoting extends State
	{
		private TextView textVoting;
		private Button buttonVoteYes;
		private Button buttonVoteNo;
		private TextView textYourVote;
		
		StateVoting(LayoutInflater layoutInflater)
		{
			super(layoutInflater);
			
			textVoting = sceneView.findViewById(R.id.text_vote_on_prime_minister);
			buttonVoteYes = sceneView.findViewById(R.id.button_vote_on_prime_minister_yes);
			buttonVoteNo = sceneView.findViewById(R.id.button_vote_on_prime_minister_no);
			textYourVote = sceneView.findViewById(R.id.text_vote_on_prime_minister_your_vote);
		}
		
		@Override
		void bind(ActionVoteOnPrimeMinister action)
		{
			textVoting.setText(context.getString(R.string.action_vote_on_prime_minister, action.getPrimeMinisterName()));
			buttonVoteYes.setOnClickListener(v -> action.vote(true));
			buttonVoteYes.setEnabled(!action.isVoted() && !action.isCancelled());
			buttonVoteNo.setOnClickListener(v -> action.vote(false));
			buttonVoteNo.setEnabled(!action.isVoted() && !action.isCancelled());
			textYourVote.setVisibility(action.isVoted() ? View.VISIBLE : View.GONE);
			textYourVote.setText(action.getVote() ? R.string.text_your_vote_yes : R.string.text_your_vote_no);
		}
		
		@Override
		int getSceneLayout()
		{
			return R.layout.scene_action_vote_on_prime_minister_voting;
		}
	}
	
	private class StateResult extends State
	{
		private TextView textVoting;
		private VotesBar votesBar;
		private RecyclerView recyclerAllVotes;
		
		private AllVotesAdapter adapter;
		
		StateResult(LayoutInflater inflater)
		{
			super(inflater);
			
			textVoting = sceneView.findViewById(R.id.text_vote_on_prime_minister);
			
			votesBar = sceneView.findViewById(R.id.votes_bar_vote_on_prime_minister);
			
			recyclerAllVotes = sceneView.findViewById(R.id.recycler_vote_on_prime_minister_all_votes);
			recyclerAllVotes.setLayoutManager(new LinearLayoutManager(context));
			recyclerAllVotes.addItemDecoration(new ItemDivider(context));
		}
		
		@Override
		void bind(ActionVoteOnPrimeMinister action)
		{
			VotingResult votingResult = action.getVotingResult();
			textVoting.setText(context.getString(R.string.action_vote_on_prime_minister, action.getPrimeMinisterName()));
			votesBar.setAllVotes(votingResult.getTotalVotes());
			votesBar.setUpvotes(votingResult.getUpvotes());
			
			adapter = new AllVotesAdapter(context, votingResult.getVoters());
			recyclerAllVotes.setAdapter(adapter);
		}
		
		@Override
		int getSceneLayout()
		{
			return R.layout.scene_action_vote_on_prime_minister_result;
		}
	}
	
	private ViewGroup scenesRoot;
	
	private Context context;
	private State stateVoting;
	private State stateResult;
	
	private ActionVoteOnPrimeMinister action;
	private State currentState;
	
	public ActionViewHolderVoteOnPrimeMinister(View view, Context context)
	{
		super(view);
		scenesRoot = view.findViewById(R.id.scenes_layout);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		this.context = context;
		stateVoting = new StateVoting(inflater);
		stateResult = new StateResult(inflater);
	}
	
	@Override
	public void bind(ActionVoteOnPrimeMinister action)
	{
		if(this.action == action) update();
		else bindNewAction(action);
	}
	
	private void update()
	{
		currentState = action.isVotingEnded() ? stateResult : stateVoting;
		Scene currentScene = currentState.getScene();
		currentState.bind(action);
		TransitionManager.go(currentScene);
	}
	
	private void bindNewAction(ActionVoteOnPrimeMinister action)
	{
		this.action = action;
		
		currentState = action.isVotingEnded() ? stateResult : stateVoting;
		Scene currentScene = currentState.getScene();
		currentState.bind(action);
		currentScene.enter();
	}
}
