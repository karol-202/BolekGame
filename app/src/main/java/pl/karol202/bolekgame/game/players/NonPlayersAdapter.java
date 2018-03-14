package pl.karol202.bolekgame.game.players;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import java8.util.stream.Collectors;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;

import java.util.ArrayList;
import java.util.List;

class NonPlayersAdapter extends RecyclerView.Adapter<NonPlayersAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textName;
		private ImageButton buttonSettings;
		
		private User user;
		
		ViewHolder(View view)
		{
			super(view);
			textName = view.findViewById(R.id.text_non_player_name);
			buttonSettings = view.findViewById(R.id.button_user_settings);
			buttonSettings.setOnClickListener(v -> showSettingsWindow());
		}
		
		void bind(User user)
		{
			this.user = user;
			
			textName.setText(user.getName());
			buttonSettings.setEnabled(user instanceof RemoteUser);
		}
		
		@SuppressLint("InflateParams")
		private void showSettingsWindow()
		{
			if(user == null || !(user instanceof RemoteUser)) return;
			RemoteUser user = (RemoteUser) this.user;
			PopupWindow window = new PopupWindow(context);
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.popup_user_settings, null);
			onSettingsViewCreate(view, user);
			
			window.setContentView(view);
			window.showAsDropDown(buttonSettings);
		}
		
		private void onSettingsViewCreate(View view, RemoteUser user)
		{
			initMutePanel(view, user);
		}
		
		private void initMutePanel(View view, RemoteUser user)
		{
			View panelMute = view.findViewById(R.id.panel_user_mute);
			panelMute.setOnClickListener(v -> toggleUserMute());
			
			ImageView imageMute = panelMute.findViewById(R.id.image_user_mute);
			imageMute.setImageResource(user.isMuted() ? R.drawable.ic_speaker_on_black_24dp : R.drawable.ic_speaker_off_black_24dp);
			
			TextView textMute = panelMute.findViewById(R.id.text_user_mute);
			textMute.setText(user.isMuted() ? R.string.text_user_unmute : R.string.text_user_mute);
		}
		
		private void toggleUserMute(RemoteUser user)
		{
			user.setMute(!user.isMuted());
		}
		
		private void initVolumePanel(View view, RemoteUser user)
		{
			View panelVolume = view.findViewById(R.id.panel_user_volume);
			
			SeekBar seekBarVolume = panelVolume.findViewById(R.id.seekBar_user_volume);
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
