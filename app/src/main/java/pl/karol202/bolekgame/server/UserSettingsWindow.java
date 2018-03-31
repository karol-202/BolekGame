package pl.karol202.bolekgame.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class UserSettingsWindow
{
	private Context context;
	private View parentView;
	private RemoteUser user;
	
	private PopupWindow window;
	private View panelMute;
	private ImageView imageMute;
	private TextView textMute;
	private View panelVolume;
	private SeekBar seekBarVolume;
	private TextView textVolume;
	
	public UserSettingsWindow(View parentView)
	{
		this.context = parentView.getContext();
		this.parentView = parentView;
		initWindow();
	}
	
	@SuppressLint("InflateParams")
	private void initWindow()
	{
		window = new PopupWindow(parentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		window.setOutsideTouchable(true);
		window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_user_settings_popup));
		
		//TODO Add border
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
		imageMute.setImageResource(user.isMuted() ? R.drawable.ic_speaker_on_black_24dp : R.drawable.ic_speaker_off_black_24dp);
		textMute.setText(user.isMuted() ? R.string.text_user_unmute : R.string.text_user_mute);
	}
	
	private void initVolumePanel(View view)
	{
		panelVolume = view.findViewById(R.id.panel_user_volume);
		
		seekBarVolume = panelVolume.findViewById(R.id.seekBar_user_volume);
		seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
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
	
	public void show(View anchor)
	{
		if(user == null || window.isShowing()) return;
		setUser(user);
		window.showAsDropDown(anchor);
	}
	
	public void dismiss()
	{
		window.dismiss();
	}
	
	public boolean isShowing()
	{
		return window.isShowing();
	}
	
	public RemoteUser getUser()
	{
		return user;
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
