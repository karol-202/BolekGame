package pl.karol202.bolekgame.ui.control;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.Utils;

public class ActivityMain extends AppCompatActivity
{
	private ControlUI controlUI;
	
	private View mainLayout;
	private Toolbar toolbar;
	private EditText editServerAddress;
	private Button buttonServerConnect;
	private ProgressBar progressServer;
	private FloatingActionButton buttonServerLogin;
	private FloatingActionButton buttonServerCreate;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		controlUI = new ControlUI(this);
		
		mainLayout = findViewById(R.id.main_layout);
		
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		editServerAddress = findViewById(R.id.edit_server_address);
		
		buttonServerConnect = findViewById(R.id.button_server_connect);
		buttonServerConnect.setOnClickListener(v -> connectToServer());
		
		progressServer = findViewById(R.id.progress_server_status);
		progressServer.setVisibility(View.GONE);
		
		buttonServerLogin = findViewById(R.id.button_server_login);
		buttonServerLogin.setOnClickListener(v -> showLoginDialog());
		
		buttonServerCreate = findViewById(R.id.button_server_create);
		buttonServerCreate.setOnClickListener(v -> showServerCreationDialog());
	}
	
	private void connectToServer()
	{
		String host = editServerAddress.getText().toString();
		controlUI.connectAsync(host);
		
		editServerAddress.setEnabled(false);
		buttonServerConnect.setEnabled(false);
		progressServer.setVisibility(View.VISIBLE);
	}
	
	void onConnect()
	{
		editServerAddress.setEnabled(false);
		buttonServerLogin.setVisibility(View.VISIBLE);
		buttonServerCreate.setVisibility(View.VISIBLE);
		progressServer.setVisibility(View.GONE);
		Snackbar.make(mainLayout, R.string.message_connected, Snackbar.LENGTH_LONG).show();
	}
	
	void onConnectFail()
	{
		editServerAddress.setEnabled(true);
		buttonServerConnect.setEnabled(true);
		progressServer.setVisibility(View.GONE);
		Snackbar.make(mainLayout, R.string.message_cannot_connect, Snackbar.LENGTH_LONG).show();
	}
	
	void onDisconnect()
	{
		editServerAddress.setEnabled(true);
		buttonServerConnect.setEnabled(true);
		buttonServerLogin.setVisibility(View.GONE);
		buttonServerCreate.setVisibility(View.GONE);
		Snackbar.make(mainLayout, R.string.message_disconnected, Snackbar.LENGTH_LONG).show();
	}
	
	@SuppressLint("InflateParams")
	private void showLoginDialog()
	{
		if(!controlUI.isConnected()) return;
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_login, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_login);
		builder.setView(view);
		
		EditText editServerCode = view.findViewById(R.id.edit_server_code);
		
		EditText editNick = view.findViewById(R.id.edit_nick);
		
		builder.setPositiveButton(R.string.button_login, (dialog, which) -> {
			Integer serverCode = Utils.parseInt(editServerCode.getText().toString());
			String nick = editNick.getText().toString();
			if(serverCode != null && !nick.isEmpty()) login(serverCode, nick);
		});
		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show();
	}
	
	private void login(int serverCode, String nick)
	{
		controlUI.login(serverCode, nick);
		
		buttonServerLogin.setVisibility(View.GONE);
		buttonServerCreate.setVisibility(View.GONE);
	}
	
	@SuppressLint("InflateParams")
	private void showServerCreationDialog()
	{
		if(!controlUI.isConnected()) return;
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_create_server, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_create_server);
		builder.setView(view);
		
		EditText editServerName = view.findViewById(R.id.edit_server_name);
		
		EditText editNick = view.findViewById(R.id.edit_nick);
		
		builder.setPositiveButton(R.string.button_create_server, (dialog, which) -> {
			String serverName = editServerName.getText().toString();
			String nick = editNick.getText().toString();
			if(!serverName.isEmpty() && !nick.isEmpty()) createServer(serverName, nick);
		});
		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show();
	}
	
	private void createServer(String serverName, String nick)
	{
		controlUI.createServer(serverName, nick);
		
		buttonServerLogin.setVisibility(View.GONE);
		buttonServerCreate.setVisibility(View.GONE);
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
	
	}
	
	void onCannotLogIn()
	{
		buttonServerLogin.setVisibility(View.VISIBLE);
		buttonServerCreate.setVisibility(View.VISIBLE);
		Snackbar.make(mainLayout, R.string.message_cannot_login, Snackbar.LENGTH_LONG).show();
	}
}