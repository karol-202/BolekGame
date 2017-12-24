package pl.karol202.bolekgame.ui.control;

import android.os.Handler;
import android.os.Looper;
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
		client.setControlUI(this);
		client.setOnDisconnectListener(this::onDisconnect);
	}
	
	void connectAsync(String host)
	{
		new Thread(() -> connect(host)).start();
	}
	
	private void connect(String host)
	{
		boolean result = client.connect(host);
		if(result)
		{
			client.run();
			runInUIThread(() -> activityMain.onConnect());
		}
		else runInUIThread(() -> activityMain.onConnectFail());
	}
	
	boolean isConnected()
	{
		return client.isConnected();
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
		runInUIThread(() -> activityMain.onLoggedIn(serverName, serverCode));
	}
	
	public void onFailure()
	{
		runInUIThread(() -> activityMain.onCannotLogIn());
	}
	
	private void onDisconnect()
	{
		runInUIThread(() -> activityMain.onDisconnect());
	}
	
	private void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
}
