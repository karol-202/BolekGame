package pl.karol202.bolekgame.game.players;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;
import pl.karol202.bolekgame.utils.ItemDivider;

public class ScreenPlayers extends Screen
{
	private class PlayersListener implements Players.OnPlayersUpdateListener
	{
		@Override
		public void onPlayerAdd()
		{
			if(playersAdapter == null) return;
			playersAdapter.onPlayerAdd();
		}
		
		@Override
		public void onPlayerRemove(int position, Player player)
		{
			if(playersAdapter == null) return;
			playersAdapter.onPlayerRemove(position);
		}
		
		@Override
		public void onPlayerUpdate(int position)
		{
			if(playersAdapter == null) return;
			playersAdapter.onPlayerUpdate(position);
		}
	}
	
	private RecyclerView recyclerPlayers;
	
	private PlayersAdapter playersAdapter;
	private Players.OnPlayersUpdateListener playersListener;
	
	private Players players;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_players, container, false);
		
		playersAdapter = new PlayersAdapter(getActivity());
		playersListener = new PlayersListener();
		
		recyclerPlayers = view.findViewById(R.id.recycler_players);
		recyclerPlayers.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerPlayers.setAdapter(playersAdapter);
		recyclerPlayers.addItemDecoration(new ItemDivider(getActivity()));
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		players = gameLogic.getPlayers();
		players.addOnPlayersUpdateListener(playersListener);
		playersAdapter.setPlayers(players);
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		players.removeOnPlayersUpdateListener(playersListener);
		playersAdapter.setContext(null);
	}
}
