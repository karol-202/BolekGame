package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.server.ServerLogic;

public interface InputServerPacket extends InputPacket
{
	void execute(ServerLogic ui);
}
