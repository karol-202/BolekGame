package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.DataBundle;
import pl.karol202.bolekgame.game.Act;
import pl.karol202.bolekgame.game.GameLogic;

public class InputPacketChooseActsPresident implements InputGamePacket
{
	private Act[] acts;
	
	InputPacketChooseActsPresident()
	{
		acts = new Act[3];
	}
	
	@Override
	public void readData(DataBundle bundle)
	{
		acts[0] = Act.getActByName(bundle.getString("act1", ""));
		acts[1] = Act.getActByName(bundle.getString("act2", ""));
		acts[2] = Act.getActByName(bundle.getString("act3", ""));
	}
	
	@Override
	public void execute(GameLogic ui)
	{
		ui.onChooseActsPresidentRequest(acts);
	}
}
