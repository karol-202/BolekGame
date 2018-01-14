package pl.karol202.bolekgame.server;

class User
{
	private String name;
	private boolean ready;
	
	User(String name)
	{
		this.name = name;
		this.ready = false;
	}
	
	User(String name, boolean ready)
	{
		this.name = name;
		this.ready = ready;
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
}
