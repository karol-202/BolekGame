package pl.karol202.bolekgame.server;

import pl.karol202.bolekgame.client.Client;

public class ServerData
{
	private static ServerData serverData;
	
	private Client client;
	private String serverName;
	private int serverCode;
	
	public ServerData(Client client, String serverName, int serverCode)
	{
		this.client = client;
		this.serverName = serverName;
		this.serverCode = serverCode;
	}
	
	static ServerData getServerData()
	{
		ServerData serverData = ServerData.serverData;
		setServerData(null);
		return serverData;
	}
	
	public static void setServerData(ServerData serverData)
	{
		ServerData.serverData = serverData;
	}
	
	public Client getClient()
	{
		return client;
	}
	
	protected String getServerName()
	{
		return serverName;
	}
	
	protected int getServerCode()
	{
		return serverCode;
	}
}
