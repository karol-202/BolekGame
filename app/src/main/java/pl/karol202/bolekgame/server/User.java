package pl.karol202.bolekgame.server;

public abstract class User
{
	private String name;
	private boolean ready;
	
	User(String name, boolean ready)
	{
		this.name = name;
		this.ready = ready;
	}
	
	public String getName()
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
}
