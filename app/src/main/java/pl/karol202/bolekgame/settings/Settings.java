package pl.karol202.bolekgame.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.vdurmont.emoji.EmojiParser;

public class Settings
{
	public enum NickCorrectness
	{
		VALID, INVALID_LENGTH, INVALID_CHARS
	}
	
	private static final String KEY_INITIALIZED = "preference_initialized";
	public static final String KEY_NICK = "preference_nick";
	private static final String KEY_SERVER_ADDRESS = "preference_server_address_2";
	private static final String KEY_VOICE_CHAT = "preference_voice_chat";
	
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
	
	public static NickCorrectness isNickCorrect(String nick)
	{
		if(nick.length() < 3 || nick.length() > 20) return NickCorrectness.INVALID_LENGTH;
		else if(EmojiParser.extractEmojis(nick).size() != 0) return NickCorrectness.INVALID_CHARS;
		else return NickCorrectness.VALID;
	}
	
	public static String getServerAddress(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(KEY_SERVER_ADDRESS, null);
	}
	
	public static boolean isVoiceChatEnabled(Context context)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(KEY_VOICE_CHAT, false);
	}
}
