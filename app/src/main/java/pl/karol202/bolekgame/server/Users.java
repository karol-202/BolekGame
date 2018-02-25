package pl.karol202.bolekgame.server;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;

public class Users
{
	public interface OnUsersUpdateListener
	{
		void onUserAdd(User user);
		
		void onUserRemove(User user, int position);
		
		void onUsersUpdate();
	}
	
	public static final int MIN_USERS = 2;
	
	private List<User> users;
	private String localUserName;
	
	private List<OnUsersUpdateListener> usersUpdateListeners;
	
	Users(String localUserName)
	{
		this.users = new ArrayList<>();
		this.localUserName = localUserName;
		this.usersUpdateListeners = new ArrayList<>();
	}
	
	private void addUser(User user)
	{
		if(users.contains(user)) return;
		users.add(user);
		for(OnUsersUpdateListener listener : usersUpdateListeners) listener.onUserAdd(user);
	}
	
	private void removeUser(User user)
	{
		if(!users.contains(user)) return;
		int userIndex = users.indexOf(user);
		users.remove(user);
		for(OnUsersUpdateListener listener : usersUpdateListeners) listener.onUserRemove(user, userIndex);
	}
	
	public void updateUsersList(List<String> usernames, List<Boolean> readiness, List<String> addresses)
	{
		List<User> usersToAdd = new ArrayList<>();
		List<User> usersToRemove = new ArrayList<>();
		
		usernamesLoop:
		for(String username : usernames)
		{
			for(User user : users) if(username.equals(user.getName())) continue usernamesLoop;
			
			int userIndex = usernames.indexOf(username);
			boolean ready = readiness.get(userIndex);
			String address = addresses.get(userIndex);
			if(username.equals(localUserName)) usersToAdd.add(new LocalUser(username, ready));
			else usersToAdd.add(new RemoteUser(username, ready, address));
		}
		for(User user : users)
		{
			if(!usernames.contains(user.getName())) usersToRemove.add(user);
			else
			{
				int userIndex = usernames.indexOf(user.getName());
				boolean ready = readiness.get(userIndex);
				if(user.isReady() == ready) continue;
				user.setReady(ready);
			}
		}
		
		for(User user : usersToAdd) addUser(user);
		for(User user : usersToRemove) removeUser(user);
		if(usersToAdd.size() == 0 && usersToRemove.size() == 0)
			for(OnUsersUpdateListener listener : usersUpdateListeners) listener.onUsersUpdate();
	}
	
	void onServerStatusUpdate()
	{
		for(OnUsersUpdateListener listener : usersUpdateListeners) listener.onUsersUpdate();
	}
	
	public Stream<User> getUsersStream()
	{
		return StreamSupport.stream(users);
	}
	
	User getUser(int position)
	{
		return users.get(position);
	}
	
	int getUsersAmount()
	{
		return users.size();
	}
	
	int getReadyUsersAmount()
	{
		return (int) StreamSupport.stream(users).filter(User::isReady).count();
	}
	
	boolean areThereEnoughUsers()
	{
		return users.size() >= MIN_USERS;
	}
	
	public String getLocalUserName()
	{
		return localUserName;
	}
	
	public void addOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListeners.add(listener);
	}
	
	public void removeOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListeners.remove(listener);
	}
}
