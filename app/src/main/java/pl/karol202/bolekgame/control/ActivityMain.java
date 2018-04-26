package pl.karol202.bolekgame.control;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import pl.karol202.bolekgame.FragmentRetain;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.server.ActivityServer;
import pl.karol202.bolekgame.server.ServerData;
import pl.karol202.bolekgame.settings.ActivitySettings;
import pl.karol202.bolekgame.settings.Settings;

public class ActivityMain extends AppCompatActivity
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private CoordinatorLayout coordinatorLayout;
	private ImageButton buttonSettings;
	private ImageButton buttonHelp;
	private Button buttonRetryConnection;
	private Button buttonCreateServer;
	private Button buttonJoinServer;
	private ImageButton buttonCreateServerClose;
	private EditText editTextServerName;
	private Button buttonCreateServerApply;
	private ImageButton buttonJoinServerClose;
	private EditText editTextServerCode;
	private Button buttonJoinServerApply;
	
	private ControlLogic controlLogic;
	private FragmentRetain<ControlLogic> fragmentRetain;
	private ActivityMainUI ui;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkFirstStart();
		restoreRetainFragment();
		ui = new ActivityMainUI(this);
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		
		buttonSettings = findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(v -> showSettings());
		
		buttonHelp = findViewById(R.id.button_help);
		buttonHelp.setOnClickListener(v -> showHelp());
		
		buttonRetryConnection = findViewById(R.id.button_retry_connection);
		buttonRetryConnection.setOnClickListener(v -> connectIfNotConnected());
		
		buttonCreateServer = findViewById(R.id.button_create_server);
		buttonCreateServer.setOnClickListener(v -> ui.showCreateServerPanel());
		
		buttonJoinServer = findViewById(R.id.button_join_server);
		buttonJoinServer.setOnClickListener(v -> ui.showJoinServerPanel());
		
		buttonCreateServerClose = findViewById(R.id.button_create_server_close);
		buttonCreateServerClose.setOnClickListener(v -> ui.showConnectedPanel());
		
		editTextServerName = findViewById(R.id.editText_server_name);
		editTextServerName.setOnEditorActionListener((v, actionId, event) -> onServerNameEditorAction(actionId));
		
		buttonCreateServerApply = findViewById(R.id.button_create_server_apply);
		buttonCreateServerApply.setOnClickListener(v -> applyServerCreation());
		
		buttonJoinServerClose = findViewById(R.id.button_join_server_close);
		buttonJoinServerClose.setOnClickListener(v -> ui.showConnectedPanel());
		
		editTextServerCode = findViewById(R.id.editText_server_code);
		editTextServerCode.setOnEditorActionListener((v, actionId, event) -> onServerCodeEditorAction(actionId));
		
		buttonJoinServerApply = findViewById(R.id.button_join_server_apply);
		buttonJoinServerApply.setOnClickListener(v -> applyServerJoin());
	}
	
	private void checkFirstStart()
	{
		if(!Settings.isFirstStart(this)) return;
		
		Intent intent = new Intent(this, ActivityStart.class);
		startActivity(intent);
	}
	
	@SuppressWarnings("unchecked")
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain<ControlLogic>) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
		else controlLogic = fragmentRetain.getLogic();
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		controlLogic = new ControlLogic();
		fragmentRetain = new FragmentRetain<>();
		fragmentRetain.setLogic(controlLogic);
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		ui.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		ui.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		controlLogic.resume(this);
		connectIfNotConnected();
		ui.updateLayout();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		editTextServerName.setOnEditorActionListener(null);
		editTextServerCode.setOnEditorActionListener(null);
		controlLogic.suspend();
	}
	
	@Override
	public void onBackPressed()
	{
		if(!ui.onBackPressed()) super.onBackPressed();
	}
	
	private void showSettings()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		startActivity(intent);
	}
	
	private void showHelp()
	{
		Intent intent = new Intent(this, ActivityHelp.class);
		startActivity(intent);
	}
	
	private void connectIfNotConnected()
	{
		String host = Settings.getServerAddress(this);
		if(controlLogic.isConnectedTo(host)) return;
		
		ui.showConnectingPanel();
		controlLogic.connect(host);
	}
	
	private void changeUsername()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		intent.putExtra("preferenceToEdit", Settings.KEY_NICK);
		startActivity(intent);
	}
	
	private void applyServerCreation()
	{
		String name = editTextServerName.getText().toString();
		if(name.length() < 3) ui.setServerNameError(getString(R.string.message_server_name_too_short));
		else
		{
			ui.setServerNameError(null);
			ui.hideKeyboard(editTextServerName);
			controlLogic.createServer(name, Settings.getNick(this));
		}
	}
	
	private void applyServerJoin()
	{
		String code = editTextServerCode.getText().toString();
		if(code.length() != 4) ui.setServerCodeError(getString(R.string.message_server_code_length));
		else
		{
			ui.setServerCodeError(null);
			ui.hideKeyboard(editTextServerCode);
			
			int codeInt = Integer.parseInt(code);
			controlLogic.login(codeInt, Settings.getNick(this));
		}
	}
	
	private boolean onServerNameEditorAction(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		applyServerCreation();
		return true;
	}
	
	private boolean onServerCodeEditorAction(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		applyServerJoin();
		return true;
	}
	
	void onConnect()
	{
		ui.showConnectedPanel();
	}
	
	void onConnectFail()
	{
		ui.showCannotConnectPanel();
	}
	
	void onDisconnect()
	{
		ui.showCannotConnectPanel();
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
		ui.showConnectedPanelOnNextUpdate();
		
		controlLogic.suspend();
		ServerData data = new ServerData(controlLogic.getClient(), serverName, serverCode);
		ServerData.setServerData(data);
		
		Intent intent = new Intent(this, ActivityServer.class);
		startActivity(intent);
	}
	
	void onInvalidServerNameError()
	{
		ui.setServerNameError(getString(R.string.message_server_name_invalid));
	}
	
	void onTooManyServersError()
	{
		Snackbar.make(coordinatorLayout, R.string.message_too_many_servers, Snackbar.LENGTH_LONG).show();
	}
	
	void onInvalidUsernameError()
	{
		Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.message_invalid_username, Snackbar.LENGTH_LONG);
		snackbar.setAction(R.string.snackbar_action_change_username, v -> changeUsername());
		snackbar.show();
	}
	
	void onTooManyUsersError()
	{
		Snackbar.make(coordinatorLayout, R.string.message_too_many_users, Snackbar.LENGTH_LONG).show();
	}
	
	void onUsernameBusyError()
	{
		Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.message_username_busy, Snackbar.LENGTH_LONG);
		snackbar.setAction(R.string.snackbar_action_change_username, v -> changeUsername());
		snackbar.show();
	}
	
	void onInvalidServerCode()
	{
		ui.setServerCodeError(getString(R.string.message_server_code_invalid));
	}
	
	void onCannotCreateServer()
	{
		Snackbar.make(coordinatorLayout, R.string.message_cannot_create_server, Snackbar.LENGTH_LONG).show();
	}
	
	void onCannotLogIn()
	{
		Snackbar.make(coordinatorLayout, R.string.message_cannot_login, Snackbar.LENGTH_LONG).show();
	}
}
