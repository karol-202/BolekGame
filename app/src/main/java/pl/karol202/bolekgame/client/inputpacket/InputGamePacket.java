package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.ui.game.GameUI;

public interface InputGamePacket extends InputPacket
{
	void execute(GameUI ui);
}
