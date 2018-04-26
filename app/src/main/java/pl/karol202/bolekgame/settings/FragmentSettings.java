package pl.karol202.bolekgame.settings;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.R;

public class FragmentSettings extends PreferenceFragment
{
	private String preferenceToEdit;
	private SharedPreferences.OnSharedPreferenceChangeListener changeListener;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		loadData();
		
		EditTextPreference nickPreference = (EditTextPreference) findPreference(Settings.KEY_NICK);
		nickPreference.setOnPreferenceChangeListener((preference, newValue) -> validateNick((String) newValue));
		
		SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
		updateAllPreferences();
		changeListener = (p, k) -> onSharedPreferenceChange(k);
		preferences.registerOnSharedPreferenceChangeListener(changeListener);
	}
	
	private void loadData()
	{
		Bundle args = getArguments();
		if(args == null) return;
		preferenceToEdit = args.getString("preferenceToEdit");
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
		
		editTextPreference.setSummary(editTextPreference.getText());
		editTextPreference.setPositiveButtonText(R.string.button_nick_change_apply);
		editTextPreference.setNegativeButtonText(R.string.button_cancel);
		
		if(preference instanceof CallableEditTextPreference && preference.getKey().equals(preferenceToEdit))
			((CallableEditTextPreference) preference).showDialog(null);
	}
	
	private boolean validateNick(String nick)
	{
		Settings.NickCorrectness nickCorrectness = Settings.isNickCorrect(nick);
		if(nickCorrectness == Settings.NickCorrectness.INVALID_LENGTH) showInvalidNickLengthDialog();
		else if(nickCorrectness == Settings.NickCorrectness.INVALID_CHARS) showInvalidNickCharsDialog();
		else return true;
		return false;
	}
	
	private void showInvalidNickLengthDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_invalid_nick);
		builder.setMessage(R.string.dialog_invalid_nick_length_detail);
		builder.setPositiveButton(R.string.button_ok, null);
		builder.show();
	}
	
	private void showInvalidNickCharsDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_invalid_nick);
		builder.setMessage(R.string.dialog_invalid_nick_chars_detail);
		builder.setPositiveButton(R.string.button_ok, null);
		builder.show();
	}
}
