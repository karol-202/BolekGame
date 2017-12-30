package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketPresidentLustrated implements InputGamePacket
{
	private String player;
	private boolean bolek;
	
	@Override
	public void readData(DataBundle bundle)
	{
		player = bundle.getString("player", "");
		bolek = bundle.getBoolean("bolek", false);
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onPresidentLustrated(player, bolek);
	}
}
