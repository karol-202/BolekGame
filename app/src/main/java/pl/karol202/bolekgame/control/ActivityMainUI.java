package pl.karol202.bolekgame.control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.support.transition.*;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class ActivityMainUI
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
	
	private ConstraintLayout mainLayout;
	private ConstraintLayout panelConnection;
	private ProgressBar progressBarConnecting;
	private TextView textConnection;
	private Button buttonRetryConnection;
	private ViewGroup panelCreateServer;
	private TextInputLayout textInputLayoutServerName;
	private EditText editTextServerName;
	private ViewGroup panelJoinServer;
	private TextInputLayout textInputLayoutServerCode;
	private EditText editTextServerCode;
	
	private Activity activity;
	private ConstraintSet mainConstraintSet;
	private ConstraintSet connectingConstraintSet;
	private State state;
	
	ActivityMainUI(Activity activity)
	{
		this.activity = activity;
		
		mainLayout = activity.findViewById(R.id.main_layout);
		mainConstraintSet = new ConstraintSet();
		mainConstraintSet.clone(mainLayout);
		
		panelConnection = activity.findViewById(R.id.panel_connection);
		connectingConstraintSet = new ConstraintSet();
		connectingConstraintSet.clone(panelConnection);
		
		progressBarConnecting = activity.findViewById(R.id.progressBar_connecting);
		
		textConnection = activity.findViewById(R.id.text_connection);
		
		buttonRetryConnection = activity.findViewById(R.id.button_retry_connection);
		
		panelCreateServer = activity.findViewById(R.id.panel_create_server);
		
		textInputLayoutServerName = activity.findViewById(R.id.editTextLayout_server_name);
		
		editTextServerName = activity.findViewById(R.id.editText_server_name);
		
		panelJoinServer = activity.findViewById(R.id.panel_join_server);
		
		textInputLayoutServerCode = activity.findViewById(R.id.editTextLayout_server_code);
		
		editTextServerCode = activity.findViewById(R.id.editText_server_code);
		
		state = State.CONNECTING;
	}
	
	void onRestoreInstanceState(Bundle savedInstanceState)
	{
		state = State.getStateById(savedInstanceState.getInt(KEY_STATE, -1));
		updateLayout();
	}
	
	void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(KEY_STATE, state.ordinal());
	}
	
	boolean onBackPressed()
	{
		if(state == State.SERVER_CREATING || state == State.SERVER_JOINING)
		{
			showConnectedPanel();
			return true;
		}
		return false;
	}
	
	void showConnectingPanel()
	{
		applyConstraintTransition(this::setConnectingLayout, this::resetViews);
		state = State.CONNECTING;
	}
	
	void showCannotConnectPanel()
	{
		applyConstraintTransition(this::setCannotConnectLayout, this::resetViews);
		state = State.CANNOT_CONNECT;
	}
	
	void showConnectedPanel()
	{
		applyConstraintTransition(() -> setConnectedLayout(true), this::resetViews);
		state = State.CONNECTED;
	}
	
	void showCreateServerPanel()
	{
		if(state == State.CONNECTED)
		{
			applyConstraintTransition(this::setCreateServerLayout, null);
			state = State.SERVER_CREATING;
		}
		else if(state == State.SERVER_JOINING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), () -> {
				showCreateServerPanel();
				resetViews();
			});
			state = State.CONNECTED;
		}
	}
	
	void showJoinServerPanel()
	{
		if(state == State.CONNECTED)
		{
			applyConstraintTransition(this::setJoinServerLayout, null);
			state = State.SERVER_JOINING;
		}
		else if(state == State.SERVER_CREATING)
		{
			applyConstraintTransition(() -> setConnectedLayout(false), () -> {
				showJoinServerPanel();
				resetViews();
			});
			state = State.CONNECTED;
		}
	}
	
	void showConnectedPanelOnNextUpdate()
	{
		state = State.CONNECTED;
	}
	
	void setServerNameError(CharSequence error)
	{
		TransitionManager.beginDelayedTransition(panelCreateServer);
		textInputLayoutServerName.setError(error);
		if(error == null) textInputLayoutServerName.setErrorEnabled(false);
	}
	
	void setServerCodeError(CharSequence error)
	{
		TransitionManager.beginDelayedTransition(panelJoinServer);
		textInputLayoutServerCode.setError(error);
		if(error == null) textInputLayoutServerCode.setErrorEnabled(false);
	}
	
	void hideKeyboard(TextView v)
	{
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manager != null) manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	void updateLayout()
	{
		switch(state)
		{
		case CONNECTING:
			setConnectingLayout();
			resetViews();
			break;
		case CANNOT_CONNECT:
			setCannotConnectLayout();
			resetViews();
			break;
		case CONNECTED:
			setConnectedLayout(true);
			resetViews();
			break;
		case SERVER_CREATING: applyConstraintTransition(this::setCreateServerLayout, null); break;
		case SERVER_JOINING: applyConstraintTransition(this::setJoinServerLayout, null); break;
		}
	}
	
	private void resetViews()
	{
		textInputLayoutServerName.setError(null);
		textInputLayoutServerName.setErrorEnabled(false);
		textInputLayoutServerCode.setError(null);
		textInputLayoutServerCode.setErrorEnabled(false);
		editTextServerName.setText(null);
		editTextServerCode.setText(null);
	}
	
	private void setConnectingLayout()
	{
		connectingConstraintSet.connect(R.id.progressBar_connecting, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
		
		int margin = (int) activity.getResources().getDimension(R.dimen.text_connecting_margin);
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
		
		int margin = (int) activity.getResources().getDimension(R.dimen.text_cannot_connect_margin);
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
}
