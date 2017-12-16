package pl.karol202.bolekgame.ui.server;

import pl.karol202.bolekgame.client.Client;

import java.util.List;

public class ServerUI
{
	private ActivityServer activityServer;
	private Client client;
	
	public ServerUI(ActivityServer activityServer, Client client)
	{
		this.activityServer = activityServer;
		this.client = client;
	}
	
	public void onLoggedOut()
	{
		activityServer.onLoggedOut();
	}
	
	public void onFailure()
	{
	
	}
	
	public void onUsersUpdate(List<String> users)
	{
	
	}
	
	public void onSetReady(String username)
	{
	
	}
	
	public void onGameStart(List<String> players)
	{
	
	}
}
