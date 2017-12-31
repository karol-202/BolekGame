package pl.karol202.bolekgame.control;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import pl.karol202.bolekgame.FragmentRetain;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.settings.ActivitySettings;

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
	
	private ConstraintLayout mainLayout;
	private ImageButton buttonSettings;
	private ConstraintLayout panelConnection;
	private ProgressBar progressBarConnecting;
	private TextView textConnection;
	private Button buttonRetryConnection;
	private Button buttonCreateServer;
	private Button buttonJoinServer;
	private ImageButton buttonCreateServerClose;
	private EditText editTextServerName;
	private Button buttonCreateServerApply;
	private ImageButton buttonJoinServerClose;
	private EditText editTextServerCode;
	private Button buttonJoinServerApply;
	
	private ConstraintSet mainConstraintSet;
	private ConstraintSet connectingConstraintSet;
	private State state;
	
	private FragmentRetain fragmentRetain;
	private ControlLogic controlLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		
		buttonCreateServerClose = findViewById(R.id.button_create_server_close);
		buttonCreateServerClose.setOnClickListener(v -> closeCreateServerPanel());
		
		editTextServerName = findViewById(R.id.editText_server_name);
		
		buttonCreateServerApply = findViewById(R.id.button_create_server_apply);
		buttonCreateServerApply.setOnClickListener(v -> applyServerCreation());
		
		buttonJoinServerClose = findViewById(R.id.button_join_server_close);
		buttonJoinServerClose.setOnClickListener(v -> closeJoinServerPanel());
		
		editTextServerCode = findViewById(R.id.editText_server_code);
		
		buttonJoinServerApply = findViewById(R.id.button_join_server_apply);
		buttonJoinServerApply.setOnClickListener(v -> applyServerJoin());
		
		state = State.CONNECTING;
		setLayout();
		
		restoreRetainFragment();
		controlLogic = new ControlLogic(this, fragmentRetain.getClient());
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
	
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		fragmentRetain = new FragmentRetain();
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		connectIfNotConnected();
	}
	
	private void connectIfNotConnected()
	{
		if(controlLogic.isConnected()) return;
		controlLogic.connect(getServerAddress());
	}
	
	private String getServerAddress()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getString(ActivitySettings.KEY_SERVER_ADDRESS, null);
	}
	
	private void showSettings()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		startActivity(intent);
	}
	
	private void retryConnection()
	{
		connectIfNotConnected();
		applyConstraintTransition(this::setConnectingLayout, null);
		state = State.CONNECTING;
	}
	
	private void showCreateServerPanel()
	{
		if(state == State.SERVER_JOINING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), this::showCreateServerPanel);
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
		state = State.CONNECTED;
	}
	
	private void applyServerCreation()
	{
	
	}
	
	private void showJoinServerPanel()
	{
		if(state == State.SERVER_CREATING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), this::showJoinServerPanel);
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
		state = State.CONNECTED;
	}
	
	private void applyServerJoin()
	{
	
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
		if(state != State.CONNECTING) return;
		applyConstraintTransition(() -> setConnectedLayout(true), null);
		state = State.CONNECTED;
	}
	
	void onConnectFail()
	{
		if(state != State.CONNECTING) return;
		applyConstraintTransition(this::setCannotConnectLayout, null);
		state = State.CANNOT_CONNECT;
	}
	
	void onDisconnect()
	{
		applyConstraintTransition(this::setConnectingLayout, null);
		state = State.CONNECTING;
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
	
	}
	
	void onCannotLogIn()
	{
		Toast.makeText(this, R.string.message_cannot_login, Toast.LENGTH_LONG).show();
	}
}