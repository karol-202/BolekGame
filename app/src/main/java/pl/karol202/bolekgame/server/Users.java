package pl.karol202.bolekgame.server;

import com.crashlytics.android.Crashlytics;
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
	
	private List<User> users;
	private String localUserName;
	private int minUsers;
	
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
	
	public void updateUsers(List<String> usernames, List<Boolean> readiness, List<String> addresses)
	{
		Crashlytics.log("Updating users");
		List<User> usersToAdd = new ArrayList<>();
		List<User> usersToRemove = new ArrayList<>();
		
		usernamesLoop:
		for(String username : usernames)
		{
			for(User user : users) if(username.equals(user.getName())) continue usernamesLoop;
			
			int userIndex = usernames.indexOf(username);
			boolean ready = readiness.get(userIndex);
			String address = addresses.get(userIndex);
			if(username.equals(localUserName))
			{
				usersToAdd.add(new LocalUser(username, ready));
				Crashlytics.log("Added local user");
			}
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
		if(usersToAdd.size() == 0 && usersToRemove.size() == 0) onUsersUpdate();
	}
	
	void onUsersUpdate()
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
		return users.size() >= minUsers;
	}
	
	public String getLocalUserName()
	{
		return localUserName;
	}
	
	public LocalUser getLocalUser()
	{
		return (LocalUser) getUsersStream().filter(u -> u instanceof LocalUser).findAny().orElse(null);
	}
	
	public int getMinUsers()
	{
		return minUsers;
	}
	
	public void setMinUsers(int minUsers)
	{
		this.minUsers = minUsers;
	}
	
	public void addOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		if(usersUpdateListeners.contains(listener)) return;
		usersUpdateListeners.add(listener);
	}
	
	public void removeOnUsersUpdateListener(OnUsersUpdateListener listener)
	{
		usersUpdateListeners.remove(listener);
	}
}
