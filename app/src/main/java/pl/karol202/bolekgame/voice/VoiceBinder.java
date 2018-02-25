package pl.karol202.bolekgame.voice;

import android.os.Binder;

import java.io.IOException;

public class VoiceBinder extends Binder
{
	private VoiceService voiceService;
	
	VoiceBinder(VoiceService voiceService)
	{
		this.voiceService = voiceService;
	}
	
	public void addPeer(String address)
	{
		try
		{
			voiceService.addPeer(address);
		}
		catch(IOException ignored)
		{
			System.err.println("Cannot connect to peer.");
		}
	}
	
	public void removePeer(String address)
	{
		voiceService.removePeer(address);
	}
	
	public void clearPeers()
	{
		voiceService.clearPeers();
	}
}