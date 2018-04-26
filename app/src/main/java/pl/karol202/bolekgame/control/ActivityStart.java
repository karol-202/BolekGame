package pl.karol202.bolekgame.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.settings.Settings;

public class ActivityStart extends AppCompatActivity
{
	private ViewGroup mainLayout;
	private TextInputLayout layoutNick;
	private TextInputEditText editTextNick;
	private Button buttonDone;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		mainLayout = findViewById(R.id.main_layout);
		
		layoutNick = findViewById(R.id.editTextLayout_nick);
		
		editTextNick = findViewById(R.id.editText_nick);
		editTextNick.setOnEditorActionListener((v, actionId, event) -> onNickEditingDone(actionId));
		
		buttonDone = findViewById(R.id.button_nick_accept);
		buttonDone.setOnClickListener(v -> done());
	}
	
	private boolean onNickEditingDone(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		done();
		return true;
	}
	
	private void done()
	{
		String nick = editTextNick.getText().toString();
		Settings.NickCorrectness nickCorrectness = Settings.isNickCorrect(nick);
		if(nickCorrectness == Settings.NickCorrectness.INVALID_LENGTH)
		{
			TransitionManager.beginDelayedTransition(mainLayout);
			layoutNick.setError(getString(R.string.message_invalid_nick_length));
		}
		else if(nickCorrectness == Settings.NickCorrectness.INVALID_CHARS)
		{
			TransitionManager.beginDelayedTransition(mainLayout);
			layoutNick.setError(getString(R.string.message_invalid_nick_chars));
		}
		else
		{
			Settings.setNick(this, nick);
			Settings.saveFirstStart(this);
			finish();
		}
	}
}
