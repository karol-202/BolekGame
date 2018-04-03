package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketLogin implements OutputPacket
{
	private int serverCode;
	private String username;
	
	public OutputPacketLogin(int serverCode, String username)
	{
		this.serverCode = serverCode;
		this.username = username;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("serverCode", serverCode);
		bundle.putString("username", username);
		bundle.putInt("apiVersion", Client.API_VERSION);
	}
	
	@Override
	public String getName()
	{
		return "LOGIN";
	}
}
