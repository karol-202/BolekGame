package pl.karol202.bolekgame.voice;

import android.media.*;
import android.os.Build;
import android.support.annotation.RequiresApi;
import pl.karol202.bolekgame.server.RemoteUser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

class VoicePlayer implements Runnable
{
	private AudioTrack audioTrack;
	private DatagramSocket socket;
	private List<RemoteUser> users;
	private byte[] buffer;
	private boolean run;
	
	VoicePlayer()
	{
		createAudioRecord();
		createSocket();
		users = new ArrayList<>();
		buffer = new byte[VoiceRecorder.BUFFER_SIZE];
		run = true;
	}
	
	private void createAudioRecord()
	{
		int bufferSize = 10 * AudioRecord.getMinBufferSize(VoiceRecorder.SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) createAudioRecordLollipop(bufferSize);
		else createAudioRecordOlder(bufferSize);
	}
	
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void createAudioRecordLollipop(int bufferSize)
	{
		AudioAttributes.Builder aaBuilder = new AudioAttributes.Builder();
		aaBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SPEECH);
		aaBuilder.setUsage(AudioAttributes.USAGE_GAME);
		AudioAttributes audioAttributes = aaBuilder.build();
		
		AudioFormat.Builder afBuilder = new AudioFormat.Builder();
		afBuilder.setSampleRate(VoiceRecorder.SAMPLE_RATE);
		afBuilder.setEncoding(AudioFormat.ENCODING_PCM_16BIT);
		afBuilder.setChannelMask(AudioFormat.CHANNEL_OUT_MONO);
		AudioFormat audioFormat = afBuilder.build();
		
		audioTrack = new AudioTrack(audioAttributes, audioFormat, bufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);
	}
	
	private void createAudioRecordOlder(int bufferSize)
	{
		audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, VoiceRecorder.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
									AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
	}
	
	private void createSocket()
	{
		try
		{
			DatagramChannel channel = DatagramChannel.open();
			socket = channel.socket();
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(VoiceService.VOICE_PORT));
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		audioTrack.play();
		try
		{
			while(run)
			{
				System.out.println("before receive");
				DatagramPacket packet = new DatagramPacket(buffer, VoiceRecorder.BUFFER_SIZE);
				socket.receive(packet);
				System.out.println("receive");
				for(int i = 0; i < buffer.length; i++) System.out.println(buffer[i]);
				audioTrack.write(buffer, 0, VoiceRecorder.BUFFER_SIZE);
			}
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	void addUser(RemoteUser user) throws SocketException
	{
		users.add(user);
	}
	
	void removeUser(RemoteUser user)
	{
		users.remove(user);
	}
	
	void stop()
	{
		run = false;
	}
}
