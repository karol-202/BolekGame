package pl.karol202.bolekgame.server;

public class User
{
	private String name;
	private boolean ready;
	private String address;
	
	User(String name)
	{
		this.name = name;
		this.ready = false;
	}
	
	User(String name, boolean ready, String address)
	{
		this.name = name;
		this.ready = ready;
		this.address = address;
	}
	
	String getName()
	{
		return name;
	}
	
	boolean isReady()
	{
		return ready;
	}
	
	void setReady(boolean ready)
	{
		this.ready = ready;
	}
	
	public String getAddress()
	{
		return address;
	}
}
