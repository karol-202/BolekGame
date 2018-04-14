package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

import java.util.ArrayList;
import java.util.List;

public class InputPacketGameStart implements InputPacket
{
	private List<String> players;
	private byte[] imagesCode;
	
	InputPacketGameStart()
	{
		players = new ArrayList<>();
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		int playersLength = bundle.getInt("players", 0);
		for(int i = 0; i < playersLength; i++) players.add(bundle.getString("player" + i, ""));
		int codeLength = bundle.getInt("imagesCodeLength", 0);
		imagesCode = new byte[codeLength];
		for(int i = 0; i < codeLength; i++)
			imagesCode[i] = (byte) bundle.getInt("imagesCode" + i, 0);
	}
	
	@Override
	public void execute(ClientListener listener)
	{
		listener.onGameStart(players, imagesCode);
	}
}
