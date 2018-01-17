package pl.karol202.bolekgame.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.screen.ScreenActs;
import pl.karol202.bolekgame.game.screen.ScreenChat;
import pl.karol202.bolekgame.game.screen.ScreenMain;
import pl.karol202.bolekgame.game.screen.ScreenPlayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GameScreenAdapter extends FragmentPagerAdapter
{
	private List<Fragment> screens;
	private Map<Integer, Fragment> screensItems;
	
	GameScreenAdapter(FragmentManager fm)
	{
		super(fm);
		screens = new ArrayList<>();
		screensItems = new HashMap<>();
		
		addScreen(R.id.item_game_main, new ScreenMain());
		addScreen(R.id.item_game_acts, new ScreenActs());
		addScreen(R.id.item_game_players, new ScreenPlayers());
		addScreen(R.id.item_game_chat, new ScreenChat());
	}
	
	@Override
	public Fragment getItem(int position)
	{
		return screens.get(position);
	}
	
	@Override
	public int getCount()
	{
		return screens.size();
	}
	
	private void addScreen(int itemId, Fragment fragment)
	{
		screens.add(fragment);
		screensItems.put(itemId, fragment);
	}
	
	int getFragmentIdByItemId(int itemId)
	{
		return screens.indexOf(screensItems.get(itemId));
	}
}
