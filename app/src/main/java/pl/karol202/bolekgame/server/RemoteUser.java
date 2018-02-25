package pl.karol202.bolekgame.server;

public class RemoteUser extends User
{
	private String address;
	
	RemoteUser(String name, boolean ready, String address)
	{
		super(name, ready);
		this.address = address;
	}
	
	public String getAddress()
	{
		return address;
	}
}
