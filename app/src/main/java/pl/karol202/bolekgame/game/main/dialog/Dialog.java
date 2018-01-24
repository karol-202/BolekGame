package pl.karol202.bolekgame.game.main.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class Dialog
{
	private DialogManager dialogManager;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	
	public Dialog(DialogManager dialogManager)
	{
		this.dialogManager = dialogManager;
		this.builder = new AlertDialog.Builder(dialogManager.getContext());
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
		builder.setPositiveButton(text, (d, w) -> {
			if(listener != null) listener.onClick(d, w);
			dialogManager.onDialogDismiss(this);
		});
	}
	
	public void setNegativeButton(int text, DialogInterface.OnClickListener listener)
	{
		builder.setNegativeButton(text, (d, w) -> {
			if(listener != null) listener.onClick(d, w);
			dialogManager.onDialogDismiss(this);
		});
	}
	
	public void setCancelable(boolean cancelable)
	{
		builder.setCancelable(cancelable);
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
