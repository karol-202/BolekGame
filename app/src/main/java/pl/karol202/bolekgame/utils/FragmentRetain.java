package pl.karol202.bolekgame.utils;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class FragmentRetain<L extends Logic> extends Fragment
{
	private L logic;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public L getLogic()
	{
		return logic;
	}
	
	public void setLogic(L logic)
	{
		this.logic = logic;
	}
}
