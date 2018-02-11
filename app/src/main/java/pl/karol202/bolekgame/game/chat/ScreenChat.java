package pl.karol202.bolekgame.game.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;

public class ScreenChat extends Screen
{
	private TextView textChat;
	private EditText editTextChat;
	private ImageButton buttonSend;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_chat, container, false);
		
		textChat = view.findViewById(R.id.text_chat);
		
		editTextChat = view.findViewById(R.id.editText_chat);
		editTextChat.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
			@Override
			public void afterTextChanged(Editable s)
			{
				onMessageEdit(s.toString());
			}
		});
		editTextChat.setOnEditorActionListener((v, actionId, event) -> onMessageEditDone(actionId));
		
		buttonSend = view.findViewById(R.id.button_text_chat_send);
		buttonSend.setOnClickListener(v -> sendTextChatMessage());
		buttonSend.setEnabled(false);
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		onChatUpdate();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(editTextChat == null) return;
		editTextChat.setOnEditorActionListener(null);
	}
	
	private void onMessageEdit(String message)
	{
		buttonSend.setEnabled(message != null && !message.isEmpty());
	}
	
	private boolean onMessageEditDone(int actionId)
	{
		if(actionId != EditorInfo.IME_ACTION_DONE) return false;
		sendTextChatMessage();
		return true;
	}
	
	private void sendTextChatMessage()
	{
		String message = editTextChat.getText().toString();
		if(message.isEmpty()) return;
		gameLogic.sendMessage(message);
		editTextChat.setText(null);
		hideKeyboard(editTextChat);
	}
	
	private void hideKeyboard(TextView v)
	{
		InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if(manager != null) manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public void onChatUpdate()
	{
		textChat.setText(gameLogic.getTextChatString());
	}
}
