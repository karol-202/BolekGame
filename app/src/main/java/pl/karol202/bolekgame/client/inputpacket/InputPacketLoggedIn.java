package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketLoggedIn implements InputPacket
{
	private String serverName;
	private int serverCode;
	
	@Override
	public void readData(DataBundle bundle)
	{
		serverName = bundle.getString("serverName", "-");
		serverCode = bundle.getInt("serverCode", -1);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onLoggedIn(serverName, serverCode);
	}
}
