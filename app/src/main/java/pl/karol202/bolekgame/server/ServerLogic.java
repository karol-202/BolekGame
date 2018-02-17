package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;
import pl.karol202.bolekgame.game.GameData;
import pl.karol202.bolekgame.utils.Logic;
import pl.karol202.bolekgame.utils.TextChat;

import java.util.List;

class ServerLogic extends Logic<ActivityServer> implements ServerStatusSupplier
{
	private String serverName;
	private int serverCode;
	
	private Users users;
	private TextChat textChat;
	private boolean gameInProgress;
	
	ServerLogic(Client client, String serverName, int serverCode, String localUserName)
	{
		super(client);
		
		this.serverName = serverName;
		this.serverCode = serverCode;
		
		this.users = new Users(new LocalUser(localUserName, this));
		this.textChat = new TextChat();
	}
	
	void logout()
	{
		sendPacket(new OutputPacketLogout());
		suspend();
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
		gameInProgress = !gameAvailable;
		runInUIThread(() -> users.onServerStatusUpdate());
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
		suspendClient();
	}
	
	GameData createGameData()
	{
		return new GameData(client, textChat, serverName, serverCode);
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
	
	boolean isConnected()
	{
		return client.isConnected();
	}
	
	@Override
	public boolean isGameInProgress()
	{
		return gameInProgress;
	}
}
