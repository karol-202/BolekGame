package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketDismissActByPresident implements OutputPacket
{
	private String act;
	
	public OutputPacketDismissActByPresident(String act)
	{
		this.act = act;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putString("act", act);
	}
	
	@Override
	public String getName()
	{
		return "DISMISSACTBYPRESIDENT";
	}
}
