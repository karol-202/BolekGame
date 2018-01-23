package pl.karol202.bolekgame.game;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.server.ServerData;
import pl.karol202.bolekgame.utils.TextChat;

public class GameData extends ServerData
{
	private static GameData gameData;
	
	private TextChat textChat;
	
	public GameData(Client client, TextChat textChat, String serverName, int serverCode)
	{
		super(client, serverName, serverCode);
		this.textChat = textChat;
	}
	
	static GameData getGameData()
	{
		GameData gameData = GameData.gameData;
		setGameData(null);
		return gameData;
	}
	
	public static void setGameData(GameData gameData)
	{
		GameData.gameData = gameData;
	}
	
	TextChat getTextChat()
	{
		return textChat;
	}
}
