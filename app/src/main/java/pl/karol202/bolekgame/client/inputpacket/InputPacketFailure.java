package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.control.ControlLogic;
import pl.karol202.bolekgame.game.GameLogic;
import pl.karol202.bolekgame.server.ServerLogic;

public class InputPacketFailure implements InputControlPacket, InputServerPacket, InputGamePacket
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
	public void execute(GameLogic ui)
	{
		ui.onFailure(problem);
	}
	
	@Override
	public void execute(ServerLogic ui)
	{
		ui.onFailure(problem);
	}
	
	@Override
	public void execute(ControlLogic ui)
	{
		ui.onFailure(problem);
	}
}
