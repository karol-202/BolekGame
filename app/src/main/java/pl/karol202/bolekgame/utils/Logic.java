package pl.karol202.bolekgame.utils;

import android.app.Activity;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;

public abstract class Logic<A extends Activity> extends ClientListenerAdapter
{
	protected A activity;
	protected Client client;
	
	public void resumeClient()
	{
		client.setClientListener(this);
	}
	
	public void suspendClient()
	{
		client.setClientListener(null);
	}
	
	protected void sendPacket(OutputPacket packet)
	{
		new NetworkingAsyncTask(client).execute(packet);
	}
	
	public void setActivity(A activity)
	{
		this.activity = activity;
	}
}
