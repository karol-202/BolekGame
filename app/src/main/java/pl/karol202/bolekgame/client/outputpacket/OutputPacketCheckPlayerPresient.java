package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketCheckPlayerPresient implements OutputPacket
{
	private String checkedPlayer;
	
	public OutputPacketCheckPlayerPresient(String checkedPlayer)
	{
		this.checkedPlayer = checkedPlayer;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("checkedPlayer", checkedPlayer);
	}
	
	@Override
	public String getName()
	{
		return "CHECKPLAYERPRESIDENT";
	}
}
