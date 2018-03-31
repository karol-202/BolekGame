package pl.karol202.bolekgame.game.dialog;

import android.content.Context;

import java.util.LinkedList;
import java.util.Queue;

public class DialogManager
{
	private Context context;
	private Dialog currentDialog;
	private Queue<Dialog> dialogQueue;
	
	public DialogManager(Context context)
	{
		this.context = context;
		this.dialogQueue = new LinkedList<>();
	}
	
	void commitDialog(Dialog dialog)
	{
		if(currentDialog != null) dialogQueue.offer(dialog);
		else if(dialog != null)
		{
			currentDialog = dialog;
			currentDialog.show();
		}
	}
	
	void onDialogDismiss(Dialog dialog)
	{
		if(dialog != currentDialog) return;
		currentDialog = null;
		commitDialog(dialogQueue.poll());
	}
	
	public void dismissAll()
	{
		if(currentDialog != null) currentDialog.dismiss();
		dialogQueue.clear();
	}
	
	Context getContext()
	{
		return context;
	}
}
