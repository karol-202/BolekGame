package pl.karol202.bolekgame.control;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.ClientListenerAdapter;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFailure;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;

public class ControlLogic extends ClientListenerAdapter
{
	private static final int TIMEOUT = 3000;
	
	private ActivityMain activityMain;
	private Client client;
	
	private TimeoutRunnable loginTimeout;
	private boolean creatingServer;
	private boolean loggingIn;
	
	ControlLogic(ActivityMain activityMain, Client client)
	{
		this.activityMain = activityMain;
		this.client = client;
		
		updateListener();
	}
	
	void updateListener()
	{
		client.setClientListener(this);
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
		setLoginTimeout();
		loggingIn = true;
	}
	
	void createServer(String serverName, String username)
	{
		client.sendPacket(new OutputPacketCreateServer(serverName, username));
		setLoginTimeout();
		creatingServer = true;
	}
	
	@Override
	public void onLoggedIn(String serverName, int serverCode)
	{
		interruptTimeout();
		runInUIThread(() -> activityMain.onLoggedIn(serverName, serverCode));
		client.suspendPacketExcecution();
		creatingServer = false;
		loggingIn = false;
	}
	
	@Override
	public void onFailure(int problem)
	{
		interruptTimeout();
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
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(() -> activityMain.onDisconnect());
	}
	
	private void runInUIThread(Runnable runnable)
	{
		new Handler(Looper.getMainLooper()).post(runnable);
	}
	
	private void setLoginTimeout()
	{
		loginTimeout = new TimeoutRunnable(TIMEOUT, () -> onFailure(0));
	}
	
	private void interruptTimeout()
	{
		if(loginTimeout == null) return;
		loginTimeout.interrupt();
		loginTimeout = null;
	}
	
	Client getClient()
	{
		return client;
	}
}
