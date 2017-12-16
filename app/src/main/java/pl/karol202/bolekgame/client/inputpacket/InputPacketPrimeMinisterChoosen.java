package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketPrimeMinisterChoosen implements InputGamePacket
{
	private String primeMinister;
	
	@Override
	public void readData(DataBundle bundle)
	{
		primeMinister = bundle.getString("primeMinister", "");
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPrimeMinisterChoose(primeMinister);
	}
}
