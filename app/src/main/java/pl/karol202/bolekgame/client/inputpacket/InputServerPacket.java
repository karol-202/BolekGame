package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.ui.server.ServerUI;

public interface InputServerPacket extends InputPacket
{
	void execute(ServerUI ui);
}
