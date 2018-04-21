package pl.karol202.bolekgame.utils;

import android.app.Activity;
import java8.util.function.Consumer;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;

public abstract class Logic<A extends Activity> extends ClientListenerAdapter
{
	private final Object activityLock = new Object();
	
	private A activity;
	private Client client;
	
	protected Logic(Client client)
	{
		this.client = client;
	}
	
	public void resume(A activity)
	{
		setActivity(activity);
		resumeClient();
	}
	
	public void suspend()
	{
		suspendClient();
		setActivity(null);
	}
	
	private void resumeClient()
	{
		client.setClientListener(this);
	}
	
	protected void suspendClient()
	{
		client.setClientListener(null);
	}
	
	protected void sendPacket(OutputPacket packet)
	{
		new NetworkingAsyncTask(client).execute(packet);
	}
	
	protected void runInUIThread(Runnable runnable)
	{
		Utils.runInUIThread(runnable);
	}
	
	protected void setActivity(A activity)
	{
		synchronized(activityLock)
		{
			this.activity = activity;
		}
	}
	
	protected void executeOnActivity(Consumer<A> action)
	{
		synchronized(activityLock)
		{
			if(activity == null) return;
			action.accept(activity);
		}
	}
	
	protected void executeOnActivityInUIThread(Consumer<A> action)
	{
		runInUIThread(() -> {
			synchronized(activityLock)
			{
				if(activity == null) return;
				action.accept(activity);
			}
		});
	}
	
	protected Client getClient()
	{
		return client;
	}
}
