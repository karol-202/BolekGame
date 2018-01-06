package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.Act;

public class InputPacketActsCheckingResult implements InputPacket
{
	private Act[] acts;
	
	InputPacketActsCheckingResult()
	{
		acts = new Act[3];
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		acts[0] = Act.getActByName(bundle.getString("act1", ""));
		acts[1] = Act.getActByName(bundle.getString("act2", ""));
		acts[2] = Act.getActByName(bundle.getString("act3", ""));
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onActsCheckingResult(acts);
	}
}
