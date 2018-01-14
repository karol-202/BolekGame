package pl.karol202.bolekgame.server;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;

import java.util.List;

public class ServerLogic extends ClientListenerAdapter
{
	private ActivityServer activityServer;
	private Client client;
	private String serverName;
	private int serverCode;
	
	private Users users;
	
	ServerLogic(ActivityServer activityServer, Client client, String serverName, int serverCode, Users users)
	{
		this.activityServer = activityServer;
		this.client = client;
		this.serverName = serverName;
		this.serverCode = serverCode;
		this.users = users;
		
		client.setClientListener(this); //Must be after initializing users because of packet execution caused by this method
	}
	
	ServerLogic(ActivityServer activityServer, Client client, String serverName, int serverCode, String localUserName)
	{
		this.activityServer = activityServer;
		this.client = client;
		this.serverName = serverName;
		this.serverCode = serverCode;
		this.users = new Users(new LocalUser(localUserName, this));
		
		client.setClientListener(this); //Must be after initializing users because of packet execution caused by this method
	}
	
	void logout()
	{
		client.sendPacket(new OutputPacketLogout());
	}
	
	void setReady()
	{
		client.sendPacket(new OutputPacketReady());
	}
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(() -> activityServer.onDisconnect());
	}
	
	@Override
	public void onLoggedOut()
	{
		runInUIThread(() -> activityServer.onLoggedOut());
	}
	
	@Override
	public void onFailure(int problem)
	{
		runInUIThread(() -> activityServer.onError());
	}
	
	@Override
	public void onUsersUpdate(List<String> usernames, List<Boolean> readiness)
	{
		runInUIThread(() -> users.updateUsersList(usernames, readiness));
	}
	
	@Override
	public void onServerStatusUpdate(boolean gameAvailable)
	{
		//Currently not used; game availability checked locally.
	}
	
	@Override
	public void onGameStart(List<String> players)
	{
		runInUIThread(() -> activityServer.onGameStart());
	}
	
	private void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
	
	String getServerName()
	{
		return serverName;
	}
	
	int getServerCode()
	{
		return serverCode;
	}
	
	Users getUsers()
	{
		return users;
	}
}
