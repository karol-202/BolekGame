package pl.karol202.bolekgame.game.players;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.server.UserSettingsWindow;
import pl.karol202.bolekgame.settings.Settings;

class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		
		private View panelRole;
		private TextView textRole;
		
		private View panelPosition;
		private TextView textPosition;
		
		private ImageButton buttonSettings;
		
		private UserSettingsWindow settingsWindow;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_player_name);
			
			panelRole = view.findViewById(R.id.panel_player_role);
			textRole = view.findViewById(R.id.text_player_role);
			
			panelPosition = view.findViewById(R.id.panel_player_position);
			textPosition = view.findViewById(R.id.text_player_position);
			
			buttonSettings = view.findViewById(R.id.button_player_settings);
			buttonSettings.setOnClickListener(v -> showSettingsWindow(settingsWindow, buttonSettings));
			
			settingsWindow = new UserSettingsWindow(view);
		}
		
		void bind(Player player)
		{
			textName.setText(player.getName());
			
			Role role = player.getRole();
			panelRole.setVisibility(role != null ? View.VISIBLE : View.GONE);
			if(role != null) textRole.setText(role.getAbbr());
			
			panelPosition.setVisibility(players.getPlayerPosition(player) != Position.NONE ? View.VISIBLE : View.GONE);
			textPosition.setText(players.getPlayerPosition(player).getName());
			
			buttonSettings.setVisibility(Settings.isVoiceChatEnabled(context) && player instanceof RemotePlayer ? View.VISIBLE : View.GONE);
			if(player instanceof RemotePlayer) settingsWindow.setUser(((RemotePlayer) player).getUser());
		}
	}
	
	private Context context;
	private Players players;
	private UserSettingsWindow currentSettingsWindow;
	
	PlayersAdapter(Context context)
	{
		this.context = context;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return new ViewHolder(inflater.inflate(R.layout.item_player, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{
		holder.bind(players.getPlayer(position));
	}
	
	@Override
	public int getItemCount()
	{
		return players != null ? players.getPlayersAmount() : 0;
	}
	
	private void showSettingsWindow(UserSettingsWindow settingsWindow, View anchor)
	{
		if(currentSettingsWindow != null && currentSettingsWindow.isShowing()) currentSettingsWindow.dismiss();
		currentSettingsWindow = settingsWindow;
		settingsWindow.show(anchor);
	}
	
	void onPlayerAdd()
	{
		notifyItemInserted(players.getPlayersAmount() - 1);
	}
	
	void onPlayerRemove(int position, Player player)
	{
		notifyItemRemoved(position);
		if(currentSettingsWindow != null && currentSettingsWindow.getUser() == player.getUser())
			currentSettingsWindow.dismiss();
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
