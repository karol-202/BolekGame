package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.control.ControlUI;

public class InputPacketLoggedIn implements InputControlPacket
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
	public void execute(ControlUI ui)
	{
		ui.onLoggedIn(serverName, serverCode);
	}
}
