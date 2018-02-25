package pl.karol202.bolekgame.game.players;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java8.util.stream.Collectors;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.server.User;

import java.util.ArrayList;
import java.util.List;

class NonPlayersAdapter extends RecyclerView.Adapter<NonPlayersAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_non_player_name);
		}
		
		void bind(User user)
		{
			textName.setText(user.getName());
		}
	}
	
	private Context context;
	private Players players;
	
	private List<User> usersList;
	
	NonPlayersAdapter(Context context)
	{
		this.context = context;
		usersList = new ArrayList<>();
	}
	
	@Override
	public NonPlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return new NonPlayersAdapter.ViewHolder(inflater.inflate(R.layout.item_non_player, parent, false));
	}
	
	@Override
	public void onBindViewHolder(NonPlayersAdapter.ViewHolder holder, int position)
	{
		holder.bind(usersList.get(position));
	}
	
	@Override
	public int getItemCount()
	{
		return usersList.size();
	}
	
	void onUserAdd(User user)
	{
		usersList.add(user);
		notifyItemInserted(usersList.size() - 1);
	}
	
	void onUserRemove(User user)
	{
		int index = usersList.indexOf(user);
		usersList.remove(user);
		notifyItemRemoved(index);
	}
	
	void onPlayerAdd(Player player)
	{
		int index = usersList.indexOf(player.getUser());
		usersList.remove(player.getUser());
		notifyItemRemoved(index);
	}
	
	void onPlayerRemove(Player player)
	{
		if(!players.getUsersStream().anyMatch(u -> u == player.getUser())) return;
		usersList.add(player.getUser());
		notifyItemInserted(usersList.size() - 1);
	}
	
	public void setPlayers(Players players)
	{
		this.players = players;
		updateList();
	}
	
	private void updateList()
	{
		usersList = players.getUsersStream().filter(u -> !players.containsPlayerOfUser(u)).collect(Collectors.toList());
		notifyDataSetChanged();
	}
	
	void setContext(Context context)
	{
		this.context = context;
	}
}
