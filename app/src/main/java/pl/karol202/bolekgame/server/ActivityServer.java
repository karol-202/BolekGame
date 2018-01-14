package pl.karol202.bolekgame.server;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.ActivityGame;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.ItemDivider;

public class ActivityServer extends AppCompatActivity
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private View coordinatorLayout;
	private TextView textServerName;
	private TextView textServerCode;
	private RecyclerView recyclerUsers;
	
	private UsersAdapter usersAdapter;
	
	private FragmentRetain fragmentRetain;
	private ServerLogic serverLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		loadServerData();
		restoreRetainFragment();
		
		usersAdapter = new UsersAdapter(this, serverLogic.getUsers());
		
		coordinatorLayout = findViewById(R.id.coordinator_layout);
		
		textServerName = findViewById(R.id.text_server_name_value);
		textServerName.setText(serverLogic.getServerName());
		
		textServerCode = findViewById(R.id.text_server_code_value);
		textServerCode.setText(String.valueOf(serverLogic.getServerCode()));
		
		recyclerUsers = findViewById(R.id.recycler_users);
		recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
		recyclerUsers.setAdapter(usersAdapter);
		recyclerUsers.addItemDecoration(new ItemDivider(this));
	}
	
	private void loadServerData()
	{
		ServerData serverData = ServerData.getServerData();
		if(serverData != null && serverData.getClient() != null)
			serverLogic = new ServerLogic(this, serverData.getClient(), serverData.getServerName(),
															serverData.getServerCode(), Settings.getNick(this));
	}
	
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
		else serverLogic = new ServerLogic(this, fragmentRetain.getClient(), fragmentRetain.getServerName(),
															 fragmentRetain.getServerCode(), fragmentRetain.getUsers());
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		fragmentRetain = new FragmentRetain();
		fragmentRetain.setUsers(serverLogic.getUsers());
		fragmentRetain.setServerName(serverLogic.getServerName());
		fragmentRetain.setServerCode(serverLogic.getServerCode());
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
	}
	
	@Override
	public void onBackPressed()
	{
		serverLogic.logout();
		super.onBackPressed();
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
	
	void onError()
	{
		Snackbar.make(coordinatorLayout, R.string.message_error, Snackbar.LENGTH_LONG).show();
	}
	
	void onGameStart()
	{
		Intent intent = new Intent(this, ActivityGame.class);
		startActivity(intent);
	}
}
