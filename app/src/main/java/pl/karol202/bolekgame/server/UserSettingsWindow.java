package pl.karol202.bolekgame.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class UserSettingsWindow
{
	private Context context;
	private RemoteUser user;
	
	private PopupWindow window;
	private View panelMute;
	private ImageView imageMute;
	private TextView textMute;
	private View panelVolume;
	private SeekBar seekBarVolume;
	private TextView textVolume;
	
	public UserSettingsWindow(Context context)
	{
		this.context = context;
		initWindow();
	}
	
	@SuppressLint("InflateParams")
	private void initWindow()
	{
		window = new PopupWindow(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.popup_user_settings, null);
		initMutePanel(view);
		initVolumePanel(view);
		
		window.setContentView(view);
	}
	
	private void initMutePanel(View view)
	{
		panelMute = view.findViewById(R.id.panel_user_mute);
		panelMute.setOnClickListener(v -> toggleUserMute(user));
		
		imageMute = panelMute.findViewById(R.id.image_user_mute);
		
		textMute = panelMute.findViewById(R.id.text_user_mute);
	}
	
	private void toggleUserMute(RemoteUser user)
	{
		user.setMute(!user.isMuted());
	}
	
	private void initVolumePanel(View view)
	{
		panelVolume = view.findViewById(R.id.panel_user_volume);
		
		seekBarVolume = panelVolume.findViewById(R.id.seekBar_user_volume);
		seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				changeUserVolume(progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
		});
		
		textVolume = panelVolume.findViewById(R.id.text_user_volume);
	}
	
	private void changeUserVolume(int seekBarValue)
	{
		user.setVolume(seekBarValue / 100f);
		textVolume.setText(String.format("%d%%", seekBarValue));
	}
	
	public void showSettingsWindow(View anchor)
	{
		if(user == null) return;
		window.showAsDropDown(anchor);
	}
	
	public void setUser(RemoteUser user)
	{
		this.user = user;
		
		imageMute.setImageResource(user.isMuted() ? R.drawable.ic_speaker_on_black_24dp : R.drawable.ic_speaker_off_black_24dp);
		textMute.setText(user.isMuted() ? R.string.text_user_unmute : R.string.text_user_mute);
		
		seekBarVolume.setProgress(Math.round(user.getVolume() * 100));
		textVolume.setText(String.format("%d%%", Math.round(user.getVolume() * 100)));
	}
}
