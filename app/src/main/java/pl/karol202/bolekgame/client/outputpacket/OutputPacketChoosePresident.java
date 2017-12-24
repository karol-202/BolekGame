package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketChoosePresident implements OutputPacket
{
	private String president;
	
	public OutputPacketChoosePresident(String president)
	{
		this.president = president;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("president", president);
	}
	
	@Override
	public String getName()
	{
		return "CHOOSEPRESIDENT";
	}
}
