package pl.karol202.bolekgame.game;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.utils.BottomNavigationBarHelper;
import pl.karol202.bolekgame.utils.ConnectionData;
import pl.karol202.bolekgame.utils.FragmentRetain;

public class ActivityGame extends AppCompatActivity
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private ViewPager viewPager;
	private BottomNavigationView bottomBar;
	
	private GameScreenAdapter gameScreenAdapter;
	
	private FragmentRetain<GameLogic> fragmentRetain;
	private GameLogic gameLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		loadServerData();
		restoreRetainFragment();
		
		gameScreenAdapter = new GameScreenAdapter(getFragmentManager());
		
		viewPager = findViewById(R.id.viewPager_game);
		viewPager.setAdapter(gameScreenAdapter);
		
		bottomBar = findViewById(R.id.bottomBar_game);
		bottomBar.setOnNavigationItemSelectedListener(this::onScreenItemSelected);
		BottomNavigationBarHelper.disableShiftAnimation(bottomBar);
	}
	
	private void loadServerData()
	{
		ConnectionData connectionData = ConnectionData.getConnectionData();
		if(connectionData != null && connectionData.getClient() != null)
			gameLogic = new GameLogic(connectionData.getClient());
	}
	
	@SuppressWarnings("unchecked")
	private void restoreRetainFragment()
	{
		FragmentManager manager = getFragmentManager();
		fragmentRetain = (FragmentRetain) manager.findFragmentByTag(TAG_FRAGMENT_RETAIN);
		if(fragmentRetain == null) createRetainFragment(manager);
		else gameLogic = fragmentRetain.getLogic();
	}
	
	private void createRetainFragment(FragmentManager fragmentManager)
	{
		fragmentRetain = new FragmentRetain<>();
		fragmentRetain.setLogic(gameLogic);
		fragmentManager.beginTransaction().add(fragmentRetain, TAG_FRAGMENT_RETAIN).commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		gameLogic.setActivity(this);
		gameLogic.resumeClient();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		gameLogic.suspendClient();
		gameLogic.setActivity(null);
	}
	
	private boolean onScreenItemSelected(MenuItem menuItem)
	{
		int fragmentId = gameScreenAdapter.getFragmentIdByItemId(menuItem.getItemId());
		if(fragmentId == -1) return false;
		viewPager.setCurrentItem(fragmentId);
		return true;
	}
}
