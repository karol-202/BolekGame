package pl.karol202.bolekgame.server;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class ActivityServer extends AppCompatActivity
{
	private TextView textServerName;
	private TextView textServerCode;
	
	private ServerLogic serverLogic;
	private ServerData serverData;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		loadServerData();
		
		textServerName = findViewById(R.id.text_server_name_value);
		textServerName.setText(serverData.getServerName());
		
		textServerCode = findViewById(R.id.text_server_code_value);
		textServerCode.setText(String.valueOf(serverData.getServerCode()));
	}
	
	private void loadServerData()
	{
		serverData = ServerData.getServerData();
		if(serverData == null || serverData.getClient() == null)
			throw new IllegalStateException("Cannot start ActivityServer without passing valid ServerData");
		serverLogic = new ServerLogic(this, serverData.getClient());
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
}
