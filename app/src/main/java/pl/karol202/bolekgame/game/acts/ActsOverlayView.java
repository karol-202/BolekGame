package pl.karol202.bolekgame.game.acts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ActsOverlayView extends View
{
	public ActsOverlayView(Context context)
	{
		this(context, null);
	}
	
	public ActsOverlayView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public ActsOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
}
