package pl.karol202.bolekgame.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings
{
	public static final String KEY_NICK = "preference_nick";
	public static final String KEY_SERVER_ADDRESS = "preference_server_address";
	
	public static String getNick(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(KEY_NICK, null);
	}
	
	public static String getServerAddress(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(KEY_SERVER_ADDRESS, null);
	}
}
