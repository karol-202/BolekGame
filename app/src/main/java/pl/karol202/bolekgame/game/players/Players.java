package pl.karol202.bolekgame.game.players;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;
import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.server.LocalUser;
import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.Users;

import java.util.ArrayList;
import java.util.List;

public class Players
{
	public interface OnPlayersUpdateListener
	{
		void onPlayerAdd(Player player);
		
		void onPlayerRemove(int position, Player player);
		
		void onPlayerUpdate(int position);
	}
	
	private Users users;
	private List<Player> players;
	
	private List<Users.OnUsersUpdateListener> usersUpdateListeners;
	private List<OnPlayersUpdateListener> playersUpdateListeners;
	
	public Players(Users users)
	{
		this.users = users;
		this.players = new ArrayList<>();
		
		usersUpdateListeners = new ArrayList<>();
		playersUpdateListeners = new ArrayList<>();
	}
	
	private void addPlayer(Player player)
	{
		if(players.contains(player)) return;
		players.add(player);
		for(OnPlayersUpdateListener listener : playersUpdateListeners) listener.onPlayerAdd(player);
	}
	
	private void removePlayer(Player player)
	{
		if(!players.contains(player)) return;
		int playerIndex = players.indexOf(player);
		players.remove(player);
		for(OnPlayersUpdateListener listener : playersUpdateListeners) listener.onPlayerRemove(playerIndex, player);
	}
	
	public void updateUsersList(List<String> names, List<Boolean> readiness, List<String> addresses)
	{
		users.updateUsersList(names, readiness, addresses);
	}
	
	public void updatePlayersList(List<String> names)
	{
		List<Player> playersToAdd = new ArrayList<>();
		List<Player> playersToRemove = new ArrayList<>();
		
		namesLoop:
		for(String name : names)
		{
			for(Player player : players) if(name.equals(player.getName())) continue namesLoop;
			
			User correspondingUser = users.getUsersStream().filter(u -> u.getName().equals(name)).findAny().orElse(null);
			
			if(correspondingUser instanceof LocalUser && name.equals(getLocalPlayerName()))
				playersToAdd.add(new LocalPlayer((LocalUser) correspondingUser));
			else if(correspondingUser instanceof RemoteUser)
				playersToAdd.add(new RemotePlayer((RemoteUser) correspondingUser));
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
	
	public Stream<User> getUsersStream()
	{
		return users.getUsersStream();
	}
	
	public Stream<Player> getPlayersStream()
	{
		return StreamSupport.stream(players);
	}
	
	boolean containsPlayerOfUser(User user)
	{
		for(Player player : players)
			if(player.getUser() == user) return true;
		return false;
	}
	
	Player getPlayer(int position)
	{
		return players.get(position);
	}
	
	public int getPlayersAmount()
	{
		return players.size();
	}
	
	public String getLocalPlayerName()
	{
		return users.getLocalUserName();
	}
	
	public LocalUser getLocalUser()
	{
		return (LocalUser) getUsersStream().filter(u -> u instanceof LocalUser).findAny().orElse(null);
	}
	
	public void addOnUsersUpdateListener(Users.OnUsersUpdateListener listener)
	{
		usersUpdateListeners.add(listener);
		users.addOnUsersUpdateListener(listener);
	}
	
	void removeOnUsersUpdateListener(Users.OnUsersUpdateListener listener)
	{
		usersUpdateListeners.remove(listener);
		users.removeOnUsersUpdateListener(listener);
	}
	
	public void addOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.add(listener);
	}
	
	void removeOnPlayersUpdateListener(OnPlayersUpdateListener listener)
	{
		playersUpdateListeners.remove(listener);
	}
	
	public void removeAllListeners()
	{
		for(Users.OnUsersUpdateListener listener : usersUpdateListeners) users.removeOnUsersUpdateListener(listener);
	}
}
