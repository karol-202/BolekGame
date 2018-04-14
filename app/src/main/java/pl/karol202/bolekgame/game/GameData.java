package pl.karol202.bolekgame.game;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.server.ServerData;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.utils.TextChat;

public class GameData extends ServerData
{
	private static GameData gameData;
	
	private Users users;
	private TextChat textChat;
	private byte[] imagesCode;
	
	public GameData(Client client, Users users, TextChat textChat, String serverName, int serverCode)
	{
		super(client, serverName, serverCode);
		this.users = users;
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
	
	@Override
	protected int getServerCode()
	{
		return super.getServerCode();
	}
	
	Users getUsers()
	{
		return users;
	}
	
	TextChat getTextChat()
	{
		return textChat;
	}
	
	public byte[] getImagesCode()
	{
		return imagesCode;
	}
	
	public void setImagesCode(byte[] imagesCode)
	{
		this.imagesCode = imagesCode;
	}
}
