package pl.karol202.bolekgame.game.players;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;
import pl.karol202.bolekgame.server.LocalUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.utils.ItemDivider;

public class ScreenPlayers extends Screen
{
	private class UsersListener implements Users.OnUsersUpdateListener
	{
		@Override
		public void onUserAdd(User user)
		{
			if(playersAdapter == null || nonPlayersAdapter == null) return;
			nonPlayersAdapter.onUserAdd(user);
			updateNonPlayersViews();
		}
		
		@Override
		public void onUserRemove(User user, int position)
		{
			if(playersAdapter == null || nonPlayersAdapter == null) return;
			nonPlayersAdapter.onUserRemove(user);
			updateNonPlayersViews();
		}
		
		@Override
		public void onUsersUpdate() { }
	}
	
	private class PlayersListener implements Players.OnPlayersUpdateListener
	{
		@Override
		public void onPlayerAdd(Player player)
		{
			if(playersAdapter == null || nonPlayersAdapter == null) return;
			playersAdapter.onPlayerAdd();
			nonPlayersAdapter.onPlayerAdd(player);
			updateNonPlayersViews();
		}
		
		@Override
		public void onPlayerRemove(int position, Player player)
		{
			if(playersAdapter == null || nonPlayersAdapter == null) return;
			playersAdapter.onPlayerRemove(position, player);
			nonPlayersAdapter.onPlayerRemove(player);
			updateNonPlayersViews();
		}
		
		@Override
		public void onPlayerUpdate(int position)
		{
			if(playersAdapter == null || nonPlayersAdapter == null) return;
			playersAdapter.onPlayerUpdate(position);
		}
	}
	
	private ImageButton buttonVoiceChatMicrophone;
	private ImageButton buttonVoiceChatSpeaker;
	private RecyclerView recyclerPlayers;
	private View viewSeparatorNonPlayersTop;
	private TextView textNonPlayers;
	private View viewSeparatorNonPlayersBottom;
	private RecyclerView recyclerNonPlayers;
	
	private PlayersAdapter playersAdapter;
	private NonPlayersAdapter nonPlayersAdapter;
	private Users.OnUsersUpdateListener usersListener;
	private Players.OnPlayersUpdateListener playersListener;
	
	private Players players;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_players, container, false);
		
		playersAdapter = new PlayersAdapter(getActivity());
		nonPlayersAdapter = new NonPlayersAdapter(getActivity());
		
		buttonVoiceChatMicrophone = view.findViewById(R.id.button_voice_chat_microphone);
		buttonVoiceChatMicrophone.setOnClickListener(v -> toggleVoiceChatMicrophone());
		
		buttonVoiceChatSpeaker = view.findViewById(R.id.button_voice_chat_speaker);
		buttonVoiceChatSpeaker.setOnClickListener(v -> toggleVoiceChatSpeaker());
		
		recyclerPlayers = view.findViewById(R.id.recycler_players);
		recyclerPlayers.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerPlayers.setAdapter(playersAdapter);
		recyclerPlayers.addItemDecoration(new ItemDivider(getActivity()));
		
		viewSeparatorNonPlayersTop = view.findViewById(R.id.view_separator_non_players_top);
		
		textNonPlayers = view.findViewById(R.id.text_non_players);
		
		viewSeparatorNonPlayersBottom = view.findViewById(R.id.view_separator_non_players_bottom);
		
		recyclerNonPlayers = view.findViewById(R.id.recycler_non_players);
		recyclerNonPlayers.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerNonPlayers.setAdapter(nonPlayersAdapter);
		recyclerNonPlayers.addItemDecoration(new ItemDivider(getActivity()));
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		if(usersListener != null && playersListener != null) return;
		usersListener = new UsersListener();
		playersListener = new PlayersListener();
		
		players = gameLogic.getPlayers();
		players.addOnUsersUpdateListener(usersListener);
		players.addOnPlayersUpdateListener(playersListener);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		playersAdapter.setPlayers(players);
		nonPlayersAdapter.setPlayers(players);
		updateNonPlayersViews();
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		if(players == null) return;
		players.removeOnUsersUpdateListener(usersListener);
		players.removeOnPlayersUpdateListener(playersListener);
		playersAdapter.setContext(null);
		nonPlayersAdapter.setContext(null);
	}
	
	private void toggleVoiceChatMicrophone()
	{
		LocalUser localUser = players.getLocalUser();
		boolean enable = !localUser.isMicrophoneEnabled();
		localUser.setMicrophoneEnabled(enable);
		buttonVoiceChatMicrophone.setImageResource(enable ? R.drawable.ic_microphone_off_black_24dp : R.drawable.ic_microphone_on_black_24dp);
	}
	
	private void toggleVoiceChatSpeaker()
	{
		LocalUser localUser = players.getLocalUser();
		boolean enable = !localUser.isSpeakerEnabled();
		localUser.setSpeakerEnabled(enable);
		buttonVoiceChatSpeaker.setImageResource(enable ? R.drawable.ic_speaker_off_black_24dp : R.drawable.ic_speaker_on_black_24dp);
	}
	
	private void updateNonPlayersViews()
	{
		int visibility = nonPlayersAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
		viewSeparatorNonPlayersTop.setVisibility(visibility);
		textNonPlayers.setVisibility(visibility);
		viewSeparatorNonPlayersBottom.setVisibility(visibility);
		recyclerNonPlayers.setVisibility(visibility);
	}
}
