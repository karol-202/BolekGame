package pl.karol202.bolekgame.utils;

import pl.karol202.bolekgame.client.Client;

public class GameData extends ServerData
{
	private static GameData gameData;
	
	private TextChat textChat;
	
	public GameData(Client client, TextChat textChat, String serverName, int serverCode)
	{
		super(client, serverName, serverCode);
		this.textChat = textChat;
	}
	
	public static GameData getGameData()
	{
		GameData gameData = GameData.gameData;
		setGameData(null);
		return gameData;
	}
	
	public static void setGameData(GameData gameData)
	{
		GameData.gameData = gameData;
	}
	
	public TextChat getTextChat()
	{
		return textChat;
	}
}
