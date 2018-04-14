package pl.karol202.bolekgame.game;

import android.Manifest;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import pl.karol202.bolekgame.BolekApplication;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.acts.ScreenActs;
import pl.karol202.bolekgame.game.chat.ScreenChat;
import pl.karol202.bolekgame.game.dialog.Dialog;
import pl.karol202.bolekgame.game.dialog.DialogManager;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.players.Player;
import pl.karol202.bolekgame.game.players.ScreenPlayers;
import pl.karol202.bolekgame.settings.Settings;
import pl.karol202.bolekgame.utils.BottomNavigationBarHelper;
import pl.karol202.bolekgame.utils.FragmentRetain;
import pl.karol202.bolekgame.utils.PermissionGrantingActivity;
import pl.karol202.bolekgame.utils.PermissionRequest;

public class ActivityGame extends PermissionGrantingActivity implements GameLogicSupplier
{
	private static final String TAG_FRAGMENT_RETAIN = "TAG_FRAG_RETAIN";
	
	static
	{
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}
	
	private ViewPager viewPager;
	private BottomNavigationView bottomBar;
	
	private GameScreenAdapter gameScreenAdapter;
	private DialogManager dialogManager;
	
	private FragmentRetain<GameLogic> fragmentRetain;
	private GameLogic gameLogic;
	private BolekApplication bolekApplication;
	private boolean finish;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		loadGameData();
		bolekApplication = (BolekApplication) getApplication();
		gameScreenAdapter = new GameScreenAdapter(getFragmentManager());
		dialogManager = new DialogManager(this);
		
		restoreRetainFragment();
		
		viewPager = findViewById(R.id.viewPager_game);
		viewPager.setAdapter(gameScreenAdapter);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
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
		bottomBar.setItemIconTintList(null);
		bottomBar.setOnNavigationItemSelectedListener(this::onScreenItemSelected);
		BottomNavigationBarHelper.disableShiftAnimation(bottomBar);
		
		tryToStartVoiceCommunication();
	}
	
	private void loadGameData()
	{
		GameData gameData = GameData.getGameData();
		if(gameData == null || gameData.getClient() == null) return;
		ImagesSet imagesSet = new ImagesSet(this, gameData.getImagesCode());
		gameLogic = new GameLogic(gameData.getClient(), gameData.getUsers(), gameData.getTextChat(), gameData.getServerCode(), imagesSet);
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
	
	private void tryToStartVoiceCommunication()
	{
		if(!Settings.isVoiceChatEnabled(this)) return;
		bolekApplication.bindToVoiceService(service -> {
			if(service != null)
				PermissionRequest.requestPermission(this, Manifest.permission.RECORD_AUDIO, () -> gameLogic.startVoiceCommunication(service));
			else Toast.makeText(ActivityGame.this, R.string.message_voice_chat_network_error, Toast.LENGTH_LONG).show();
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		gameLogic.resume(this); //On orientation changes
		if(gameLogic.isNotificationEnabled()) setTextChatNotification();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(!isFinishing()) //On orientation changes
		{
			gameLogic.suspend();
			dialogManager.dismissAll();
			if(finish) finish();
		}
		else bolekApplication.unbindFromVoiceService();
	}
	
	private boolean onScreenItemSelected(MenuItem menuItem)
	{
		int fragmentId = gameScreenAdapter.getScreenPositionByItemId(menuItem.getItemId());
		if(fragmentId == -1) return false;
		viewPager.setCurrentItem(fragmentId);
		if(fragmentId != GameScreenAdapter.ITEM_ACTS)
		{
			ScreenActs acts = getScreenActs();
			if(acts != null) acts.closeAllHints();
		}
		
		if(fragmentId == GameScreenAdapter.ITEM_PLAYERS)
		{
			ScreenPlayers players = getScreenPlayers();
			if(players != null) players.updateVoiceChatControls();
		}
		else if(fragmentId == GameScreenAdapter.ITEM_CHAT)
		{
			menuItem.setIcon(R.drawable.ic_menu_text_chat);
			gameLogic.setTextChatNotification(false);
		}
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
		dialog.setMessage(gameLogic.willGameBeEndedAfterMyLeave() ? R.string.dialog_leave_are_you_sure_detail_game_end : R.string.dialog_leave_are_you_sure_detail);
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
		finish = true;
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
		
		setTextChatNotification();
	}
	
	private void setTextChatNotification()
	{
		if(viewPager.getCurrentItem() == GameScreenAdapter.ITEM_CHAT) return;
		gameLogic.setTextChatNotification(true);
		setTextChatNotificationIcon();
	}
	
	private void setTextChatNotificationIcon()
	{
		Menu menu = bottomBar.getMenu();
		MenuItem item = menu.findItem(R.id.item_game_chat);
		item.setIcon(R.drawable.ic_menu_text_chat_attention_black_24dp);
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
	
	void onYouAreLustrated(String president)
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(getString(R.string.dialog_you_are_lustrated, president));
		dialog.setMessage(R.string.dialog_you_are_lustrated_detail);
		dialog.setPositiveButton(R.string.button_ok, (d, w) -> finish());
		dialog.setUncancelable();
		dialog.commit();
		finish = true;
	}
	
	void onGameExit()
	{
		finish();
	}
	
	void onTooFewPlayers()
	{
		Dialog dialog = new Dialog(dialogManager);
		dialog.setTitle(R.string.dialog_too_few_players);
		dialog.setMessage(R.string.dialog_too_few_players_detail);
		dialog.setPositiveButton(R.string.button_ok, (d, w) -> finish());
		dialog.setUncancelable();
		dialog.commit();
		finish = true;
	}
	
	@Override
	public GameLogic getGameLogic()
	{
		return gameLogic;
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
