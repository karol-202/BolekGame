package pl.karol202.bolekgame.control;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFailure;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;

public class ControlLogic
{
	private static final int TIMEOUT = 2000;
	
	private ActivityMain activityMain;
	private Client client;
	
	private TimeoutRunnable loginTimeout;
	private boolean creatingServer;
	private boolean loggingIn;
	
	ControlLogic(ActivityMain activityMain, Client client)
	{
		this.activityMain = activityMain;
		this.client = client;
		
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
		loginTimeout = new TimeoutRunnable(TIMEOUT, () -> onFailure(0));
		loggingIn = true;
	}
	
	void createServer(String serverName, String username)
	{
		client.sendPacket(new OutputPacketCreateServer(serverName, username));
		loginTimeout = new TimeoutRunnable(TIMEOUT, () -> onFailure(0));
		creatingServer = true;
	}
	
	public void onLoggedIn(String serverName, int serverCode)
	{
		runInUIThread(() -> activityMain.onLoggedIn(serverName, serverCode));
		loginTimeout.interrupt();
		creatingServer = false;
		loggingIn = false;
	}
	
	public void onFailure(int problem)
	{
		loginTimeout.interrupt();
		if(creatingServer) onServerCreatingFailure(problem);
		else if(loggingIn) onLoggingFailure(problem);
		creatingServer = false;
		loggingIn = false;
	}
	
	private void onServerCreatingFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_SERVER_INVALID_NAME)
			runInUIThread(() -> activityMain.onInvalidServerNameError());
		else if(problem == InputPacketFailure.PROBLEM_SERVER_TOO_MANY)
			runInUIThread(() -> activityMain.onTooManyServersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			runInUIThread(() -> activityMain.onInvalidUsernameError());
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			runInUIThread(() -> activityMain.onTooManyUsersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			runInUIThread(() -> activityMain.onUsernameBusyError());
		else runInUIThread(() -> activityMain.onCannotCreateServer());
	}
	
	private void onLoggingFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_SERVER_CODE_INVALID)
			runInUIThread(() -> activityMain.onInvalidServerCode());
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			runInUIThread(() -> activityMain.onInvalidUsernameError());
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			runInUIThread(() -> activityMain.onTooManyUsersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			runInUIThread(() -> activityMain.onUsernameBusyError());
		else runInUIThread(() -> activityMain.onCannotLogIn());
	}
	
	private void onDisconnect()
	{
		runInUIThread(() -> activityMain.onDisconnect());
	}
	
	private void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
	
	Client getClient()
	{
		return client;
	}
}
