package pl.karol202.bolekgame.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.acts.ScreenActs;
import pl.karol202.bolekgame.game.chat.ScreenChat;
import pl.karol202.bolekgame.game.main.ScreenMain;
import pl.karol202.bolekgame.game.players.ScreenPlayers;

class GameScreenAdapter extends FragmentPagerAdapter
{
	static final int ITEM_MAIN = 0;
	static final int ITEM_ACTS = 1;
	static final int ITEM_PLAYERS = 2;
	static final int ITEM_CHAT = 3;
	
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
		case ITEM_MAIN: return new ScreenMain();
		case ITEM_ACTS: return new ScreenActs();
		case ITEM_PLAYERS: return new ScreenPlayers();
		case ITEM_CHAT: return new ScreenChat();
		}
		return null;
	}
	
	@Override
	public int getCount()
	{
		return 4;
	}
	
	@NonNull
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
		case R.id.item_game_main: return ITEM_MAIN;
		case R.id.item_game_acts: return ITEM_ACTS;
		case R.id.item_game_players: return ITEM_PLAYERS;
		case R.id.item_game_chat: return ITEM_CHAT;
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
		case ITEM_MAIN: return R.id.item_game_main;
		case ITEM_ACTS: return R.id.item_game_acts;
		case ITEM_PLAYERS: return R.id.item_game_players;
		case ITEM_CHAT: return R.id.item_game_chat;
		}
		return -1;
	}
}
