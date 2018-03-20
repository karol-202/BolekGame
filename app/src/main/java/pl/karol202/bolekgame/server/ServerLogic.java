package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;
import pl.karol202.bolekgame.game.GameData;
import pl.karol202.bolekgame.utils.Logic;
import pl.karol202.bolekgame.utils.TextChat;
import pl.karol202.bolekgame.voice.VoiceService;

import java.util.List;

class ServerLogic extends Logic<ActivityServer>
{
	private String serverName;
	private int serverCode;
	
	private Users users;
	private TextChat textChat;
	private VoiceService voiceService;
	private boolean gameInProgress;
	
	ServerLogic(Client client, String serverName, int serverCode, String localUserName)
	{
		super(client);
		
		this.serverName = serverName;
		this.serverCode = serverCode;
		
		this.users = new Users(localUserName);
		
		this.textChat = new TextChat();
	}
	
	void startVoiceCommunication(VoiceService voiceService)
	{
		this.voiceService = voiceService;
		voiceService.setUsers(users);
		voiceService.start();
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
	
	boolean isMicrophoneEnabled()
	{
		return users.getLocalUser().isMicrophoneEnabled();
	}
	
	void setMicrophoneEnabled(boolean enabled)
	{
		users.getLocalUser().setMicrophoneEnabled(enabled);
	}
	
	boolean isSpeakerEnabled()
	{
		return users.getLocalUser().isSpeakerEnabled();
	}
	
	void setSpeakerEnabled(boolean enabled)
	{
		users.getLocalUser().setSpeakerEnabled(enabled);
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
