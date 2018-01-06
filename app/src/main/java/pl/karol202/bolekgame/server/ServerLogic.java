package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;

import java.util.List;

public class ServerLogic extends ClientListenerAdapter
{
	private ActivityServer activityServer;
	private Client client;
	
	ServerLogic(ActivityServer activityServer, Client client)
	{
		this.activityServer = activityServer;
		this.client = client;
		
		client.setClientListener(this);
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
	public void onUsersUpdate(List<String> users)
	{
	
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
}
