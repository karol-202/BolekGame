package pl.karol202.bolekgame.game;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

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
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		init(context);
	}
	
	private void init(Context context)
	{
		if(!(context instanceof GameLogicSupplier))
			throw new IllegalStateException("Activity using this fragment must be GameLogicSupplier.");
		gameLogicSupplier = (GameLogicSupplier) context;
	}
	
	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		gameLogic = gameLogicSupplier.getGameLogic();
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		gameLogicSupplier = null;
		gameLogic = null;
	}
}
