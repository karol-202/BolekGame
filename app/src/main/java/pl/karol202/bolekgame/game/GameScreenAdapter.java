package pl.karol202.bolekgame.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.screen.Screen;
import pl.karol202.bolekgame.game.screen.acts.ScreenActs;
import pl.karol202.bolekgame.game.screen.chat.ScreenChat;
import pl.karol202.bolekgame.game.screen.main.ScreenMain;
import pl.karol202.bolekgame.game.screen.players.ScreenPlayers;

class GameScreenAdapter extends FragmentPagerAdapter
{
	private SparseArray<Screen> screens;
	
	GameScreenAdapter(FragmentManager fm)
	{
		super(fm);
		screens = new SparseArray<>();
	}
	
	@Override
	public Fragment getItem(int position)
	{
		switch(position)
		{
		case 0: return new ScreenMain();
		case 1: return new ScreenActs();
		case 2: return new ScreenPlayers();
		case 3: return new ScreenChat();
		}
		return null;
	}
	
	@Override
	public int getCount()
	{
		return 4;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Screen screen = (Screen) super.instantiateItem(container, position);
		screens.put(position, screen);
		return screen;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		screens.remove(position);
		super.destroyItem(container, position, object);
	}
	
	int getScreenPositionByItemId(int itemId)
	{
		switch(itemId)
		{
		case R.id.item_game_main: return 0;
		case R.id.item_game_acts: return 1;
		case R.id.item_game_players: return 2;
		case R.id.item_game_chat: return 3;
		}
		return -1;
	}
	
	Screen getScreenOnPosition(int position)
	{
		return screens.get(position);
	}
	
	int getItemIdOfScreenAtPosition(int screenPosition)
	{
		switch(screenPosition)
		{
		case 0: return R.id.item_game_main;
		case 1: return R.id.item_game_acts;
		case 2: return R.id.item_game_players;
		case 3: return R.id.item_game_chat;
		}
		return -1;
	}
}
