package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionLustrate;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

public class LustrationCandidatesAdapter extends RecyclerView.Adapter<LustrationCandidatesAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		
		private Player player;
		
		ViewHolder(View view)
		{
			super(view);
			view.setOnClickListener(v -> listener.onLustration(player));
			
			textName = view.findViewById(R.id.text_lustration_candidate_name);
		}
		
		private void bind(Player player)
		{
			if(this.player == player) update();
			else bindNewPlayer(player);
		}
		
		private void bindNewPlayer(Player player)
		{
			this.player = player;
			
			itemView.setBackgroundResource(player == choosenPlayer ? R.drawable.background_item_checked : R.drawable.background_item);
			textName.setText(player.getName());
		}
		
		private void update()
		{
			TransitionManager.beginDelayedTransition((ViewGroup) itemView);
			itemView.setBackgroundResource(player == choosenPlayer ? R.drawable.background_item_checked : R.drawable.background_item);
		}
	}
	
	private Context context;
	
	private ActionLustrate.OnLustrationListener listener;
	private List<Player> candidates;
	private Player choosenPlayer;
	
	LustrationCandidatesAdapter(Context context)
	{
		this.context = context;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return new ViewHolder(inflater.inflate(R.layout.item_lustration_candidate, parent, false));
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		Player player = candidates.get(position);
		holder.bind(player);
	}
	
	@Override
	public int getItemCount()
	{
		return candidates.size();
	}
	
	public void setAction(ActionLustrate action)
	{
		listener = action.getLustrationListener();
		candidates = action.getAvailablePlayers();
		choosenPlayer = action.getLustratedPlayer();
		notifyDataSetChanged();
	}
}
