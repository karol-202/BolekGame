package pl.karol202.bolekgame.utils;

import android.os.Handler;
import android.os.Looper;

public class Utils
{
	public static void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
}
