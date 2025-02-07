package pl.karol202.bolekgame.server;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import pl.karol202.bolekgame.*;
import pl.karol202.bolekgame.control.ActivityHelp;
import pl.karol202.bolekgame.game.ActivityGame;
import pl.karol202.bolekgame.game.GameData;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.view.AnimatedImageButton;
import pl.karol202.bolekgame.view.ItemDivider;

public class ActivityServer extends PermissionGrantingActivity
{
	private class UserListenerImpl implements UsersAdapter.UserListener
	{
		@Override
		public void onUserReady()
		{
			serverLogic.setReady();
		}
		
		@Override
		public void onUserSpectate()
		{
			serverLogic.spectate();
		}
	}
	
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private View coordinatorLayout;
	private TextView textServerName;
	private TextView textServerCode;
	private ImageButton buttonHelp;
	private RecyclerView recyclerUsers;
	private View textChatLayout;
	private AnimatedImageButton buttonTextChatToggle;
	private ImageButton buttonVoiceChatMicrophone;
	private ImageButton buttonVoiceChatSpeaker;
	private TextView textChatMessage;
	private TextView textChat;
	private EditText editTextChat;
	private ImageButton buttonTextChatSend;
	
	private UsersAdapter usersAdapter;
	private BottomSheetBehavior textChatLayoutBehaviour;
	private boolean textChatLayoutStateChangeByClick;
	private ViewTreeObserver.OnGlobalLayoutListener layoutListener;
	private boolean hidingTextChatLayout;
	
	private FragmentRetain<ServerLogic> fragmentRetain;
	private ServerLogic serverLogic;
	private BolekApplication bolekApplication;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		loadServerData();
		restoreRetainFragment();
		bolekApplication = (BolekApplication) getApplication();
		
		usersAdapter = new UsersAdapter(this, serverLogic.getUsers(), () -> serverLogic.isGameInProgress(), new UserListenerImpl());
		layoutListener = this::onLayoutUpdate;
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		coordinatorLayout.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
		
		textServerName = findViewById(R.id.text_server_name_value);
		textServerName.setText(serverLogic.getServerName());
		
		textServerCode = findViewById(R.id.text_server_code_value);
		textServerCode.setText(String.valueOf(serverLogic.getServerCode()));
		
		buttonHelp = findViewById(R.id.button_help);
		buttonHelp.setOnClickListener(v -> showHelp());
		
		recyclerUsers = findViewById(R.id.recycler_users);
		recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
		recyclerUsers.setAdapter(usersAdapter);
		recyclerUsers.addItemDecoration(new ItemDivider(this));
		
		textChatLayout = findViewById(R.id.panel_text_chat);
		textChatLayoutBehaviour = BottomSheetBehavior.from(textChatLayout);
		textChatLayoutBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
		{
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int state)
			{
				onTextChatLayoutStateChanged(state);
			}
			
			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
		});
		
		buttonTextChatToggle = findViewById(R.id.button_text_chat_toggle);
		buttonTextChatToggle.setOnClickListener(v -> toggleTextChatLayout());
		
		buttonVoiceChatMicrophone = findViewById(R.id.button_voice_chat_microphone);
		buttonVoiceChatMicrophone.setVisibility(Settings.isVoiceChatEnabled(this) ? View.VISIBLE : View.GONE);
		buttonVoiceChatMicrophone.setOnClickListener(v -> toggleVoiceChatMicrophone());
		
		buttonVoiceChatSpeaker = findViewById(R.id.button_voice_chat_speaker);
		buttonVoiceChatSpeaker.setVisibility(Settings.isVoiceChatEnabled(this) ? View.VISIBLE : View.GONE);
		buttonVoiceChatSpeaker.setOnClickListener(v -> toggleVoiceChatSpeaker());
		
		textChatMessage = findViewById(R.id.text_chat_message);
		textChatMessage.setText(null);
		
		textChat = findViewById(R.id.text_chat);
		onTextChatUpdate(false);
		
		editTextChat = findViewById(R.id.editText_chat);
		editTextChat.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
			@Override
			public void afterTextChanged(Editable s)
			{
				onMessageEdit(s.toString());
			}
		});
		editTextChat.setOnEditorActionListener((view, actionId, event) -> onMessageEditDone(actionId));
		
		buttonTextChatSend = findViewById(R.id.button_text_chat_send);
		buttonTextChatSend.setOnClickListener(v -> sendTextChatMessage());
		buttonTextChatSend.setEnabled(false);
		
		tryToStartVoiceCommunication();
	}
	
	private void loadServerData()
	{
		ServerData serverData = ServerData.getServerData();
		if(serverData != null && serverData.getClient() != null)
			serverLogic = new ServerLogic(serverData.getClient(), serverData.getServerName(),
										  serverData.getServerCode(), Settings.getNick(this));
	}
	
	@SuppressWarnings("unchecked")
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
		else serverLogic = fragmentRetain.getLogic();
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		fragmentRetain = new FragmentRetain<>();
		fragmentRetain.setLogic(serverLogic);
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
	}
	
	private void tryToStartVoiceCommunication()
	{
		if(!Settings.isVoiceChatEnabled(this)) return;
		bolekApplication.bindToVoiceService(service -> {
			if(service != null)
				PermissionRequest.requestPermission(this, Manifest.permission.RECORD_AUDIO, () -> serverLogic.startVoiceCommunication(service));
			else Toast.makeText(ActivityServer.this, R.string.message_voice_chat_network_error, Toast.LENGTH_LONG).show();
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		serverLogic.resume(this); //On orientation changes and on back from other activity
		onTextChatUpdate(false);
		if(!serverLogic.isConnected()) finish();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		editTextChat.setOnEditorActionListener(null);
		removeLayoutListener();
		usersAdapter.onDestroy();
		
		if(!isFinishing()) serverLogic.suspend(); //On configuration changes
		else bolekApplication.unbindFromVoiceService();
	}
	
	private void removeLayoutListener()
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		coordinatorLayout.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
	}
	
	@Override
	public void onBackPressed()
	{
		serverLogic.logout();
		super.onBackPressed();
	}
	
	private void showHelp()
	{
		Intent intent = new Intent(this, ActivityHelp.class);
		startActivity(intent);
	}
	
	private void toggleTextChatLayout()
	{
		if(textChatLayoutBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
		{
			hidingTextChatLayout = false;
			
			textChatLayoutBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
			buttonTextChatToggle.toggleAnimation(true);
			buttonTextChatToggle.setContentDescription(getString(R.string.acc_text_chat_hide));
			textChatLayoutStateChangeByClick = true;
			
			textChatMessage.setText(null);
		}
		else if(textChatLayoutBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED)
		{
			hidingTextChatLayout = true;
			hideKeyboard(editTextChat);
			
			textChatLayoutBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
			buttonTextChatToggle.toggleAnimation(false);
			buttonTextChatToggle.setContentDescription(getString(R.string.acc_text_chat_show));
			textChatLayoutStateChangeByClick = true;
		}
	}
	
	private void onTextChatLayoutStateChanged(int state)
	{
		if(textChatLayoutStateChangeByClick) textChatLayoutStateChangeByClick = false;
		else if(state == BottomSheetBehavior.STATE_COLLAPSED)
		{
			buttonTextChatToggle.toggleAnimation(false);
			buttonTextChatToggle.setContentDescription(getString(R.string.acc_text_chat_show));
		}
		else if(state == BottomSheetBehavior.STATE_EXPANDED)
		{
			buttonTextChatToggle.toggleAnimation(true);
			buttonTextChatToggle.setContentDescription(getString(R.string.acc_text_chat_hide));
			textChatMessage.setText(null);
		}
	}
	
	private void toggleVoiceChatMicrophone()
	{
		boolean enable = !serverLogic.isMicrophoneEnabled();
		serverLogic.setMicrophoneEnabled(enable);
		buttonVoiceChatMicrophone.setImageResource(enable ? R.drawable.ic_microphone_off_black_24dp : R.drawable.ic_microphone_on_black_24dp);
		buttonVoiceChatMicrophone.setContentDescription(getString(enable ? R.string.acc_voice_chat_microphone_mute : R.string.acc_voice_chat_microphone_unmute));
	}
	
	private void toggleVoiceChatSpeaker()
	{
		boolean enable = !serverLogic.isSpeakerEnabled();
		serverLogic.setSpeakerEnabled(enable);
		buttonVoiceChatSpeaker.setImageResource(enable ? R.drawable.ic_speaker_off_black_24dp : R.drawable.ic_speaker_on_black_24dp);
		buttonVoiceChatSpeaker.setContentDescription(getString(enable ? R.string.acc_voice_chat_speaker_mute : R.string.acc_voice_chat_speaker_unmute));
	}
	
	void onTextChatUpdate(boolean notification)
	{
		textChat.setText(serverLogic.getTextChatString());
		
		if(notification && textChatLayoutBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
			textChatMessage.setText(serverLogic.getLastChatEntryString());
	}
	
	private void onMessageEdit(String message)
	{
		buttonTextChatSend.setEnabled(message != null && !message.isEmpty());
	}
	
	private boolean onMessageEditDone(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		sendTextChatMessage();
		return true;
	}
	
	private void sendTextChatMessage()
	{
		String message = editTextChat.getText().toString();
		if(message.isEmpty()) return;
		
		serverLogic.sendMessage(message);
		editTextChat.setText(null);
		textChatMessage.setText(null);
		hideKeyboard(editTextChat);
	}
	
	private void hideKeyboard(TextView v)
	{
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manager != null) manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	private void onLayoutUpdate()
	{
		if(hidingTextChatLayout) textChatLayoutBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
		hidingTextChatLayout = false;
	}
	
	public void onVoiceChatUpdate()
	{
		buttonVoiceChatMicrophone.setImageResource(serverLogic.isMicrophoneEnabled() ? R.drawable.ic_microphone_off_black_24dp :
																					   R.drawable.ic_microphone_on_black_24dp);
		buttonVoiceChatSpeaker.setImageResource(serverLogic.isSpeakerEnabled() ? R.drawable.ic_speaker_off_black_24dp :
																				 R.drawable.ic_speaker_on_black_24dp);
	}
	
	void onDisconnect()
	{
		showDisconnectionDialog();
	}
	
	private void showDisconnectionDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_disconnection);
		builder.setMessage(R.string.dialog_disconnection_detail);
		builder.setPositiveButton(R.string.button_ok, (d, w) -> finish());
		builder.setCancelable(false);
		builder.show();
	}
	
	void onLoggedOut()
	{
		finish();
	}
	
	void onError()
	{
		Snackbar.make(coordinatorLayout, R.string.message_error, Snackbar.LENGTH_LONG).show();
	}
	
	void onGameStart(byte[] imagesCode)
	{
		serverLogic.suspend();
		
		GameData data = serverLogic.createGameData();
		data.setImagesCode(imagesCode);
		GameData.setGameData(data);
		
		Intent intent = new Intent(this, ActivityGame.class);
		startActivity(intent);
	}
	
	void onSpectatingStart(byte[] imagesCode)
	{
		serverLogic.suspend();
		
		GameData data = serverLogic.createGameData();
		data.setSpectating(true);
		data.setImagesCode(imagesCode);
		GameData.setGameData(data);
		
		Intent intent = new Intent(this, ActivityGame.class);
		startActivity(intent);
	}
}
