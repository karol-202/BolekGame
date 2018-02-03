package pl.karol202.bolekgame.game.players;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Position;

class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		private View panelPosition;
		private TextView textPosition;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_player_name);
			panelPosition = view.findViewById(R.id.panel_player_position);
			textPosition = view.findViewById(R.id.text_player_position);
		}
		
		void bind(Player player)
		{
			textName.setText(player.getName());
			panelPosition.setVisibility(player.getPosition() != Position.NONE ? View.VISIBLE : View.GONE);
			textPosition.setText(player.getPosition().getName());
		}
	}
	
	private Context context;
	private Players players;
	
	PlayersAdapter(Context context)
	{
		this.context = context;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return new ViewHolder(inflater.inflate(R.layout.item_player, parent, false));
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		holder.bind(players.getPlayer(position));
	}
	
	@Override
	public int getItemCount()
	{
		return players != null ? players.getPlayersAmount() : 0;
	}
	
	void onPlayerAdd()
	{
		notifyItemInserted(players.getPlayersAmount() - 1);
	}
	
	void onPlayerRemove(int position)
	{
		notifyItemRemoved(position);
	}
	
	void onPlayerUpdate(int position)
	{
		notifyItemChanged(position);
	}
	
	public void setPlayers(Players players)
	{
		this.players = players;
		notifyDataSetChanged();
	}
	
	void setContext(Context context)
	{
		this.context = context;
	}
}
