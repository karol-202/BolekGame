package pl.karol202.bolekgame.game;

import java.util.ArrayList;
import java.util.List;

public class Players
{
	public interface OnPlayersUpdateListener
	{
		void onPlayerAdd();
		
		void onPlayerRemove(int position, Player player);
	}
	
	private List<Player> players;
	
	private List<OnPlayersUpdateListener> playersUpdateListeners;
	
	Players(LocalPlayer localPlayer)
	{
		this.players = new ArrayList<>();
		players.add(localPlayer);
		
		playersUpdateListeners = new ArrayList<>();
	}
	
	private void addPlayer(Player player)
	{
		if(players.contains(player)) return;
		players.add(player);
		for(OnPlayersUpdateListener listener : playersUpdateListeners) listener.onPlayerAdd();
	}
	
	private void removePlayer(Player player)
	{
		if(!players.contains(player)) return;
		int playerIndex = players.indexOf(player);
		players.remove(player);
		for(OnPlayersUpdateListener listener : playersUpdateListeners) listener.onPlayerRemove(playerIndex, player);
	}
	
	void updatePlayersList(List<String> names)
	{
		List<Player> playersToAdd = new ArrayList<>();
		List<Player> playersToRemove = new ArrayList<>();
		
		namesLoop:
		for(String name : names)
		{
			for(Player player : players) if(name.equals(player.getName())) continue namesLoop;
			playersToAdd.add(new RemotePlayer(name));
		}
		for(Player player : players)
			if(!names.contains(player.getName())) playersToRemove.add(player);
		
		for(Player player : playersToAdd) addPlayer(player);
		for(Player player : playersToRemove) removePlayer(player);
	}
	
	public Player getPlayer(int position)
	{
		return players.get(position);
	}
	
	public int getPlayersAmount()
	{
		return players.size();
	}
	
	public void addOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.add(listener);
	}
	
	public void removeOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.remove(listener);
	}
}
