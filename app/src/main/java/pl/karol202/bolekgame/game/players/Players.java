package pl.karol202.bolekgame.game.players;

import pl.karol202.bolekgame.game.gameplay.Position;

import java.util.ArrayList;
import java.util.List;

public class Players
{
	public interface OnPlayersUpdateListener
	{
		void onPlayerAdd();
		
		void onPlayerRemove(int position, Player player);
		
		void onPlayerUpdate(int position);
	}
	
	private List<Player> players;
	private String localPlayerName;
	
	private List<OnPlayersUpdateListener> playersUpdateListeners;
	
	public Players(String localPlayerName)
	{
		this.players = new ArrayList<>();
		this.localPlayerName = localPlayerName;
		
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
			if(name.equals(localPlayerName)) playersToAdd.add(new LocalPlayer(name));
			else playersToAdd.add(new RemotePlayer(name));
		}
		for(Player player : players)
			if(!names.contains(player.getName())) playersToRemove.add(player);
		
		for(Player player : playersToAdd) addPlayer(player);
		for(Player player : playersToRemove) removePlayer(player);
	}
	
	public Player getPlayerAtPosition(Position position)
	{
		if(position == Position.NONE) return null;
		for(Player player : players)
			if(player.getPosition() == position) return player;
		return null;
	}
	
	public void setPlayerPositionAndResetRest(String playerName, Position position)
	{
		if(position == Position.NONE) return;
		for(Player player : players)
		{
			if(player.getName().equals(playerName)) setPlayerPosition(player, position);
			else if(player.getPosition() == position) setPlayerPosition(player, Position.NONE);
		}
	}
	
	private void setPlayerPosition(Player player, Position position)
	{
		if(player.getPosition() == position) return;
		player.setPosition(position);
		int playerIndex = players.indexOf(player);
		for(OnPlayersUpdateListener listener : playersUpdateListeners) listener.onPlayerUpdate(playerIndex);
	}
	
	public Player findPlayer(String name)
	{
		for(Player player : players)
			if(player.getName().equals(name)) return player;
		return null;
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
		return localPlayerName;
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
