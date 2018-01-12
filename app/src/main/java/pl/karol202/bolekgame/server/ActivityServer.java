package pl.karol202.bolekgame.server;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import pl.karol202.bolekgame.utils.ItemDivider;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.settings.Settings;

public class ActivityServer extends AppCompatActivity
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private TextView textServerName;
	private TextView textServerCode;
	private RecyclerView recyclerUsers;
	
	private UsersAdapter usersAdapter;
	
	private FragmentRetain fragmentRetain;
	private ServerLogic serverLogic;
	private ServerData serverData;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		loadServerData();
		restoreRetainFragment();
		serverLogic = new ServerLogic(this, serverData.getClient(), Settings.getNick(this));
		
		usersAdapter = new UsersAdapter(this, serverLogic.getUsers());
		
		textServerName = findViewById(R.id.text_server_name_value);
		textServerName.setText(serverData.getServerName());
		
		textServerCode = findViewById(R.id.text_server_code_value);
		textServerCode.setText(String.valueOf(serverData.getServerCode()));
		
		recyclerUsers = findViewById(R.id.recycler_users);
		recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
		recyclerUsers.setAdapter(usersAdapter);
		recyclerUsers.addItemDecoration(new ItemDivider(this));
	}
	
	private void loadServerData()
	{
		serverData = ServerData.getServerData();
	}
	
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
		else serverData = new ServerData(fragmentRetain.getClient(), fragmentRetain.getServerName(), fragmentRetain.getServerCode());
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		fragmentRetain = new FragmentRetain();
		fragmentRetain.setServerName(serverData.getServerName());
		fragmentRetain.setServerCode(serverData.getServerCode());
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
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
