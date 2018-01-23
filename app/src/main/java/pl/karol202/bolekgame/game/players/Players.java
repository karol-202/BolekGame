package pl.karol202.bolekgame.game.players;

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
	private LocalPlayer localPlayer;
	
	private List<OnPlayersUpdateListener> playersUpdateListeners;
	
	public Players(LocalPlayer localPlayer)
	{
		this.players = new ArrayList<>();
		this.localPlayer = localPlayer;
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
	
	public void updatePlayersList(List<String> names)
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
	
	Player getPlayer(int position)
	{
		return players.get(position);
	}
	
	public int getPlayersAmount()
	{
		return players.size();
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	public String getLocalPlayerName()
	{
		return localPlayer.getName();
	}
	
	public void addOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.add(listener);
	}
	
	void removeOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.remove(listener);
	}
}
