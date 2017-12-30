package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.game.GameLogic;

public interface InputGamePacket extends InputPacket
{
	void execute(GameLogic ui);
}
