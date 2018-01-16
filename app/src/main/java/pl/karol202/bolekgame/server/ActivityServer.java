package pl.karol202.bolekgame.server;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.ActivityGame;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.AnimatedImageButton;
import pl.karol202.bolekgame.utils.FragmentRetain;
import pl.karol202.bolekgame.utils.ItemDivider;

public class ActivityServer extends AppCompatActivity
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private View coordinatorLayout;
	private TextView textServerName;
	private TextView textServerCode;
	private RecyclerView recyclerUsers;
	private View textChatLayout;
	private AnimatedImageButton buttonTextChatToggle;
	private TextView textChat;
	private EditText editTextChat;
	private ImageButton buttonTextChatSend;
	
	private UsersAdapter usersAdapter;
	private BottomSheetBehavior textChatLayoutBehaviour;
	private boolean textChatLayoutStateChangeByClick;
	
	private FragmentRetain<ServerLogic> fragmentRetain;
	private ServerLogic serverLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		loadServerData();
		restoreRetainFragment();
		
		usersAdapter = new UsersAdapter(this, serverLogic.getUsers());
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		
		textServerName = findViewById(R.id.text_server_name_value);
		textServerName.setText(serverLogic.getServerName());
		
		textServerCode = findViewById(R.id.text_server_code_value);
		textServerCode.setText(String.valueOf(serverLogic.getServerCode()));
		
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
		buttonTextChatToggle.setOnClickListener(v -> toggleTextChat());
		
		textChat = findViewById(R.id.text_chat);
		textChat.setMovementMethod(new ScrollingMovementMethod());
		onTextChatUpdate();
		
		editTextChat = findViewById(R.id.editText_chat);
		
		buttonTextChatSend = findViewById(R.id.button_text_chat_send);
		buttonTextChatSend.setOnClickListener(v -> sendTextChatMessage());
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
	
	@Override
	protected void onResume()
	{
		super.onResume();
		serverLogic.setActivity(this);
		serverLogic.resumeClient();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		serverLogic.suspendClient();
		serverLogic.setActivity(null);
	}
	
	@Override
	public void onBackPressed()
	{
		serverLogic.logout();
		super.onBackPressed();
	}
	
	private void toggleTextChat()
	{
		if(textChatLayoutBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
		{
			textChatLayoutBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
			buttonTextChatToggle.toggleAnimation(true);
			textChatLayoutStateChangeByClick = true;
		}
		else if(textChatLayoutBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED)
		{
			textChatLayoutBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
			buttonTextChatToggle.toggleAnimation(false);
			textChatLayoutStateChangeByClick = true;
		}
	}
	
	private void onTextChatLayoutStateChanged(int state)
	{
		if(textChatLayoutStateChangeByClick) textChatLayoutStateChangeByClick = false;
		else if(state == BottomSheetBehavior.STATE_COLLAPSED) buttonTextChatToggle.toggleAnimation(false);
		else if(state == BottomSheetBehavior.STATE_EXPANDED) buttonTextChatToggle.toggleAnimation(true);
	}
	
	void onTextChatUpdate()
	{
		textChat.setText(serverLogic.getTextChatString());
	}
	
	void sendTextChatMessage()
	{
		String message = editTextChat.getText().toString();
		if(message.isEmpty()) return;
		serverLogic.sendMessage(message);
		editTextChat.setText(null);
	}
	
	void onDisconnect()
	{
		showDisconnectionDialog();
	}
	
	private void showDisconnectionDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_disconnection);
		builder.setMessage(R.string.message_disconnection);
		builder.setPositiveButton(R.string.ok, (d, w) -> finish());
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
	
	void onGameStart()
	{
		Intent intent = new Intent(this, ActivityGame.class);
		startActivity(intent);
	}
}
