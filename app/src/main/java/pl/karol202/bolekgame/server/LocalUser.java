package pl.karol202.bolekgame.server;

public class LocalUser extends User
{
	private boolean microphoneEnabled;
	private boolean speakerEnabled;
	
	LocalUser(String name, boolean ready)
	{
		super(name, ready);
	}
	
	public boolean isMicrophoneEnabled()
	{
		return microphoneEnabled;
	}
	
	public void setMicrophoneEnabled(boolean microphoneEnabled)
	{
		this.microphoneEnabled = microphoneEnabled;
	}
	
	public boolean isSpeakerEnabled()
	{
		return speakerEnabled;
	}
	
	public void setSpeakerEnabled(boolean speakerEnabled)
	{
		this.speakerEnabled = speakerEnabled;
	}
}
