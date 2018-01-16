package pl.karol202.bolekgame.control;

import android.os.Handler;
import android.os.Looper;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFailure;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;
import pl.karol202.bolekgame.utils.Logic;

public class ControlLogic extends Logic<ActivityMain>
{
	private static final int TIMEOUT = 3500;
	
	private TimeoutRunnable loginTimeout;
	private boolean creatingServer;
	private boolean loggingIn;
	
	ControlLogic()
	{
		client = new Client();
		
		resumeClient();
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
			runInUIThread(activity::onConnect);
		}
		else runInUIThread(activity::onConnectFail);
	}
	
	boolean isConnected()
	{
		return client.isConnected();
	}
	
	void login(int serverCode, String username)
	{
		sendPacket(new OutputPacketLogin(serverCode, username));
		setLoginTimeout();
		loggingIn = true;
	}
	
	void createServer(String serverName, String username)
	{
		sendPacket(new OutputPacketCreateServer(serverName, username));
		setLoginTimeout();
		creatingServer = true;
	}
	
	@Override
	public void onLoggedIn(String serverName, int serverCode)
	{
		interruptTimeout();
		runInUIThread(() -> activity.onLoggedIn(serverName, serverCode));
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
			runInUIThread(() -> activity.onInvalidServerNameError());
		else if(problem == InputPacketFailure.PROBLEM_SERVER_TOO_MANY)
			runInUIThread(() -> activity.onTooManyServersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			runInUIThread(() -> activity.onInvalidUsernameError());
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			runInUIThread(() -> activity.onTooManyUsersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			runInUIThread(() -> activity.onUsernameBusyError());
		else runInUIThread(() -> activity.onCannotCreateServer());
	}
	
	private void onLoggingFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_SERVER_CODE_INVALID)
			runInUIThread(() -> activity.onInvalidServerCode());
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			runInUIThread(() -> activity.onInvalidUsernameError());
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			runInUIThread(() -> activity.onTooManyUsersError());
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			runInUIThread(() -> activity.onUsernameBusyError());
		else runInUIThread(() -> activity.onCannotLogIn());
	}
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(() -> activity.onDisconnect());
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
