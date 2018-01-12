package pl.karol202.bolekgame.server;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.client.Client;

public class FragmentRetain extends Fragment
{
	private Client client;
	private String serverName;
	private int serverCode;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	Client getClient()
	{
		if(client == null) client = new Client();
		else client = client.recreateClient();
		return client;
	}
	
	public String getServerName()
	{
		return serverName;
	}
	
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
	
	public void setServerCode(int serverCode)
	{
		this.serverCode = serverCode;
	}
}
