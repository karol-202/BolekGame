package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketPrimeMinisterAssigned implements InputGamePacket
{
	private String primeMinister;
	
	@Override
	public void readData(DataBundle bundle)
	{
		primeMinister = bundle.getString("primeMinister", "");
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onPrimeMinisterAssigment(primeMinister);
	}
}
