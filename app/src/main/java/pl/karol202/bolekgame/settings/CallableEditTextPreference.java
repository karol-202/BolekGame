package pl.karol202.bolekgame.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class CallableEditTextPreference extends EditTextPreference
{
	public CallableEditTextPreference(Context context)
	{
		super(context);
	}
	
	public CallableEditTextPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CallableEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	public void showDialog(Bundle state)
	{
		super.showDialog(state);
	}
}
