package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketDismissActPyPresident implements OutputPacket
{
	private String act;
	
	public OutputPacketDismissActPyPresident(String act)
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
		return "DISMISSACTPYPRESIDENT";
	}
}
