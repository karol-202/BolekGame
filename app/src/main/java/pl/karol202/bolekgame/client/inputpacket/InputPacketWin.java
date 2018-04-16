package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.gameplay.WinCause;

public class InputPacketWin implements InputPacket
{
	private boolean ministers;
	private WinCause cause;
	
	@Override
	public void readData(DataBundle bundle)
	{
		ministers = bundle.getBoolean("ministers", true);
		cause = WinCause.values()[bundle.getInt("cause", 0)];
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onWin(ministers, cause);
	}
}
