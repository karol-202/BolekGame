package pl.karol202.bolekgame.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import pl.karol202.bolekgame.R;

public class AnimatedImageButton extends android.support.v7.widget.AppCompatImageButton
{
	private AnimatedVectorDrawableCompat drawable;
	private AnimatedVectorDrawableCompat drawableReversed;
	private boolean toggled;
	
	public AnimatedImageButton(Context context)
	{
		this(context, null);
	}
	
	public AnimatedImageButton(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.imageButtonStyle);
	}
	
	public AnimatedImageButton(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnimatedImageButton, defStyleAttr, 0);
		drawable = AnimatedVectorDrawableCompat.create(context, array.getResourceId(R.styleable.AnimatedImageButton_animatedDrawable, 0));
		drawableReversed = AnimatedVectorDrawableCompat.create(context, array.getResourceId(R.styleable.AnimatedImageButton_animatedDrawableReversed, 0));
		array.recycle();
		
		setImageDrawable(drawable);
	}
	
	public void toggleAnimation(boolean toggled)
	{
		if(this.toggled == toggled) return;
		this.toggled = toggled;
		AnimatedVectorDrawableCompat current = toggled ? drawableReversed : drawable;
		setImageDrawable(current);
		current.start();
	}
}
