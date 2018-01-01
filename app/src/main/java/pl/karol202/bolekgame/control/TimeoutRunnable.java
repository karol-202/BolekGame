package pl.karol202.bolekgame.control;

import android.os.Handler;

public class TimeoutRunnable
{
	private int timeoutTime;
	private Runnable timeoutTask;
	private boolean interrupt;
	
	TimeoutRunnable(int timeoutTime, Runnable timeoutTask)
	{
		this.timeoutTime = timeoutTime;
		this.timeoutTask = timeoutTask;
		run();
	}
	
	private void run()
	{
		Handler handler = new Handler();
		handler.postDelayed(() -> { if(!interrupt) timeoutTask.run(); }, timeoutTime);
	}
	
	public void interrupt()
	{
		interrupt = true;
	}
}
