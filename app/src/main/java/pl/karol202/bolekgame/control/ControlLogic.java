package pl.karol202.bolekgame.control;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFailure;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketCreateServer;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketLogin;
import pl.karol202.bolekgame.Logic;

class ControlLogic extends Logic<ActivityMain>
{
	private static final int TIMEOUT = 3500;
	
	private TimeoutRunnable loginTimeout;
	private boolean creatingServer;
	private boolean loggingIn;
	
	ControlLogic()
	{
		super(new Client());
	}
	
	void connect(String host)
	{
		new Thread(() -> connectAndWait(host)).start();
	}
	
	private void connectAndWait(String host)
	{
		boolean result = getClient().connect(host);
		if(result)
		{
			getClient().run();
			executeOnActivityInUIThread(ActivityMain::onConnect);
		}
		else executeOnActivityInUIThread(ActivityMain::onConnectFail);
	}
	
	boolean isConnected()
	{
		return getClient().isConnected();
	}
	
	boolean isConnectedTo(String host)
	{
		return getClient().isConnectedTo(host);
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
		executeOnActivityInUIThread(a -> a.onLoggedIn(serverName, serverCode));
		suspendClient();
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
			executeOnActivityInUIThread(ActivityMain::onInvalidServerNameError);
		else if(problem == InputPacketFailure.PROBLEM_SERVER_TOO_MANY)
			executeOnActivityInUIThread(ActivityMain::onTooManyServersError);
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			executeOnActivityInUIThread(ActivityMain::onInvalidUsernameError);
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			executeOnActivityInUIThread(ActivityMain::onTooManyUsersError);
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			executeOnActivityInUIThread(ActivityMain::onUsernameBusyError);
		else executeOnActivityInUIThread(ActivityMain::onCannotCreateServer);
	}
	
	private void onLoggingFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_SERVER_CODE_INVALID)
			executeOnActivityInUIThread(ActivityMain::onInvalidServerCode);
		else if(problem == InputPacketFailure.PROBLEM_USER_INVALID_NAME)
			executeOnActivityInUIThread(ActivityMain::onInvalidUsernameError);
		else if(problem == InputPacketFailure.PROBLEM_USER_TOO_MANY)
			executeOnActivityInUIThread(ActivityMain::onTooManyUsersError);
		else if(problem == InputPacketFailure.PROBLEM_USER_NAME_BUSY)
			executeOnActivityInUIThread(ActivityMain::onUsernameBusyError);
		else executeOnActivityInUIThread(ActivityMain::onCannotLogIn);
	}
	
	@Override
	public void onDisconnect()
	{
		executeOnActivityInUIThread(ActivityMain::onDisconnect);
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
	
	protected Client getClient()
	{
		return super.getClient();
	}
}
