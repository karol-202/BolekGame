package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;

import java.util.List;

public class ServerLogic extends ClientListenerAdapter
{
	private ActivityServer activityServer;
	private Client client;
	
	private Users users;
	
	ServerLogic(ActivityServer activityServer, Client client, String localUserName)
	{
		this.activityServer = activityServer;
		this.client = client;
		
		LocalUser localUser = new LocalUser(localUserName, this);
		users = new Users(localUser);
		
		client.setClientListener(this); //Must be after initializing users because of packet execution caused by this method
	}
	
	public void setReady()
	{
		client.sendPacket(new OutputPacketReady());
	}
	
	@Override
	public void onDisconnect()
	{
		activityServer.onDisconnect();
	}
	
	@Override
	public void onLoggedOut()
	{
		activityServer.onLoggedOut();
	}
	
	@Override
	public void onFailure(int problem)
	{
	
	}
	
	@Override
	public void onUsersUpdate(List<String> usernames)
	{
		users.updateUsers(usernames);
	}
	
	@Override
	public void onSetReady(String username)
	{
	
	}
	
	@Override
	public void onServerStatusUpdate(boolean gameAvailable)
	{
	
	}
	
	@Override
	public void onGameStart(List<String> players)
	{
	
	}
	
	Users getUsers()
	{
		return users;
	}
}
