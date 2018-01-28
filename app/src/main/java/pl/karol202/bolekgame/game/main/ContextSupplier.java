package pl.karol202.bolekgame.game.main;

import android.content.Context;

public interface ContextSupplier
{
	Context getContext();
	
	String getString(int resId, Object... formatArgs);
}
