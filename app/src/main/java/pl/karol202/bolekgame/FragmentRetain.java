package pl.karol202.bolekgame;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import pl.karol202.bolekgame.client.Client;

public class FragmentRetain extends Fragment
{
	private Client client;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public Client getClient()
	{
		if(client == null) client = new Client();
		else client = client.recreateClient();
		return client;
	}
	
	public void setClient(Client client)
	{
		this.client = client;
	}
}
