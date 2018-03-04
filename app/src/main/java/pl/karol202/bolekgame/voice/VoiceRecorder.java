package pl.karol202.bolekgame.voice;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import pl.karol202.bolekgame.server.RemoteUser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

class VoiceRecorder implements Runnable
{
	static final int SAMPLE_RATE = 8000;//Hz
	static final int SAMPLE_SIZE = 2;//Bytes
	static final int SAMPLES_TO_SEND = 100;
	static final int BUFFER_SIZE = SAMPLES_TO_SEND * SAMPLE_SIZE;
	
	private AudioRecord audioRecord;
	private DatagramSocket socket;
	private List<RemoteUser> users;
	private byte[] buffer;
	private boolean run;
	
	VoiceRecorder()
	{
		createAudioRecord();
		createSocket();
		users = new ArrayList<>();
		buffer = new byte[BUFFER_SIZE];
		run = true;
	}
	
	private void createAudioRecord()
	{
		int bufferSize = 10 * AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
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
		audioRecord.startRecording();
		while(run)
		{
			audioRecord.read(buffer, 0, BUFFER_SIZE);
			sendSamplesToAll();
		}
	}
	
	private void sendSamplesToAll()
	{
		try
		{
			for(RemoteUser user : users) sendSamplesToUser(user);
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private void sendSamplesToUser(RemoteUser user) throws IOException
	{
		DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE, user.getAddress(), VoiceService.VOICE_PORT);
		socket.send(packet);
		System.out.println("send to: " + user.getAddress());
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
