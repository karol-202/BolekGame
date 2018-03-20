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
	static final int SAMPLE_SIZE = 2;//Bytes
	static final int SAMPLES_TO_SEND = 300;
	static final int BUFFER_SIZE = SAMPLES_TO_SEND * SAMPLE_SIZE;
	
	private AudioRecord audioRecord;
	private DatagramChannel channel;
	private LocalUser localUser;
	private Map<RemoteUser, InetSocketAddress> users;
	private ByteBuffer buffer;
	private boolean run;
	
	VoiceRecorder(DatagramChannel channel)
	{
		this.channel = channel;
		users = new HashMap<>();
		buffer = ByteBuffer.allocate(BUFFER_SIZE);
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
			channel.close();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	private void createAudioRecord()
	{
		int bufferSize = 4 * AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
		
		if(audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED)
			Crashlytics.log(Log.ERROR, "BolekGame", "Cannot initialize AudioRecord.");
	}
	
	private void doWork() throws IOException, InterruptedException
	{
		checkState();
		if(localUser.isMicrophoneEnabled())
		{
			record();
			for(Map.Entry<RemoteUser, InetSocketAddress> entry : users.entrySet())
				sendSamplesToUser(entry.getKey(), entry.getValue());
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
	
	private void record() throws IOException
	{
		audioRecord.read(buffer, BUFFER_SIZE);
	}
	
	private void sendSamplesToUser(RemoteUser user, InetSocketAddress address) throws IOException
	{
		channel.send(buffer, address);
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
