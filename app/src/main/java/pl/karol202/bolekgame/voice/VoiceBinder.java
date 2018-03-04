package pl.karol202.bolekgame.voice;

import android.os.Binder;

public class VoiceBinder extends Binder
{
	private VoiceService voiceService;
	
	VoiceBinder(VoiceService voiceService)
	{
		this.voiceService = voiceService;
	}
	
	public VoiceService getVoiceService()
	{
		return voiceService;
	}
}