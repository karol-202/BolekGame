package pl.karol202.bolekgame.view;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

public class BottomNavigationBarHelper
{
	@SuppressLint("RestrictedApi")
	public static void disableShiftAnimation(BottomNavigationView view)
	{
		BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
		try
		{
			Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
			shiftingMode.setAccessible(true);
			shiftingMode.setBoolean(menuView, false);
			shiftingMode.setAccessible(false);
			for (int i = 0; i < menuView.getChildCount(); i++)
			{
				BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
				item.setShiftingMode(false);
				item.setChecked(item.getItemData().isChecked());
			}
		}
		catch (Exception ignored) { }
	}
}
