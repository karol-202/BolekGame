package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.WinCause;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketWin implements InputGamePacket
{
	private WinCause cause;
	
	@Override
	public void readData(DataBundle bundle)
	{
		cause = WinCause.values()[bundle.getInt("cause", 0)];
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onWin(cause);
	}
}
