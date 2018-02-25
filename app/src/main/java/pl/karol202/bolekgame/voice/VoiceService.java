package pl.karol202.bolekgame.voice;

import android.app.Service;
import android.content.Intent;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.os.Binder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class VoiceService extends Service
{
	private VoiceBinder binder;
	
	private AudioGroup audioGroup;
	private Map<String, AudioStream> streams;
	
	public VoiceService()
	{
		audioGroup = new AudioGroup();
		streams = new HashMap<>();
	}
	
	@Nullable
	@Override
	public Binder onBind(Intent intent)
	{
		if(binder == null) binder = new VoiceBinder(this);
		return binder;
	}
	
	void addPeer(String address) throws IOException
	{
		InetAddress inetAddress = InetAddress.getByName(address);
		AudioStream audioStream = new AudioStream(inetAddress);
		audioStream.setCodec(AudioCodec.PCMA);
		audioStream.join(audioGroup);
		streams.put(address, audioStream);
	}
	
	void removePeer(String address)
	{
		streams.remove(address);
	}
	
	void clearPeers()
	{
		streams.clear();
	}
}
