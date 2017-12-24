package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketLustratePresident implements OutputPacket
{
	private String player;
	
	public OutputPacketLustratePresident(String player)
	{
		this.player = player;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.getString("player", player);
	}
	
	@Override
	public String getName()
	{
		return "LUSTRATEPRESIDENT";
	}
}
