package pl.karol202.bolekgame.game.main;

import android.content.Context;
import pl.karol202.bolekgame.game.ImagesSet;

public interface ContextSupplier
{
	Context getContext();
	
	String getString(int resId, Object... formatArgs);
	
	ImagesSet getImagesSet();
}
