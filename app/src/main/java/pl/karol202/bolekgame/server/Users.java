package pl.karol202.bolekgame.server;

import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;

public class Users
{
	interface OnUsersUpdateListener
	{
		void onUserAdd();
		
		void onUserRemove(int position);
		
		void onUsersUpdate();
	}
	
	public static final int MIN_USERS = 2;
	
	private List<User> users;
	private LocalUser localUser;
	
	private OnUsersUpdateListener usersUpdateListener;
	
	Users(LocalUser localUser)
	{
		this.users = new ArrayList<>();
		this.localUser = localUser;
		users.add(localUser);
	}
	
	private void addUser(User user)
	{
		if(users.contains(user)) return;
		users.add(user);
		if(usersUpdateListener != null) usersUpdateListener.onUserAdd();
	}
	
	private void removeUser(User user)
	{
		if(!users.contains(user)) return;
		int userIndex = users.indexOf(user);
		users.remove(user);
		if(usersUpdateListener != null) usersUpdateListener.onUserRemove(userIndex);
	}
	
	void updateUsersList(List<String> usernames, List<Boolean> readiness)
	{
		List<User> usersToAdd = new ArrayList<>();
		List<User> usersToRemvoe = new ArrayList<>();
		
		usernamesLoop:
		for(String username : usernames)
		{
			for(User user : users) if(username.equals(user.getName())) continue usernamesLoop;
			int userIndex = usernames.indexOf(username);
			boolean ready = readiness.get(userIndex);
			usersToAdd.add(new User(username, ready));
		}
		for(User user : users)
		{
			if(!usernames.contains(user.getName())) usersToRemvoe.add(user);
			else
			{
				int userIndex = usernames.indexOf(user.getName());
				boolean ready = readiness.get(userIndex);
				if(user.isReady() == ready) continue;
				user.setReady(ready);
			}
		}
		
		for(User user : usersToAdd) addUser(user);
		for(User user : usersToRemvoe) removeUser(user);
		if(usersToAdd.size() == 0 && usersToRemvoe.size() == 0 && usersUpdateListener != null)
			usersUpdateListener.onUsersUpdate();
	}
	
	void onServerStatusUpdate()
	{
		if(usersUpdateListener != null) usersUpdateListener.onUsersUpdate();
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
	
	String getLocalUserName()
	{
		return localUser.getName();
	}
	
	void setOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListener = listener;
	}
}
