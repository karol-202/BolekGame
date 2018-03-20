package pl.karol202.bolekgame.voice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.game.players.Players;
import pl.karol202.bolekgame.server.LocalUser;
import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.Users;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

public class VoiceService extends Service
{
	static final int VOICE_PORT = 60607;
	
	private VoiceBinder binder;
	private VoiceRecorder voiceRecorder;
	private VoicePlayer voicePlayer;
	
	public VoiceService()
	{
		initVoiceComponents();
	}
	
	private void initVoiceComponents()
	{
		try
		{
			DatagramChannel channel = DatagramChannel.open();
			voiceRecorder = new VoiceRecorder(channel);
			voicePlayer = new VoicePlayer(channel);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Nullable
	@Override
	public Binder onBind(Intent intent)
	{
		if(binder != null) return binder;
		binder = new VoiceBinder(voiceRecorder != null && voicePlayer != null ? this : null);
		return binder;
	}
	
	public void setUsers(Users users)
	{
		setLocalUser(users.getLocalUser());
		removeAllUsers();
		users.getUsersStream().forEach(this::addUser);
		users.addOnUsersUpdateListener(new Users.OnUsersUpdateListener() {
			@Override
			public void onUserAdd(User user)
			{
				VoiceService.this.addUser(user);
			}
			
			@Override
			public void onUserRemove(User user, int position)
			{
				VoiceService.this.removeUser(user);
			}
			
			@Override
			public void onUsersUpdate() { }
		});
	}
	
	public void setUsers(Players players)
	{
		setLocalUser(players.getLocalUser());
		removeAllUsers();
		players.getUsersStream().forEach(this::addUser);
		players.addOnUsersUpdateListener(new Users.OnUsersUpdateListener() {
			@Override
			public void onUserAdd(User user)
			{
				VoiceService.this.addUser(user);
			}
			
			@Override
			public void onUserRemove(User user, int position)
			{
				VoiceService.this.removeUser(user);
			}
			
			@Override
			public void onUsersUpdate() { }
		});
	}
	
	private void addUser(User user)
	{
		if(!(user instanceof RemoteUser)) return;
		RemoteUser remoteUser = (RemoteUser) user;
		if(remoteUser.getAddress() == null) return;
		voiceRecorder.addUser(remoteUser);
		voicePlayer.addUser(remoteUser);
	}
	
	private void removeUser(User user)
	{
		if(!(user instanceof RemoteUser)) return;
		RemoteUser remoteUser = (RemoteUser) user;
		voiceRecorder.removeUser(remoteUser);
		voicePlayer.removeUser(remoteUser);
	}
	
	private void removeAllUsers()
	{
		voiceRecorder.removeAllUsers();
		voicePlayer.removeAllUsers();
	}
	
	private void setLocalUser(LocalUser localUser)
	{
		voiceRecorder.setLocalUser(localUser);
		voicePlayer.setLocalUser(localUser);
	}
	
	public void start()
	{
		new Thread(voiceRecorder).start();
		new Thread(voicePlayer).start();
	}
	
	public void stop()
	{
		voiceRecorder.stop();
		voicePlayer.stop();
	}
}
