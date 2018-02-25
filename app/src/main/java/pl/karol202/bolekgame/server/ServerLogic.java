package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;
import pl.karol202.bolekgame.game.GameData;
import pl.karol202.bolekgame.utils.Logic;
import pl.karol202.bolekgame.utils.TextChat;
import pl.karol202.bolekgame.voice.UsersVoiceHandler;
import pl.karol202.bolekgame.voice.VoiceBinder;

import java.util.List;

class ServerLogic extends Logic<ActivityServer>
{
	private String serverName;
	private int serverCode;
	
	private Users users;
	private TextChat textChat;
	private VoiceBinder voiceBinder;
	private boolean gameInProgress;
	
	ServerLogic(Client client, String serverName, int serverCode, String localUserName)
	{
		super(client);
		
		this.serverName = serverName;
		this.serverCode = serverCode;
		
		users = new Users(localUserName);
		users.addOnUsersUpdateListener(new UsersVoiceHandler(() -> voiceBinder));
		
		textChat = new TextChat();
	}
	
	@Override
	protected void setActivity(ActivityServer activity)
	{
		super.setActivity(activity);
		if(activity != null) activity.bindVoiceService();
		else onVoiceServiceUnbind();
	}
	
	void onVoiceServiceBind(VoiceBinder voiceBinder)
	{
		this.voiceBinder = voiceBinder;
		voiceBinder.clearPeers();
		users.getUsersStream().filter(u -> u instanceof RemoteUser).map(u -> (RemoteUser) u).forEach(u -> voiceBinder.addPeer(u.getAddress()));
	}
	
	void onVoiceServiceUnbind()
	{
		this.voiceBinder = null;
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
	public void onUsersUpdate(List<String> usernames, List<Boolean> readiness, List<String> addresses)
	{
		runInUIThread(() -> users.updateUsersList(usernames, readiness, addresses));
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
		return new GameData(client, users, textChat, serverName, serverCode);
	}
	
	boolean isConnected()
	{
		return client.isConnected();
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
	
	boolean isGameInProgress()
	{
		return gameInProgress;
	}
}
