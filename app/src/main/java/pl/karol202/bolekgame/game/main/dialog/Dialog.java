package pl.karol202.bolekgame.game.main.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Dialog
{
	private DialogManager dialogManager;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	
	public Dialog(DialogManager dialogManager)
	{
		this.dialogManager = dialogManager;
		builder = new AlertDialog.Builder(dialogManager.getContext());
		builder.setOnDismissListener(d -> dialogManager.onDialogDismiss(this));
	}
	
	public void setTitle(String title)
	{
		builder.setTitle(title);
	}
	
	public void setTitle(int title)
	{
		builder.setTitle(title);
	}
	
	public void setMessage(String message)
	{
		builder.setMessage(message);
	}
	
	public void setMessage(int message)
	{
		builder.setMessage(message);
	}
	
	public void setPositiveButton(int text, DialogInterface.OnClickListener listener)
	{
		builder.setPositiveButton(text, listener);
	}
	
	public void setNegativeButton(int text)
	{
		builder.setNegativeButton(text, null);
	}
	
	public void setUncancelable()
	{
		builder.setCancelable(false);
	}
	
	public void commit()
	{
		dialog = builder.create();
		dialogManager.commitDialog(this);
	}
	
	void show()
	{
		if(dialog != null) dialog.show();
	}
}
