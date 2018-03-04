package pl.karol202.bolekgame.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteUser extends User
{
	private InetAddress address;
	
	RemoteUser(String name, boolean ready, String address)
	{
		super(name, ready);
		createInetAddress(address);
	}
	
	private void createInetAddress(String address)
	{
		try
		{
			this.address = InetAddress.getByName(address);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
	
	public InetAddress getAddress()
	{
		return address;
	}
}
