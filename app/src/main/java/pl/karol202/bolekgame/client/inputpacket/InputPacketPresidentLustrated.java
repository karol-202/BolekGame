package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPresidentLustrated implements InputPacket
{
	private String player;
	private boolean bolek;
	
	@Override
	public void readData(DataBundle bundle)
	{
		player = bundle.getString("player", "");
		bolek = bundle.getBoolean("bolek", false);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPresidentLustrated(player, bolek);
	}
}
