package pl.karol202.bolekgame.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
import android.support.transition.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
		STANDARD, SERVER_CREATING, SERVER_JOINING;
		
		private static State getStateById(int id)
		{
			if(id < 0 || id > 2) return null;
			return values()[id];
		}
	}
	
	private static final String KEY_STATE = "KEY_STATE";
	
	private ControlLogic controlLogic;
	
	private ConstraintLayout mainLayout;
	private ImageButton buttonSettings;
	private View panelServer;
	private Button buttonCreateServer;
	private Button buttonJoinServer;
	private View panelCreateServer;
	private ImageButton buttonCreateServerClose;
	private EditText editTextServerName;
	private Button buttonCreateServerApply;
	private ImageButton buttonJoinServerClose;
	private EditText editTextServerCode;
	private Button buttonJoinServerApply;
	
	private ConstraintSet mainConstraintSet;
	private State state;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		controlLogic = new ControlLogic(this);
		
		mainLayout = findViewById(R.id.main_layout);
		mainConstraintSet = new ConstraintSet();
		mainConstraintSet.clone(mainLayout);
		
		buttonSettings = findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(v -> showSettings());
		
		panelServer = findViewById(R.id.panel_server);
		
		buttonCreateServer = findViewById(R.id.button_create_server);
		buttonCreateServer.setOnClickListener(v -> showCreateServerPanel());
		
		buttonJoinServer = findViewById(R.id.button_join_server);
		buttonJoinServer.setOnClickListener(v -> showJoinServerPanel());
		
		hidePanelUnderScreen(R.id.panel_create_server);
		
		buttonCreateServerClose = findViewById(R.id.button_create_server_close);
		buttonCreateServerClose.setOnClickListener(v -> closeCreateServerPanel());
		
		editTextServerName = findViewById(R.id.editText_server_name);
		
		buttonCreateServerApply = findViewById(R.id.button_create_server_apply);
		buttonCreateServerApply.setOnClickListener(v -> applyServerCreation());
		
		hidePanelUnderScreen(R.id.panel_join_server);
		
		buttonJoinServerClose = findViewById(R.id.button_join_server_close);
		buttonJoinServerClose.setOnClickListener(v -> closeJoinServerPanel());
		
		editTextServerCode = findViewById(R.id.editText_server_code);
		
		buttonJoinServerApply = findViewById(R.id.button_join_server_apply);
		buttonJoinServerApply.setOnClickListener(v -> applyServerJoin());
		
		state = State.STANDARD;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		state = State.getStateById(savedInstanceState.getInt(KEY_STATE, -1));
		if(state == null) state = State.STANDARD;
		switch(state)
		{
		case STANDARD: return;
		case SERVER_CREATING:
			setCreateServerLayout();
			break;
		case SERVER_JOINING:
			setJoinServerLayout();
			break;
		}
		mainConstraintSet.applyTo(mainLayout);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_STATE, state.ordinal());
	}
	
	private void hidePanelUnderScreen(int id)
	{
		mainConstraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.applyTo(mainLayout);
	}
	
	private void showSettings()
	{
		Intent intent = new Intent(this, ActivitySettings.class);
		startActivity(intent);
	}
	
	private void showCreateServerPanel()
	{
		if(state == State.SERVER_JOINING)
		{
			applyConstraintTransition(() -> setStandardLayout(false), this::showCreateServerPanel);
			state = State.STANDARD;
		}
		else if(state == State.STANDARD)
		{
			applyConstraintTransition(this::setCreateServerLayout, null);
			state = State.SERVER_CREATING;
		}
	}
	
	private void closeCreateServerPanel()
	{
		if(state == State.STANDARD) return;
		applyConstraintTransition(() -> setStandardLayout(true), null);
		state = State.STANDARD;
	}
	
	private void applyServerCreation()
	{
	
	}
	
	private void showJoinServerPanel()
	{
		if(state == State.SERVER_CREATING)
		{
			applyConstraintTransition(() -> setStandardLayout(false), this::showJoinServerPanel);
			state = State.STANDARD;
		}
		else if(state == State.STANDARD)
		{
			applyConstraintTransition(this::setJoinServerLayout, null);
			state = State.SERVER_JOINING;
		}
	}
	
	private void closeJoinServerPanel()
	{
		if(state == State.STANDARD) return;
		applyConstraintTransition(() -> setStandardLayout(true), null);
		state = State.STANDARD;
	}
	
	private void applyServerJoin()
	{
	
	}
	
	private void setStandardLayout(boolean serverPanelToCenter)
	{
		if(serverPanelToCenter)
			mainConstraintSet.connect(R.id.panel_server, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
	}
	
	private void setCreateServerLayout()
	{
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_create_server, ConstraintSet.TOP, R.id.panel_server, ConstraintSet.BOTTOM);
	}
	
	private void setJoinServerLayout()
	{
		mainConstraintSet.clear(R.id.panel_server, ConstraintSet.BOTTOM);
		mainConstraintSet.connect(R.id.panel_join_server, ConstraintSet.TOP, R.id.panel_server, ConstraintSet.BOTTOM);
	}
	
	private void applyConstraintTransition(ConstraintChange change, OnTransitionEndListener listener)
	{
		Transition transition = new ChangeBounds();
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
		mainConstraintSet.applyTo(mainLayout);
	}
	
	void onConnect()
	{
		Snackbar.make(mainLayout, R.string.message_connected, Snackbar.LENGTH_LONG).show();
	}
	
	void onConnectFail()
	{
		Snackbar.make(mainLayout, R.string.message_cannot_connect, Snackbar.LENGTH_LONG).show();
	}
	
	void onDisconnect()
	{
		Snackbar.make(mainLayout, R.string.message_disconnected, Snackbar.LENGTH_LONG).show();
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
	
	}
	
	void onCannotLogIn()
	{
		Snackbar.make(mainLayout, R.string.message_cannot_login, Snackbar.LENGTH_LONG).show();
	}
}