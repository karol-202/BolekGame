package pl.karol202.bolekgame.utils;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Checkable;

public class CheckableLayout extends ConstraintLayout implements Checkable
{
	public interface OnCheckedChangeListener
	{
		void onCheckedChange(boolean checked);
	}
	
	private static final int[] checkedState = new int[] { android.R.attr.state_checked };
	
	private OnCheckedChangeListener checkListener;
	private boolean checked;
	
	public CheckableLayout(Context context)
	{
		super(context);
	}
	
	public CheckableLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CheckableLayout(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	public boolean performClick()
	{
		toggle();
		return super.performClick();
	}
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace)
	{
		int[] state = super.onCreateDrawableState(extraSpace + 1);
		if(checked) mergeDrawableStates(state, checkedState);
		return state;
	}
	
	@Override
	public void setChecked(boolean checked)
	{
		this.checked = checked;
		if(checkListener != null) checkListener.onCheckedChange(checked);
	}
	
	@Override
	public boolean isChecked()
	{
		return checked;
	}
	
	@Override
	public void toggle()
	{
		setChecked(!checked);
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
	{
		checkListener = listener;
	}
}
