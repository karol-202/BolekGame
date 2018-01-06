package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.WinCause;

public class InputPacketWin implements InputPacket
{
	private WinCause cause;
	
	@Override
	public void readData(DataBundle bundle)
	{
		cause = WinCause.values()[bundle.getInt("cause", 0)];
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onWin(cause);
	}
}
