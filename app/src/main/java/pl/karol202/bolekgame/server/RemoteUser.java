package pl.karol202.bolekgame.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteUser extends User
{
	private InetAddress address;
	private float volume;
	private boolean mute;
	
	RemoteUser(String name, boolean ready, String address)
	{
		super(name, ready);
		createInetAddress(address);
		volume = 1;
		mute = false;
	}
	
	private void createInetAddress(String address)
	{
		try
		{
			this.address = InetAddress.getByName(address);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
	
	public InetAddress getAddress()
	{
		return address;
	}

	public float getVolume()
	{
		return volume;
	}
	
	public void setVolume(float volume)
	{
		if(volume < 0 || volume > 1) return;
		this.volume = volume;
	}
	
	public boolean isMuted()
	{
		return mute;
	}
	
	public void setMute(boolean mute)
	{
		this.mute = mute;
	}
}
