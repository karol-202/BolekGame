package pl.karol202.bolekgame.game.screen.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.screen.Screen;

public class ScreenChat extends Screen
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_chat, container, false);
		
		return view;
	}
}
