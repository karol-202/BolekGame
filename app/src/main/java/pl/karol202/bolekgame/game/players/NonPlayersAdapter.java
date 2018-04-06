package pl.karol202.bolekgame.game.players;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java8.util.stream.Collectors;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.UserSettingsWindow;
import pl.karol202.bolekgame.settings.Settings;

import java.util.ArrayList;
import java.util.List;

class NonPlayersAdapter extends RecyclerView.Adapter<NonPlayersAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		private ImageButton buttonSettings;
		private UserSettingsWindow settingsWindow;
		
		private User user;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_non_player_name);
			
			buttonSettings = view.findViewById(R.id.button_non_player_settings);
			buttonSettings.setOnClickListener(v -> showSettingsWindow(settingsWindow, buttonSettings));
			
			settingsWindow = new UserSettingsWindow(view);
		}
		
		void bind(User user)
		{
			this.user = user;
			if(user instanceof RemoteUser) settingsWindow.setUser((RemoteUser) user);
			
			textName.setText(user.getName());
			buttonSettings.setVisibility(Settings.isVoiceChatEnabled(context) && user instanceof RemoteUser ? View.VISIBLE : View.GONE);
		}
	}
	
	private Context context;
	private Players players;
	private List<User> usersList;
	private UserSettingsWindow currentSettingsWindow;
	
	NonPlayersAdapter(Context context)
	{
		this.context = context;
		usersList = new ArrayList<>();
	}
	
	@NonNull
	@Override
	public NonPlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return new NonPlayersAdapter.ViewHolder(inflater.inflate(R.layout.item_non_player, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull NonPlayersAdapter.ViewHolder holder, int position)
	{
		holder.bind(usersList.get(position));
	}
	
	@Override
	public int getItemCount()
	{
		return usersList.size();
	}
	
	private void showSettingsWindow(UserSettingsWindow settingsWindow, View anchor)
	{
		if(currentSettingsWindow != null && currentSettingsWindow.isShowing()) currentSettingsWindow.dismiss();
		currentSettingsWindow = settingsWindow;
		settingsWindow.show(anchor);
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
		if(currentSettingsWindow != null && currentSettingsWindow.getUser() == user) currentSettingsWindow.dismiss();
	}
	
	void onPlayerAdd(Player player)
	{
		int index = usersList.indexOf(player.getUser());
		usersList.remove(player.getUser());
		notifyItemRemoved(index);
		if(currentSettingsWindow != null && currentSettingsWindow.getUser() == player.getUser())
			currentSettingsWindow.dismiss();
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
