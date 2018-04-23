package pl.karol202.bolekgame.game;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.server.ServerData;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.server.TextChat;

public class GameData extends ServerData
{
	private static GameData gameData;
	
	private boolean spectating;
	private Users users;
	private TextChat textChat;
	private byte[] imagesCode;
	
	public GameData(Client client, String serverName, int serverCode)
	{
		super(client, serverName, serverCode);
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
	
	public boolean isSpectating()
	{
		return spectating;
	}
	
	public void setSpectating(boolean spectating)
	{
		this.spectating = spectating;
	}
	
	public Users getUsers()
	{
		return users;
	}
	
	public void setUsers(Users users)
	{
		this.users = users;
	}
	
	public TextChat getTextChat()
	{
		return textChat;
	}
	
	public void setTextChat(TextChat textChat)
	{
		this.textChat = textChat;
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
