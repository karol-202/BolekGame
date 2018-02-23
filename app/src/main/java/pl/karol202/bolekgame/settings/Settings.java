package pl.karol202.bolekgame.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings
{
	public static final String KEY_INITIALIZED = "preference_initialized";
	public static final String KEY_NICK = "preference_nick";
	public static final String KEY_SERVER_ADDRESS = "preference_server_address";
	
	public static boolean isFirstStart(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return !preferences.getBoolean(KEY_INITIALIZED, false);
	}
	
	public static void saveFirstStart(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_INITIALIZED, true);
		editor.apply();
	}
	
	public static String getNick(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(KEY_NICK, null);
	}
	
	public static void setNick(Context context, String nick)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(KEY_NICK, nick);
		editor.apply();
	}
	
	public static boolean isValidNick(String nick)
	{
		return nick.length() >= 3 && nick.length() <= 20;
	}
	
	public static String getServerAddress(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(KEY_SERVER_ADDRESS, null);
	}
}
