package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketPresidentCheckedPlayer implements InputGamePacket
{
	private String checkedPlayer;
	
	@Override
	public void readData(DataBundle bundle)
	{
		checkedPlayer = bundle.getString("checkedPlayer", "");
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onPresidentCheckedPlayer(checkedPlayer);
	}
}
