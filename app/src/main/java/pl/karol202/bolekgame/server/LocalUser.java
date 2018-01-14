package pl.karol202.bolekgame.server;

class LocalUser extends User
{
	private ServerLogic serverLogic;
	
	LocalUser(String name, ServerLogic serverLogic)
	{
		super(name);
		this.serverLogic = serverLogic;
	}
	
	void changeReadiness(boolean ready)
	{
		if(isReady() == ready) return;
		setReady(ready);
		serverLogic.setReady();
	}
}
