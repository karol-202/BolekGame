package pl.karol202.bolekgame.server;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;
import pl.karol202.bolekgame.utils.Logic;

import java.util.List;

class ServerLogic extends Logic<ActivityServer>
{
	private String serverName;
	private int serverCode;
	
	private Users users;
	private TextChat textChat;
	
	ServerLogic(Client client, String serverName, int serverCode, String localUserName)
	{
		this.client = client;
		
		this.serverName = serverName;
		this.serverCode = serverCode;
		
		this.users = new Users(new LocalUser(localUserName, this));
		this.textChat = new TextChat();
		
		resumeClient(); //Must be after initializing users because of packet execution caused by this method
	}
	
	void logout()
	{
		sendPacket(new OutputPacketLogout());
	}
	
	void setReady()
	{
		sendPacket(new OutputPacketReady());
	}
	
	void sendMessage(String message)
	{
		sendPacket(new OutputPacketMessage(message));
		textChat.addEntry(users.getLocalUserName(), message);
		activity.onTextChatUpdate();
	}
	
	String getTextChatString()
	{
		return textChat.getTextChatString();
	}
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(() -> activity.onDisconnect());
	}
	
	@Override
	public void onLoggedOut()
	{
		runInUIThread(() -> activity.onLoggedOut());
	}
	
	@Override
	public void onFailure(int problem)
	{
		runInUIThread(() -> activity.onError());
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
	public void onMessage(String sender, String message)
	{
		textChat.addEntry(sender, message);
		runInUIThread(() -> activity.onTextChatUpdate());
	}
	
	@Override
	public void onGameStart(List<String> players)
	{
		runInUIThread(() -> activity.onGameStart());
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
