package pl.karol202.bolekgame.game;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.screen.acts.ScreenActs;
import pl.karol202.bolekgame.game.screen.chat.ScreenChat;
import pl.karol202.bolekgame.game.screen.main.ScreenMain;
import pl.karol202.bolekgame.game.screen.players.ScreenPlayers;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.BottomNavigationBarHelper;
import pl.karol202.bolekgame.utils.ConnectionData;
import pl.karol202.bolekgame.utils.FragmentRetain;

public class ActivityGame extends AppCompatActivity implements GameLogicSupplier
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private ViewPager viewPager;
	private BottomNavigationView bottomBar;
	
	private GameScreenAdapter gameScreenAdapter;
	private boolean playerLeaveDialog;
	private boolean tooFewPlayersDialogWaiting;
	
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
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
			
			@Override
			public void onPageSelected(int position) { }
			
			@Override
			public void onPageScrollStateChanged(int state)
			{
				onScreensScroll();
			}
		});
		
		bottomBar = findViewById(R.id.bottomBar_game);
		bottomBar.setOnNavigationItemSelectedListener(this::onScreenItemSelected);
		BottomNavigationBarHelper.disableShiftAnimation(bottomBar);
	}
	
	private void loadServerData()
	{
		ConnectionData connectionData = ConnectionData.getConnectionData();
		if(connectionData != null && connectionData.getClient() != null)
			gameLogic = new GameLogic(connectionData.getClient(), Settings.getNick(this));
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
		gameLogic.resume(this); //On orientation changes
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(!isFinishing()) gameLogic.suspend(); //On orientation changes
	}
	
	private boolean onScreenItemSelected(MenuItem menuItem)
	{
		int fragmentId = gameScreenAdapter.getScreenPositionByItemId(menuItem.getItemId());
		if(fragmentId == -1) return false;
		viewPager.setCurrentItem(fragmentId);
		return true;
	}
	
	private void onScreensScroll()
	{
		int currentScreenPosition = viewPager.getCurrentItem();
		int itemId = gameScreenAdapter.getItemIdOfScreenAtPosition(currentScreenPosition);
		bottomBar.setSelectedItemId(itemId);
	}
	
	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_leave_are_you_sure);
		builder.setMessage(gameLogic.willGameBeEndedAfterMyLeave() ? R.string.dialog_leave_are_you_sure_detail_game_end :
																	 R.string.dialog_leave_are_you_sure_detail);
		builder.setPositiveButton(R.string.leave, (d, w) -> leaveGame());
		builder.setNegativeButton(R.string.remain, null);
		builder.show();
	}
	
	private void leaveGame()
	{
		gameLogic.exit();
		finish();
	}
	
	void onDisconnect()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_disconnection);
		builder.setMessage(R.string.dialog_disconnection_detail);
		builder.setPositiveButton(R.string.ok, (d, w) -> finish());
		builder.setCancelable(false);
		builder.show();
	}
	
	void onError()
	{
		Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
	}
	
	void onPlayerLeaved(Player player)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_player_leaved, player.getName()));
		builder.setPositiveButton(R.string.ok, (d, w) -> {
			playerLeaveDialog = false;
			if(tooFewPlayersDialogWaiting) onTooFewPlayers();
		});
		builder.show();
		playerLeaveDialog = true;
	}
	
	void onTooFewPlayers()
	{
		if(playerLeaveDialog)
		{
			tooFewPlayersDialogWaiting = true;
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_too_few_players);
		builder.setMessage(R.string.dialog_too_few_players_detail);
		builder.setPositiveButton(R.string.ok, (d, w) -> finish());
		builder.setCancelable(false);
		builder.show();
		tooFewPlayersDialogWaiting = false;
	}
	
	void onGameExit()
	{
		finish();
	}
	
	@Override
	public GameLogic getGameLogic()
	{
		return gameLogic;
	}
	
	ScreenMain getScreenMain()
	{
		return (ScreenMain) gameScreenAdapter.getScreenOnPosition(0);
	}
	
	ScreenActs getScreenActs()
	{
		return (ScreenActs) gameScreenAdapter.getScreenOnPosition(1);
	}
	
	ScreenPlayers getScreenPlayers()
	{
		return (ScreenPlayers) gameScreenAdapter.getScreenOnPosition(2);
	}
	
	ScreenChat getScreenChat()
	{
		return (ScreenChat) gameScreenAdapter.getScreenOnPosition(3);
	}
}
