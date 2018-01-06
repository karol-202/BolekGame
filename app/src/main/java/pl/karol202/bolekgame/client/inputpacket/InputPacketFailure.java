package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketFailure implements InputPacket
{
	public static final int PROBLEM_SERVER_INVALID_NAME = 1;
	public static final int PROBLEM_SERVER_TOO_MANY = 2;
	public static final int PROBLEM_USER_INVALID_NAME = 3;
	public static final int PROBLEM_USER_TOO_MANY = 4;
	public static final int PROBLEM_USER_NAME_BUSY = 5;
	public static final int PROBLEM_SERVER_CODE_INVALID = 6;
	
	private int problem;
	
	@Override
	public void readData(DataBundle bundle)
	{
		problem = bundle.getInt("problem", 0);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onFailure(problem);
	}
}
