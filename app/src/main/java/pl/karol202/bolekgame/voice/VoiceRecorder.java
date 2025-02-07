package pl.karol202.bolekgame.voice;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import pl.karol202.bolekgame.server.LocalUser;
import pl.karol202.bolekgame.server.RemoteUser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

class VoiceRecorder implements Runnable
{
	static final int SAMPLE_RATE = 8000;//Hz
	static final int BUFFER_SIZE_SHORTS = 1000;
	static final int BUFFER_SIZE_BYTES = 2 * BUFFER_SIZE_SHORTS;
	
	private AudioRecord audioRecord;
	private DatagramChannel channel;
	private LocalUser localUser;
	private Map<RemoteUser, InetSocketAddress> users;
	private short[] shortArray;
	private ByteBuffer byteBuffer;
	private boolean run;
	
	VoiceRecorder(DatagramChannel channel)
	{
		this.channel = channel;
		users = new HashMap<>();
		shortArray = new short[BUFFER_SIZE_SHORTS];
		byteBuffer = ByteBuffer.allocate(BUFFER_SIZE_BYTES);
	}
	
	@Override
	public void run()
	{
		if(run) return;
		run = true;
		
		try
		{
			Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
			
			createAudioRecord();
			if(audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) return;
			
			while(run) doWork();
			
			audioRecord.stop();
			audioRecord.release();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			run = false;
		}
	}
	
	private void createAudioRecord()
	{
		int bufferSize = 10 * AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
									  AudioFormat.ENCODING_PCM_16BIT, bufferSize);
		
		if(audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED)
			Crashlytics.log(Log.ERROR, "BolekGame", "Cannot initialize AudioRecord.");
	}
	
	private void doWork() throws IOException, InterruptedException
	{
		checkState();
		if(localUser.isMicrophoneEnabled())
		{
			record();
			for(InetSocketAddress address : users.values())
				sendSamplesToUser(address);
		}
		else Thread.sleep(10);
	}
	
	private void checkState()
	{
		if(localUser.isMicrophoneEnabled() && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED)
			audioRecord.startRecording();
		else if(!localUser.isMicrophoneEnabled() && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
			audioRecord.stop();
	}
	
	private void record()
	{
		audioRecord.read(shortArray, 0, BUFFER_SIZE_SHORTS);
		byteBuffer.clear();
		for(short s : shortArray)
		{
			byteBuffer.put((byte) (s & 0xFF));
			byteBuffer.put((byte) (s >> 8));
		}
		byteBuffer.flip();
	}
	
	private void sendSamplesToUser(InetSocketAddress address) throws IOException
	{
		channel.send(byteBuffer, address);
	}
	
	void addUser(RemoteUser user)
	{
		users.put(user, new InetSocketAddress(user.getAddress(), VoiceService.VOICE_PORT));
	}
	
	void removeUser(RemoteUser user)
	{
		users.remove(user);
	}
	
	void removeAllUsers()
	{
		users.clear();
	}
	
	void setLocalUser(LocalUser localUser)
	{
		this.localUser = localUser;
	}
	
	void stop()
	{
		run = false;
	}
}
