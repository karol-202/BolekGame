package pl.karol202.bolekgame.control;

public class TimeoutRunnable implements Runnable
{
	private int timeoutTime;
	private Runnable timeoutTask;
	private boolean interrupt;
	
	TimeoutRunnable(int timeoutTime, Runnable timeoutTask)
	{
		this.timeoutTime = timeoutTime;
		this.timeoutTask = timeoutTask;
		new Thread(this).start();
	}
	
	@Override
	public void run()
	{
		try
		{
			this.wait(timeoutTime);
			if(!interrupt) timeoutTask.run();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	void interrupt()
	{
		interrupt = true;
		this.notify();
	}
}
