package pl.karol202.bolekgame.utils;

import pl.karol202.bolekgame.client.Client;

public class ConnectionData
{
	private static ConnectionData connectionData;
	
	private Client client;
	private String serverName;
	private int serverCode;
	
	public ConnectionData(Client client, String serverName, int serverCode)
	{
		this.client = client;
		this.serverName = serverName;
		this.serverCode = serverCode;
	}
	
	public static ConnectionData getConnectionData()
	{
		ConnectionData connectionData = ConnectionData.connectionData;
		setConnectionData(null);
		return connectionData;
	}
	
	public static void setConnectionData(ConnectionData connectionData)
	{
		ConnectionData.connectionData = connectionData;
	}
	
	public Client getClient()
	{
		return client;
	}
	
	public String getServerName()
	{
		return serverName;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
}
