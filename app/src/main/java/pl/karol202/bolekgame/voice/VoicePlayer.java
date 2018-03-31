package pl.karol202.bolekgame.voice;

import android.media.*;
import android.os.Build;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import pl.karol202.bolekgame.server.LocalUser;
import pl.karol202.bolekgame.server.RemoteUser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

class VoicePlayer implements Runnable
{
	private AudioTrack audioTrack;
	private DatagramChannel channel;
	private LocalUser localUser;
	private Map<RemoteUser, InetSocketAddress> users;
	private Map<RemoteUser, short[]> userBuffers;
	
	private boolean run;
	private ByteBuffer temporaryBuffer;
	private byte[] buffer;
	
	VoicePlayer(DatagramChannel channel)
	{
		this.channel = channel;
		users = new HashMap<>();
		userBuffers = new HashMap<>();
		
		temporaryBuffer = ByteBuffer.allocate(VoiceRecorder.BUFFER_SIZE_BYTES);
		buffer = new byte[VoiceRecorder.BUFFER_SIZE_BYTES];
	}
	
	@Override
	public void run()
	{
		if(run) return;
		run = true;
		
		try
		{
			Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
			
			createAudioTrack();
			if(audioTrack.getState() == AudioRecord.STATE_UNINITIALIZED) return;
			
			while(run) doWork();
			
			audioTrack.release();
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
	
	private void createAudioTrack()
	{
		int bufferSize = 4 * AudioTrack.getMinBufferSize(VoiceRecorder.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) createAudioTrackLollipop(bufferSize);
		else createAudioTrackOlder(bufferSize);
		
		if(audioTrack.getState() == AudioRecord.STATE_UNINITIALIZED)
			Crashlytics.log(Log.ERROR, "BolekGame", "Cannot initialize AudioTrack.");
	}
	
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void createAudioTrackLollipop(int bufferSize)
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
	
	private void createAudioTrackOlder(int bufferSize)
	{
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, VoiceRecorder.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
	}
	
	private void doWork() throws InterruptedException, IOException
	{
		checkState();
		if(localUser.isSpeakerEnabled())
		{
			receiveSamples();
			//mixSamples();
			clearSamples();
			play();
		}
		else Thread.sleep(10);
	}
	
	private void checkState()
	{
		if(localUser.isSpeakerEnabled() && audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
			audioTrack.play();
		else if(!localUser.isSpeakerEnabled() && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
		{
			audioTrack.pause();
			audioTrack.flush();
		}
	}
	
	private void receiveSamples() throws IOException
	{
		SocketAddress address;
		while((address = channel.receive(temporaryBuffer)) != null)
		{
			RemoteUser user = findUser(address);
			//receiveUserSamples(user, temporaryBuffer);
			buffer = temporaryBuffer.array();
			temporaryBuffer.clear();
		}
	}
	
	private RemoteUser findUser(SocketAddress address)
	{
		for(Map.Entry<RemoteUser, InetSocketAddress> entry : users.entrySet())
			if(entry.getValue().equals(address)) return entry.getKey();
		return null;
	}
	
	private void receiveUserSamples(RemoteUser user, ByteBuffer buffer)
	{
		if(user == null) return;
		buffer.flip();
		short[] userBuffer = userBuffers.get(user);
		for(int i = 0; i < buffer.limit() / 2; i++)
		{
			if(user.isMuted()) userBuffer[i] = 0;
			else
			{
				byte low = buffer.get();
				byte high = buffer.get();
				int sample = high << 8 | low;
				userBuffer[i] = (short) Math.round(sample * user.getVolume());
			}
		}
	}
	
	private void mixSamples()
	{
		for(int i = 0; i < buffer.length; i++)
		{
			short sum = 0;
			for(short[] samples : userBuffers.values()) sum += samples[i];
			float sampleFloat = (float) Math.tanh(sum / 32768f);
			//buffer[i] = (short) (sampleFloat * 32768);
		}
	}
	
	private void clearSamples()
	{
		for(short[] samples : userBuffers.values())
		{
			for(int i = 0; i < samples.length; i++) samples[i] = 0;
		}
	}
	
	private void play()
	{
		audioTrack.write(buffer, 0, VoiceRecorder.BUFFER_SIZE_BYTES);
	}
	
	void addUser(RemoteUser user)
	{
		users.put(user, new InetSocketAddress(user.getAddress(), VoiceService.VOICE_PORT));
		userBuffers.put(user, new short[VoiceRecorder.BUFFER_SIZE_SHORTS]);
	}
	
	void removeUser(RemoteUser user)
	{
		users.remove(user);
		userBuffers.remove(user);
	}
	
	void removeAllUsers()
	{
		users.clear();
		userBuffers.clear();
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
