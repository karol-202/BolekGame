package pl.karol202.bolekgame.ui.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class ActivityMain extends AppCompatActivity
{
	private ControlUI controlUI;
	
	private EditText editNick;
	private EditText editServerAddress;
	private Button buttonServerConnect;
	private TextView textServerStatus;
	private FloatingActionButton buttonServerLogin;
	private FloatingActionButton buttonServerCreate;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		controlUI = new ControlUI(this);
		
		editNick = findViewById(R.id.edit_nick);
		
		editServerAddress = findViewById(R.id.edit_server_address);
		
		buttonServerConnect = findViewById(R.id.button_server_connect);
		buttonServerConnect.setOnClickListener(v -> connectToServer());
		
		textServerStatus = findViewById(R.id.text_server_status);
		
		buttonServerLogin = findViewById(R.id.button_server_login);
		buttonServerLogin.setEnabled(false);
		buttonServerLogin.setOnClickListener(v -> loginToServer());
		
		buttonServerCreate = findViewById(R.id.button_server_create);
		buttonServerCreate.setEnabled(false);
		buttonServerCreate.setOnClickListener(v -> createServer());
	}
	
	private void connectToServer()
	{
		String host = editServerAddress.getText().toString();
		if(controlUI.connect(host)) onConnect();
	}
	
	private void onConnect()
	{
		textServerStatus.setText(R.string.text_connected);
		buttonServerLogin.setEnabled(true);
		buttonServerCreate.setEnabled(true);
	}
	
	public void onDisconnect()
	{
		textServerStatus.setText(R.string.text_not_connected);
		buttonServerLogin.setEnabled(false);
		buttonServerCreate.setEnabled(false);
	}
	
	private void loginToServer()
	{
		String username = editNick.getText().toString();
		//controlUI.login(username);
	}
	
	private void createServer()
	{
		String username = editNick.getText().toString();
		//controlUI.login(username);
	}
	
	void onLoggedIn(String serverName, int serverCode)
	{
	
	}
}