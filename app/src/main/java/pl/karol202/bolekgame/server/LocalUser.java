package pl.karol202.bolekgame.server;

public class LocalUser extends User
{
	private boolean waitingForSpectating;
	private boolean microphoneEnabled;
	private boolean speakerEnabled;
	
	LocalUser(String name, boolean ready)
	{
		super(name, ready);
		waitingForSpectating = false;
		microphoneEnabled = true;
		speakerEnabled = true;
	}
	
	public boolean isWaitingForSpectating()
	{
		return waitingForSpectating;
	}
	
	public void setWaitingForSpectating(boolean waitingForSpectating)
	{
		this.waitingForSpectating = waitingForSpectating;
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
