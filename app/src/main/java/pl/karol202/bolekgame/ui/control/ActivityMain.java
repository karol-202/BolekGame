package pl.karol202.bolekgame.ui.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import pl.karol202.bolekgame.R;

public class ActivityMain extends AppCompatActivity
{
	private EditText editNick;
	private EditText editServerAddress;
	private FloatingActionButton buttonServerConnect;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editNick = findViewById(R.id.edit_nick);
		
		editServerAddress = findViewById(R.id.edit_server_address);
		
		buttonServerConnect = findViewById(R.id.button_server_connect);
		buttonServerConnect.setOnClickListener(view -> connectToServer());
	}
	
	private void connectToServer()
	{
	
	}
}