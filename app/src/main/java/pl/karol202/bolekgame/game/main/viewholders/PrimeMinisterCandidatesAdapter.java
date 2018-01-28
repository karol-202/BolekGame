package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePrimeMinister;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class PrimeMinisterCandidatesAdapter extends RecyclerView.Adapter<PrimeMinisterCandidatesAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private Player player;
		
		private TextView textName;
		
		private ViewHolder(View view)
		{
			super(view);
			view.setOnClickListener(v -> listener.onPrimeMinisterCandidateChoose(player));
			textName = view.findViewById(R.id.text_prime_minister_candidate_name);
		}
		
		private void bind(Player player)
		{
			if(this.player == player) update();
			else bindNewPlayer(player);
		}
		
		private void bindNewPlayer(Player player)
		{
			this.player = player;
			
			itemView.setBackgroundResource(player == choosenCandidate ? R.drawable.background_item_checked : R.drawable.background_item);
			textName.setText(player.getName());
		}
		
		private void update()
		{
			TransitionManager.beginDelayedTransition((ViewGroup) itemView);
			itemView.setBackgroundResource(player == choosenCandidate ? R.drawable.background_item_checked : R.drawable.background_item);
		}
	}
	
	private Context context;
	
	private ActionChoosePrimeMinister action;
	private ActionChoosePrimeMinister.OnPrimeMinisterCandidateChooseListener listener;
	private List<Player> candidates;
	private Player choosenCandidate;
	
	PrimeMinisterCandidatesAdapter(Context context)
	{
		this.context = context;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_prime_minister_candidate, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		Player candidate = candidates.get(position);
		holder.bind(candidate);
	}
	
	@Override
	public int getItemCount()
	{
		return candidates.size();
	}
	
	public void setAction(ActionChoosePrimeMinister action)
	{
		//if(this.action == action) updateAction();
		//else setNewAction(action);
		setNewAction(action);
	}
	
	private void setNewAction(ActionChoosePrimeMinister action)
	{
		this.action = action;
		listener = action.getOnPrimeMinisterCandidateChooseListener();
		candidates = action.getCandidates();
		choosenCandidate = action.getChoosenCandidate();
		notifyDataSetChanged();
	}
	
	private void updateAction()
	{
		updateChoosenCandidate();
	}
	
	private void updateChoosenCandidate()
	{
		if(choosenCandidate == action.getChoosenCandidate()) return;
		choosenCandidate = action.getChoosenCandidate();
		notifyItemChanged(candidates.indexOf(choosenCandidate));
	}
}
