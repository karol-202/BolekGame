package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.ui.game.GameUI;

public class InputPacketPresidentAssigned implements InputGamePacket
{
	private String president;
	
	@Override
	public void readData(DataBundle bundle)
	{
		president = bundle.getString("president", "");
	}
	
	@Override
	public void execute(GameUI ui)
	{
		ui.onPresidentAssignment(president);
	}
}
