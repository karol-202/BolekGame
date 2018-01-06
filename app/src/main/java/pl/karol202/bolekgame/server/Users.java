package pl.karol202.bolekgame.server;

import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;

class Users
{
	interface OnUsersUpdateListener
	{
		void onUsersUpdate();
	}
	
	private static final int MIN_USERS = 5;
	
	private List<User> users;
	private LocalUser localUser;
	
	private List<OnUsersUpdateListener> usersUpdateListeners;
	
	Users()
	{
		users = new ArrayList<>();
		
		usersUpdateListeners = new ArrayList<>();
	}
	
	void addUser(User user)
	{
		users.add(user);
		callOnUsersUpdateListener();
	}
	
	void removeUser(User user)
	{
		users.remove(user);
		callOnUsersUpdateListener();
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
	
	void setLocalUser(LocalUser localUser)
	{
		this.localUser = localUser;
	}
	
	void addOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListeners.add(listener);
	}
	
	private void callOnUsersUpdateListener()
	{
		StreamSupport.stream(usersUpdateListeners).forEach(OnUsersUpdateListener::onUsersUpdate);
	}
}
