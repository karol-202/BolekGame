package pl.karol202.bolekgame.server;

import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;

class Users
{
	interface OnUsersUpdateListener
	{
		void onUserAdd();
		
		void onUserRemove(int position);
	}
	
	private static final int MIN_USERS = 2;
	
	private List<User> users;
	private LocalUser localUser;
	
	private List<OnUsersUpdateListener> usersUpdateListeners;
	
	Users(LocalUser localUser)
	{
		this.users = new ArrayList<>();
		this.localUser = localUser;
		users.add(localUser);
		
		usersUpdateListeners = new ArrayList<>();
	}
	
	private void addUser(User user)
	{
		if(users.contains(user)) return;
		users.add(user);
		StreamSupport.stream(usersUpdateListeners).forEach(OnUsersUpdateListener::onUserAdd);
	}
	
	private void removeUser(User user)
	{
		if(!users.contains(user)) return;
		int userIndex = users.indexOf(user);
		users.remove(user);
		StreamSupport.stream(usersUpdateListeners).forEach(l -> l.onUserRemove(userIndex));
	}
	
	void updateUsers(List<String> usernames)
	{
		List<User> usersToAdd = new ArrayList<>();
		List<User> usersToRemvoe = new ArrayList<>();
		
		usernamesLoop:
		for(String username : usernames)
		{
			for(User user : users) if(username.equals(user.getName())) continue usernamesLoop;
			usersToAdd.add(new User(username));
		}
		usersLoop:
		for(User user : users)
		{
			for(String username : usernames) if(username.equals(user.getName())) continue usersLoop;
			usersToRemvoe.add(user);
		}
		
		for(User user : usersToAdd) addUser(user);
		for(User user : usersToRemvoe) removeUser(user);
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
	
	LocalUser getLocalUser()
	{
		return localUser;
	}
	
	void addOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListeners.add(listener);
	}
}
