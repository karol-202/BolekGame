package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;

import java.util.List;

public class ServerLogic
{
	private ActivityServer activityServer;
	private Client client;
	
	public ServerLogic(ActivityServer activityServer, Client client)
	{
		this.activityServer = activityServer;
		this.client = client;
	}
	
	public void onLoggedOut()
	{
		activityServer.onLoggedOut();
	}
	
	public void onFailure(int problem)
	{
	
	}
	
	public void onUsersUpdate(List<String> users)
	{
	
	}
	
	public void onSetReady(String username)
	{
	
	}
	
	public void onServerStatusUpdate(boolean gameAvailable)
	{
	
	}
	
	public void onGameStart(List<String> players)
	{
	
	}
}
