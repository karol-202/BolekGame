package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.players.Player;

import java.util.Map;

public class AllVotesAdapter extends RecyclerView.Adapter<AllVotesAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		private TextView textVote;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_voter_name);
			textVote = view.findViewById(R.id.text_voter_vote);
		}
		
		void bind(Player player, boolean vote)
		{
			textName.setText(player.getName());
			textVote.setText(vote ? R.string.text_vote_yes : R.string.text_vote_no);
		}
	}
	
	private Context context;
	private Map<Player, Boolean> votes;
	
	AllVotesAdapter(Context context, Map<Player, Boolean> votes)
	{
		this.context = context;
		this.votes = votes;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_voter, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		Player[] players = new Player[votes.size()];
		Player player = votes.keySet().toArray(players)[position];
		holder.bind(player, votes.get(player));
	}
	
	@Override
	public int getItemCount()
	{
		return votes.size();
	}
}
