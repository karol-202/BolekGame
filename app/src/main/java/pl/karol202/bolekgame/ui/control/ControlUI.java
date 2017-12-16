package pl.karol202.bolekgame.ui.control;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;

public class ControlUI
{
	private ActivityMain activityMain;
	private Client client;
	
	ControlUI(ActivityMain activityMain)
	{
		this.activityMain = activityMain;
		
		client = new Client();
		client.setOnDisconnectListener(this::onDisconnect);
	}
	
	boolean connect(String host)
	{
		boolean result = client.connect(host);
		if(result) client.run();
		return result;
	}
	
	void login(int serverCode, String username)
	{
		client.sendPacket(new OutputPacketLogin(serverCode, username));
	}
	
	void createServer(String serverName, String username)
	{
		client.sendPacket(new OutputPacketCreateServer(serverName, username));
	}
	
	public void onLoggedIn(String serverName, int serverCode)
	{
		activityMain.onLoggedIn(serverName, serverCode);
	}
	
	public void onFailure()
	{
	
	}
	
	private void onDisconnect()
	{
		activityMain.onDisconnect();
	}
}
