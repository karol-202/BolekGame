package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public class OutputPacketChoosePlayerOrActsCheckingPresident implements OutputPacket
{
	private int choice;
	
	public OutputPacketChoosePlayerOrActsCheckingPresident(int choice)
	{
		this.choice = choice;
	}
	
	@Override
	public void saveData(DataBundle bundle)
	{
		bundle.putInt("choice", choice);
	}
	
	@Override
	public String getName()
	{
		return "CHOOSEPLAYERORACTSCHECKINGPRESIDENT";
	}
}
