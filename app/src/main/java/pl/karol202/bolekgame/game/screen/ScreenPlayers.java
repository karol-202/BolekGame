package pl.karol202.bolekgame.game.screen;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;

public class ScreenPlayers extends Fragment
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_players, container, false);
		
		return view;
	}
}
