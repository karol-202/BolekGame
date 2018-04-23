package pl.karol202.bolekgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import pl.karol202.bolekgame.R;

public class AspectImageView extends android.support.v7.widget.AppCompatImageView
{
	private float aspect;
	
	public AspectImageView(Context context)
	{
		this(context, null);
	}
	
	public AspectImageView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public AspectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectImageView);
		aspect = array.getFloat(R.styleable.AspectImageView_aspect, 1f);
		array.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = Math.round(width * aspect);
		setMeasuredDimension(width, height);
	}
}
