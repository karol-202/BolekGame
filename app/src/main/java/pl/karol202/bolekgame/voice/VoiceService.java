package pl.karol202.bolekgame.voice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.Users;

import java.net.SocketException;

public class VoiceService extends Service
{
	static final int VOICE_PORT = 6007;
	
	private VoiceBinder binder;
	private VoiceRecorder voiceRecorder;
	private VoicePlayer voicePlayer;
	
	public VoiceService()
	{
		voiceRecorder = new VoiceRecorder();
		voicePlayer = new VoicePlayer();
	}
	
	@Nullable
	@Override
	public Binder onBind(Intent intent)
	{
		if(binder == null) binder = new VoiceBinder(this);
		return binder;
	}
	
	public void setUsers(Users users)
	{
		users.getUsersStream().forEach(this::onUserAdd);
		users.addOnUsersUpdateListener(new Users.OnUsersUpdateListener() {
			@Override
			public void onUserAdd(User user)
			{
				VoiceService.this.onUserAdd(user);
			}
			
			@Override
			public void onUserRemove(User user, int position)
			{
				VoiceService.this.onUserRemove(user);
			}
			
			@Override
			public void onUsersUpdate() { }
		});
	}
	
	private void onUserAdd(User user)
	{
		try
		{
			if(!(user instanceof RemoteUser)) return;
			RemoteUser remoteUser = (RemoteUser) user;
			if(remoteUser.getAddress() == null) return;
			voiceRecorder.addUser(remoteUser);
			voicePlayer.addUser(remoteUser);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	private void onUserRemove(User user)
	{
		if(!(user instanceof RemoteUser)) return;
		RemoteUser remoteUser = (RemoteUser) user;
		voiceRecorder.removeUser(remoteUser);
		voicePlayer.removeUser(remoteUser);
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
