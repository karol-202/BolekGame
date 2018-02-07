package pl.karol202.bolekgame.game;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.acts.ScreenActs;
import pl.karol202.bolekgame.game.chat.ScreenChat;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.main.ScreenMain;
import pl.karol202.bolekgame.game.main.dialog.Dialog;
import pl.karol202.bolekgame.game.main.dialog.DialogManager;
import pl.karol202.bolekgame.game.players.Player;
import pl.karol202.bolekgame.game.players.ScreenPlayers;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.BottomNavigationBarHelper;
import pl.karol202.bolekgame.utils.FragmentRetain;

public class ActivityGame extends AppCompatActivity implements GameLogicSupplier
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	private ViewPager viewPager;
	private BottomNavigationView bottomBar;
	
	private GameScreenAdapter gameScreenAdapter;
	private DialogManager dialogManager;
	
	private FragmentRetain<GameLogic> fragmentRetain;
	private GameLogic gameLogic;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		loadGameData();
		restoreRetainFragment();
		
		gameScreenAdapter = new GameScreenAdapter(getFragmentManager());
		dialogManager = new DialogManager(this);
		
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
	
	private void loadGameData()
	{
		GameData gameData = GameData.getGameData();
		if(gameData != null && gameData.getClient() != null)
			gameLogic = new GameLogic(gameData.getClient(), gameData.getTextChat(), Settings.getNick(this));
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
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(R.string.dialog_leave_are_you_sure);
		dialog.setMessage(gameLogic.willGameBeEndedAfterMyLeave() ? R.string.dialog_leave_are_you_sure_detail_game_end :
																	R.string.dialog_leave_are_you_sure_detail);
		dialog.setPositiveButton(R.string.button_leave, (d, w) -> leaveGame());
		dialog.setNegativeButton(R.string.button_remain);
		dialog.commit();
	}
	
	private void leaveGame()
	{
		gameLogic.exit();
		finish();
	}
	
	void onDisconnect()
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(R.string.dialog_disconnection);
		dialog.setMessage(R.string.dialog_disconnection_detail);
		dialog.setPositiveButton(R.string.button_ok, (d, w) -> finish());
		dialog.setUncancelable();
		dialog.commit();
	}
	
	void onError()
	{
		Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show();
	}
	
	void onInvalidUserError()
	{
		Toast.makeText(this, R.string.message_invalid_user, Toast.LENGTH_LONG).show();
	}
	
	void onPlayerLeaved(Player player)
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(getString(R.string.dialog_player_leaved, player.getName()));
		dialog.setPositiveButton(R.string.button_ok, null);
		dialog.commit();
	}
	
	void onTextChatUpdate()
	{
		ScreenChat screenChat = getScreenChat();
		if(screenChat != null) screenChat.onChatUpdate();
	}
	
	void onRoleAssigned(Role role)
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(getString(R.string.action_role_assigned, getString(role.getNameInstr())));
		dialog.setPositiveButton(R.string.button_ok, null);
		dialog.commit();
	}
	
	void onPollIndexChange()
	{
		ScreenActs screenActs = getScreenActs();
		if(screenActs != null) screenActs.onPollIndexUpdate();
	}
	
	void onActsUpdate()
	{
		ScreenActs screenActs = getScreenActs();
		if(screenActs != null) screenActs.onActsUpdate();
	}
	
	void onTooFewPlayers()
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(R.string.dialog_too_few_players);
		dialog.setMessage(R.string.dialog_too_few_players_detail);
		dialog.setPositiveButton(R.string.button_ok, (d, w) -> finish());
		dialog.setUncancelable();
		dialog.commit();
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
