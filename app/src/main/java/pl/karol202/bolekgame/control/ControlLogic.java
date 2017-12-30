package pl.karol202.bolekgame.control;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;

public class ControlLogic
{
	private static final int TIMEOUT = 2000;
	
	private ActivityMain activityMain;
	private Client client;
	
	private TimeoutRunnable loginTimeout;
	
	ControlLogic(ActivityMain activityMain)
	{
		this.activityMain = activityMain;
		
		client = new Client();
		client.setControlLogic(this);
		client.setOnDisconnectListener(this::onDisconnect);
	}
	
	void connect(String host)
	{
		new Thread(() -> connectAndWait(host)).start();
	}
	
	private void connectAndWait(String host)
	{
		boolean result = client.connect(host);
		if(result)
		{
			client.run();
			runInUIThread(activityMain::onConnect);
		}
		else runInUIThread(activityMain::onConnectFail);
	}
	
	boolean isConnected()
	{
		return client.isConnected();
	}
	
	void login(int serverCode, String username)
	{
		client.sendPacket(new OutputPacketLogin(serverCode, username));
		loginTimeout = new TimeoutRunnable(TIMEOUT, this::onFailure);
	}
	
	void createServer(String serverName, String username)
	{
		client.sendPacket(new OutputPacketCreateServer(serverName, username));
		loginTimeout = new TimeoutRunnable(TIMEOUT, this::onFailure);
	}
	
	
	public void onLoggedIn(String serverName, int serverCode)
	{
		loginTimeout.interrupt();
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
