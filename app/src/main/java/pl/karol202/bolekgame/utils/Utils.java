package pl.karol202.bolekgame.utils;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class Utils
{
	static void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
	
	public static Rect getViewRectRelativeToRoot(View root, View view)
	{
		Point point = new Point();
		View current = view;
		while(current != root)
		{
			point.offset(current.getLeft(), current.getTop());
			current = (View) current.getParent();
		}
		return new Rect(point.x, point.y, point.x + view.getWidth(), point.y + view.getHeight());
	}
}
