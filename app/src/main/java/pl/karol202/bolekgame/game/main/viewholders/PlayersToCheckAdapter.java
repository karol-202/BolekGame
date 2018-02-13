package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionCheckPlayer;
import pl.karol202.bolekgame.game.players.Player;

import java.util.List;

class PlayersToCheckAdapter extends RecyclerView.Adapter<PlayersToCheckAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		
		private Player player;
		
		ViewHolder(View view)
		{
			super(view);
			view.setOnClickListener(v -> action.checkPlayer(player));
			textName = view.findViewById(R.id.text_player_to_check_name);
		}
		
		void bind(Player player)
		{
			this.player = player;
			textName.setText(player.getName());
		}
	}
	
	private Context context;
	private ActionCheckPlayer action;
	private List<Player> players;
	
	PlayersToCheckAdapter(Context context)
	{
		this.context = context;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_player_to_check, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		Player player = players.get(position);
		holder.bind(player);
	}
	
	@Override
	public int getItemCount()
	{
		return players.size();
	}
	
	void setAction(ActionCheckPlayer action)
	{
		this.action = action;
		players = action.getPlayersToCheck();
		notifyDataSetChanged();
	}
}
