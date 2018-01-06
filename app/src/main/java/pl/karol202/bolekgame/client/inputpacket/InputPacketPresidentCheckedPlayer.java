package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketPresidentCheckedPlayer implements InputPacket
{
	private String checkedPlayer;
	
	@Override
	public void readData(DataBundle bundle)
	{
		checkedPlayer = bundle.getString("checkedPlayer", "");
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onPresidentCheckedPlayer(checkedPlayer);
	}
}
