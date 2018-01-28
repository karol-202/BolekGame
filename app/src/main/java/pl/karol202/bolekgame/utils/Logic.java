package pl.karol202.bolekgame.utils;

import android.app.Activity;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;

public abstract class Logic<A extends Activity> extends ClientListenerAdapter
{
	protected A activity;
	protected Client client;
	
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
		this.activity = activity;
	}
}
