package pl.karol202.bolekgame.voice;

import pl.karol202.bolekgame.server.RemoteUser;
import pl.karol202.bolekgame.server.User;
import pl.karol202.bolekgame.server.Users;

public class UsersVoiceHandler implements Users.OnUsersUpdateListener
{
	private VoiceBinderSupplier voiceBinderSupplier;
	
	public UsersVoiceHandler(VoiceBinderSupplier voiceBinderSupplier)
	{
		this.voiceBinderSupplier = voiceBinderSupplier;
	}
	
	@Override
	public void onUserAdd(User user)
	{
		VoiceBinder voiceBinder = voiceBinderSupplier.getVoiceBinder();
		if(voiceBinder == null || !(user instanceof RemoteUser)) return;
		RemoteUser remoteUser = (RemoteUser) user;
		voiceBinder.addPeer(remoteUser.getAddress());
	}
	
	@Override
	public void onUserRemove(User user, int position)
	{
		VoiceBinder voiceBinder = voiceBinderSupplier.getVoiceBinder();
		if(voiceBinder == null || !(user instanceof RemoteUser)) return;
		RemoteUser remoteUser = (RemoteUser) user;
		voiceBinder.removePeer(remoteUser.getAddress());
	}
	
	@Override
	public void onUsersUpdate() { }
}
