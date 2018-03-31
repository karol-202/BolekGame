package pl.karol202.bolekgame;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.squareup.leakcanary.LeakCanary;
import pl.karol202.bolekgame.voice.VoiceBinder;
import pl.karol202.bolekgame.voice.VoiceService;

import java.util.ArrayList;
import java.util.List;

public class BolekApplication extends Application
{
	public interface VoiceServiceBindListener
	{
		void onVoiceServiceBind(VoiceService voiceService);
	}
	
	private class VoiceServiceConnection implements ServiceConnection
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			onVoiceServiceBind(service);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) { }
	}
	
	private ServiceConnection serviceConnection;
	private VoiceBinder voiceBinder;
	private int boundActivities;
	private List<VoiceServiceBindListener> voiceServiceBindListeners;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		voiceServiceBindListeners = new ArrayList<>();
		
		installLeakCanary();
	}
	
	private void installLeakCanary()
	{
		if(LeakCanary.isInAnalyzerProcess(this)) return;
		LeakCanary.install(this);
	}
	
	private void bindVoiceService()
	{
		if(serviceConnection != null) return;
		serviceConnection = new VoiceServiceConnection();
		Intent intent = new Intent(this, VoiceService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}
	
	private void unbindVoiceService()
	{
		if(voiceBinder == null) return;
		VoiceService voiceService = voiceBinder.getVoiceService();
		if(voiceService != null) voiceService.stop();
		voiceBinder = null;
		
		unbindService(serviceConnection);
		serviceConnection = null;
	}
	
	private void onVoiceServiceBind(IBinder binder)
	{
		voiceBinder = (VoiceBinder) binder;
		for(VoiceServiceBindListener listener : voiceServiceBindListeners)
			listener.onVoiceServiceBind(voiceBinder.getVoiceService());
		voiceServiceBindListeners.clear();
	}
	
	public void bindToVoiceService(VoiceServiceBindListener listener)
	{
		boundActivities++;
		if(voiceBinder == null)
		{
			voiceServiceBindListeners.add(listener);
			bindVoiceService();
			
		}
		else listener.onVoiceServiceBind(voiceBinder.getVoiceService());
	}
	
	public void unbindFromVoiceService()
	{
		if(--boundActivities == 0) unbindVoiceService();
		voiceServiceBindListeners.clear();
	}
}
