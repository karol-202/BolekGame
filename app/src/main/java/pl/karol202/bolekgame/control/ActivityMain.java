package pl.karol202.bolekgame.control;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.transition.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.server.ActivityServer;
import pl.karol202.bolekgame.server.ServerData;
import pl.karol202.bolekgame.settings.ActivitySettings;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.FragmentRetain;

public class ActivityMain extends AppCompatActivity
{
	private interface ConstraintChange
	{
		void changeConstraints();
	}
	
	private interface OnTransitionEndListener
	{
		void onTransitionEnd();
	}
	
	private enum State
	{
		CONNECTING, CANNOT_CONNECT, CONNECTED, SERVER_CREATING, SERVER_JOINING;
		
		private static State getStateById(int id)
		{
			if(id < 0 || id >= values().length) return null;
			return values()[id];
		}
	}
	
	private static final String KEY_STATE = "KEY_STATE";
	
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private CoordinatorLayout coordinatorLayout;
	private ConstraintLayout mainLayout;
	private ImageButton buttonSettings;
	private ConstraintLayout panelConnection;
	private ProgressBar progressBarConnecting;
	private TextView textConnection;
	private Button buttonRetryConnection;
	private Button buttonCreateServer;
	private Button buttonJoinServer;
	private ViewGroup panelCreateServer;
	private ImageButton buttonCreateServerClose;
	private TextInputLayout textInputLayoutServerName;
	private EditText editTextServerName;
	private Button buttonCreateServerApply;
	private ViewGroup panelJoinServer;
	private ImageButton buttonJoinServerClose;
	private TextInputLayout textInputLayoutServerCode;
	private EditText editTextServerCode;
	private Button buttonJoinServerApply;
	
	private ConstraintSet mainConstraintSet;
	private ConstraintSet connectingConstraintSet;
	private State state;
	
	private FragmentRetain<ControlLogic> fragmentRetain;
	private ControlLogic controlLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		restoreRetainFragment();
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		
		mainLayout = findViewById(R.id.main_layout);
		mainConstraintSet = new ConstraintSet();
		mainConstraintSet.clone(mainLayout);
		
		buttonSettings = findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(v -> showSettings());
		
		panelConnection = findViewById(R.id.panel_connection);
		connectingConstraintSet = new ConstraintSet();
		connectingConstraintSet.clone(panelConnection);
		
		progressBarConnecting = findViewById(R.id.progressBar_connecting);
		
		textConnection = findViewById(R.id.text_connection);
		
		buttonRetryConnection = findViewById(R.id.button_retry_connection);
		buttonRetryConnection.setOnClickListener(v -> retryConnection());
		
		buttonCreateServer = findViewById(R.id.button_create_server);
		buttonCreateServer.setOnClickListener(v -> showCreateServerPanel());
		
		buttonJoinServer = findViewById(R.id.button_join_server);
		buttonJoinServer.setOnClickListener(v -> showJoinServerPanel());
		
		panelCreateServer = findViewById(R.id.panel_create_server);
		
		buttonCreateServerClose = findViewById(R.id.button_create_server_close);
		buttonCreateServerClose.setOnClickListener(v -> closeCreateServerPanel());
		
		textInputLayoutServerName = findViewById(R.id.editTextLayout_server_name);
		
		editTextServerName = findViewById(R.id.editText_server_name);
		editTextServerName.setOnEditorActionListener((v, actionId, event) -> onServerNameEditingDone(actionId));
		
		buttonCreateServerApply = findViewById(R.id.button_create_server_apply);
		buttonCreateServerApply.setOnClickListener(v -> applyServerCreation());
		
		panelJoinServer = findViewById(R.id.panel_join_server);
		
		buttonJoinServerClose = findViewById(R.id.button_join_server_close);
		buttonJoinServerClose.setOnClickListener(v -> closeJoinServerPanel());
		
		textInputLayoutServerCode = findViewById(R.id.editTextLayout_server_code);
		
		editTextServerCode = findViewById(R.id.editText_server_code);
		editTextServerCode.setOnEditorActionListener((v, actionId, event) -> onServerCodeEditingDone(actionId));
		
		buttonJoinServerApply = findViewById(R.id.button_join_server_apply);
		buttonJoinServerApply.setOnClickListener(v -> applyServerJoin());
		
		state = State.CONNECTING;
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
	
	private void setLayout()
	{
		if(state == null) state = State.CONNECTING;
		switch(state)
		{
		case CONNECTING:
			setConnectingLayout();
			break;
		case CANNOT_CONNECT:
			setCannotConnectLayout();
			break;
		case CONNECTED:
			setConnectedLayout(true);
			break;
		case SERVER_CREATING:
			setCreateServerLayout();
			break;
		case SERVER_JOINING:
			setJoinServerLayout();
			break;
		}
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		state = State.getStateById(savedInstanceState.getInt(KEY_STATE, -1));
		setLayout();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_STATE, state.ordinal());
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		controlLogic.resume(this); //On orientation changes and on back from other activity
		connectIfNotConnected();
		updateState();
	}
	
	private void connectIfNotConnected()
	{
		if(controlLogic.isConnected()) return;
		controlLogic.connect(Settings.getServerAddress(this));
	}
	
	private void updateState()
	{
		if(controlLogic.isConnected()) state = State.CONNECTED;
		else state = State.CONNECTING;
		setLayout();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		editTextServerName.setOnEditorActionListener(null);
		editTextServerCode.setOnEditorActionListener(null);
		controlLogic.suspend(); //On orientation changes
	}
	
	@Override
	public void onBackPressed()
	{
		if(state == State.SERVER_CREATING || state == State.SERVER_JOINING)
		{
			applyConstraintTransition(() -> setConnectedLayout(true), null);
			setAllPanelsClosed();
		}
		else super.onBackPressed();
	}
	
	private void showSettings()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		startActivity(intent);
	}
	
	private void retryConnection()
	{
		if(controlLogic.isConnected()) return;
		connectIfNotConnected();
		applyConstraintTransition(this::setConnectingLayout, null);
		state = State.CONNECTING;
	}
	
	private void showCreateServerPanel()
	{
		if(state == State.SERVER_JOINING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), this::showCreateServerPanel);
			onJoinServerPanelClose();
			state = State.CONNECTED;
		}
		else if(state == State.CONNECTED)
		{
			applyConstraintTransition(this::setCreateServerLayout, null);
			state = State.SERVER_CREATING;
		}
	}
	
	private void closeCreateServerPanel()
	{
		if(state == State.CONNECTED) return;
		applyConstraintTransition(() -> setConnectedLayout(true), null);
		onCreateServerPanelClose();
		state = State.CONNECTED;
	}
	
	private void onCreateServerPanelClose()
	{
		textInputLayoutServerName.setErrorEnabled(false);
		editTextServerName.setText("");
		hideKeyboard(editTextServerName);
	}
	
	private boolean onServerNameEditingDone(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		applyServerCreation();
		return true;
	}
	
	private void applyServerCreation()
	{
		TransitionManager.beginDelayedTransition(panelCreateServer);
		String serverName = editTextServerName.getText().toString();
		if(serverName.length() < 3) textInputLayoutServerName.setError(getString(R.string.message_server_name_too_short));
		else
		{
			textInputLayoutServerName.setErrorEnabled(false);
			hideKeyboard(editTextServerName);
			controlLogic.createServer(serverName, Settings.getNick(this));
		}
	}
	
	private void showJoinServerPanel()
	{
		if(state == State.SERVER_CREATING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), this::showJoinServerPanel);
			onCreateServerPanelClose();
			state = State.CONNECTED;
		}
		else if(state == State.CONNECTED)
		{
			applyConstraintTransition(this::setJoinServerLayout, null);
			state = State.SERVER_JOINING;
		}
	}
	
	private void closeJoinServerPanel()
	{
		if(state == State.CONNECTED) return;
		applyConstraintTransition(() -> setConnectedLayout(true), null);
		onJoinServerPanelClose();
		state = State.CONNECTED;
	}
	
	private void onJoinServerPanelClose()
	{
		textInputLayoutServerCode.setErrorEnabled(false);
		editTextServerCode.setText("");
		hideKeyboard(editTextServerCode);
	}
	
	private boolean onServerCodeEditingDone(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		applyServerJoin();
		return true;
	}
	
	private void applyServerJoin()
	{
		TransitionManager.beginDelayedTransition(panelJoinServer);
		String serverCodeString = editTextServerCode.getText().toString();
		if(serverCodeString.length() != 4) textInputLayoutServerCode.setError(getString(R.string.message_server_code_length));
		else
		{
			int serverCode = Integer.parseInt(serverCodeString);
			textInputLayoutServerCode.setErrorEnabled(false);
			hideKeyboard(editTextServerCode);
			controlLogic.login(serverCode, Settings.getNick(this));
		}
	}
	
	private void setAllPanelsClosed()
	{
		onCreateServerPanelClose();
		onJoinServerPanelClose();
		state = State.CONNECTED;
	}
	
	private void hideKeyboard(TextView v)
	{
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manager != null) manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	private void setConnectingLayout()
	{
		connectingConstraintSet.connect(R.id.progressBar_connecting, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		int margin = (int) getResources().getDimension(R.dimen.text_connecting_margin);
		connectingConstraintSet.connect(R.id.text_connection, ConstraintSet.TOP, R.id.progressBar_connecting, ConstraintSet.TOP, margin);
		connectingConstraintSet.connect(R.id.text_connection, ConstraintSet.BOTTOM, R.id.progressBar_connecting, ConstraintSet.BOTTOM, margin);
		
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
		
		progressBarConnecting.setVisibility(View.VISIBLE);
		textConnection.setText(R.string.text_connecting);
		buttonRetryConnection.setVisibility(View.GONE);
	}
	
	private void setCannotConnectLayout()
	{
		connectingConstraintSet.connect(R.id.progressBar_connecting, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		int margin = (int) getResources().getDimension(R.dimen.text_cannot_connect_margin);
		connectingConstraintSet.connect(R.id.text_connection, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, margin);
		connectingConstraintSet.connect(R.id.text_connection, ConstraintSet.BOTTOM, R.id.button_retry_connection, ConstraintSet.TOP, margin);
		
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
		
		progressBarConnecting.setVisibility(View.GONE);
		textConnection.setText(R.string.text_cannot_connect);
		buttonRetryConnection.setVisibility(View.VISIBLE);
	}
	
	private void setConnectedLayout(boolean serverPanelToCenter)
	{
		mainConstraintSet.clear(R.id.panel_connection, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		if(serverPanelToCenter)
			mainConstraintSet.connect(R.id.panel_server, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
	}
	
	private void setCreateServerLayout()
	{
		mainConstraintSet.clear(R.id.panel_connection, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
		
		editTextServerName.setEnabled(true);
		editTextServerCode.setEnabled(false);
		editTextServerName.requestFocus();
	}
	
	private void setJoinServerLayout()
	{
		mainConstraintSet.clear(R.id.panel_connection, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_connection, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.connect(R.id.panel_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, R.id.panel_server, ConstraintSet.BOTTOM);
		
		mainConstraintSet.applyTo(mainLayout);
		connectingConstraintSet.applyTo(panelConnection);
		
		editTextServerName.setEnabled(false);
		editTextServerCode.setEnabled(true);
		editTextServerCode.requestFocus();
	}
	
	private void applyConstraintTransition(ConstraintChange change, OnTransitionEndListener listener)
	{
		TransitionSet transition = new TransitionSet();
		transition.addTransition(new ChangeBounds());
		transition.addTransition(new Fade());
		transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
		if(listener != null) transition.addListener(new TransitionListenerAdapter()
		{
			@Override
			public void onTransitionEnd(@NonNull Transition transition)
			{
				listener.onTransitionEnd();
			}
		});
		TransitionManager.beginDelayedTransition(mainLayout, transition);
		change.changeConstraints();
	}
	
	void onConnect()
	{
		applyConstraintTransition(() -> setConnectedLayout(true), null);
		state = State.CONNECTED;
	}
	
	void onConnectFail()
	{
		applyConstraintTransition(this::setCannotConnectLayout, null);
		state = State.CANNOT_CONNECT;
	}
	
	void onDisconnect()
	{
		applyConstraintTransition(this::setConnectingLayout, null);
		state = State.CONNECTING;
		connectIfNotConnected();
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
		setAllPanelsClosed();
		controlLogic.suspend();
		ServerData data = new ServerData(controlLogic.getClient(), serverName, serverCode);
		ServerData.setServerData(data);
		
		Intent intent = new Intent(this, ActivityServer.class);
		startActivity(intent);
	}
	
	void onInvalidServerNameError()
	{
		TransitionManager.beginDelayedTransition(panelCreateServer);
		textInputLayoutServerName.setError(getString(R.string.message_server_name_invalid));
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
		TransitionManager.beginDelayedTransition(panelJoinServer);
		textInputLayoutServerCode.setError(getString(R.string.message_server_code_invalid));
	}
	
	void onCannotCreateServer()
	{
		Snackbar.make(coordinatorLayout, R.string.message_cannot_create_server, Snackbar.LENGTH_LONG).show();
	}
	
	void onCannotLogIn()
	{
		Snackbar.make(coordinatorLayout, R.string.message_cannot_login, Snackbar.LENGTH_LONG).show();
	}
	
	private void changeUsername()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		intent.putExtra("preferenceToEdit", Settings.KEY_NICK);
		startActivity(intent);
	}
}