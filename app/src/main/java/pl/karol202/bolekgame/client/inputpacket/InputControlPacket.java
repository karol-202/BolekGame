package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.ui.control.ControlUI;

public interface InputControlPacket extends InputPacket
{
	void execute(ControlUI ui);
}
