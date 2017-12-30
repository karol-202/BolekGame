package pl.karol202.bolekgame.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.R;

public class FragmentSettings extends PreferenceFragment
{
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
		updateAllPreferences();
		preferences.registerOnSharedPreferenceChangeListener((p, k) -> onSharedPreferenceChange(k));
	}
	
	private void updateAllPreferences()
	{
		for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++)
			updatePreference(getPreferenceScreen().getPreference(i));
	}
	
	private void onSharedPreferenceChange(String key)
	{
		updatePreference(findPreference(key));
	}
	
	private void updatePreference(Preference preference)
	{
		if(!(preference instanceof EditTextPreference)) return;
		EditTextPreference editTextPreference = (EditTextPreference) preference;
		
		editTextPreference.setSummary(((EditTextPreference) preference).getText());
		editTextPreference.setPositiveButtonText(R.string.button_nick_change_apply);
		editTextPreference.setNegativeButtonText(R.string.button_cancel);
	}
}
