package pl.karol202.bolekgame.server;

import com.vdurmont.emoji.EmojiParser;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogout;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketReady;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketSpectate;
import pl.karol202.bolekgame.game.GameData;
import pl.karol202.bolekgame.Logic;
import pl.karol202.bolekgame.voice.VoiceService;

import java.util.List;

class ServerLogic extends Logic<ActivityServer>
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
		
		this.users = new Users(localUserName);
		users.addOnUsersUpdateListener(new Users.OnUsersUpdateListener() {
			@Override
			public void onUserAdd(User user) { }
			
			@Override
			public void onUserRemove(User user, int position) { }
			
			@Override
			public void onUsersUpdate()
			{
				ServerLogic.this.onUsersUpdate();
			}
		});
		
		this.textChat = new TextChat();
	}
	
	private void onUsersUpdate()
	{
		executeOnActivity(ActivityServer::onVoiceChatUpdate);
	}
	
	void startVoiceCommunication(VoiceService voiceService)
	{
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
	
	void spectate()
	{
		sendPacket(new OutputPacketSpectate());
	}
	
	void sendMessage(String message)
	{
		message = EmojiParser.removeAllEmojis(message);
		sendPacket(new OutputPacketMessage(message));
		textChat.addEntry(users.getLocalUserName(), message, true);
		executeOnActivity(a -> a.onTextChatUpdate(false));
	}
	
	String getTextChatString()
	{
		return textChat.getTextChatString();
	}
	
	String getLastChatEntryString()
	{
		return textChat.getLastEntryString();
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
		executeOnActivityInUIThread(ActivityServer::onDisconnect);
	}
	
	@Override
	public void onLoggedOut()
	{
		executeOnActivityInUIThread(ActivityServer::onLoggedOut);
	}
	
	@Override
	public void onFailure(int problem)
	{
		users.getLocalUser().setWaitingForSpectating(false);
		executeOnActivityInUIThread(ActivityServer::onError);
	}
	
	@Override
	public void onUsersUpdate(List<String> usernames, List<Boolean> readiness, List<String> addresses)
	{
		runInUIThread(() -> users.updateUsers(usernames, readiness, addresses));
	}
	
	@Override
	public void onServerStatusUpdate(boolean gameAvailable, int minUsers)
	{
		gameInProgress = !gameAvailable;
		users.setMinUsers(minUsers);
		runInUIThread(() -> users.onUsersUpdate());
	}
	
	@Override
	public void onMessage(String sender, String message, boolean newMessage)
	{
		textChat.addEntry(sender, message, newMessage);
		executeOnActivityInUIThread(a -> a.onTextChatUpdate(true));
	}
	
	@Override
	public void onGameStart(List<String> players, byte[] imagesCode)
	{
		suspendClient();
		executeOnActivityInUIThread(a -> a.onGameStart(imagesCode));
	}
	
	@Override
	public void onSpectatingStart(byte[] imagesCode)
	{
		suspendClient();
		users.getLocalUser().setWaitingForSpectating(false);
		executeOnActivityInUIThread(a -> a.onSpectatingStart(imagesCode));
	}
	
	GameData createGameData()
	{
		GameData data = new GameData(getClient(), serverName, serverCode);
		data.setUsers(users);
		data.setTextChat(textChat);
		return data;
	}
	
	boolean isConnected()
	{
		return getClient().isConnected();
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
