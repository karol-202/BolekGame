package pl.karol202.bolekgame.server;

class LocalUser extends User
{
	private ServerLogic serverLogic;
	
	LocalUser(String name, ServerLogic serverLogic)
	{
		super(name);
		this.serverLogic = serverLogic;
	}
	
	@Override
	void setReady(boolean ready)
	{
		super.setReady(ready);
		serverLogic.setReady();
	}
}
