package pl.karol202.bolekgame.game.screen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.game.GameLogic;
import pl.karol202.bolekgame.game.GameLogicSupplier;

public abstract class Screen extends Fragment
{
	private GameLogicSupplier gameLogicSupplier;
	protected GameLogic gameLogic;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		init(activity);
	}
	
	private void init(Context context)
	{
		if(!(context instanceof GameLogicSupplier))
			throw new IllegalStateException("Activity using this fragment must be GameLogicSupplier.");
		gameLogicSupplier = (GameLogicSupplier) context;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		gameLogic = gameLogicSupplier.getGameLogic();
	}
}
