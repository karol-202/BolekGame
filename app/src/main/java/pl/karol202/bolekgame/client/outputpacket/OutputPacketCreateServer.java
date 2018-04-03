package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketCreateServer implements OutputPacket
{
	private String serverName;
	private String username;
	
	public OutputPacketCreateServer(String serverName, String username)
	{
		this.serverName = serverName;
		this.username = username;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("name", serverName);
		bundle.putString("username", username);
		bundle.putInt("apiVersion", Client.API_VERSION);
	}
	
	@Override
	public String getName()
	{
		return "CREATESERVER";
	}
}
