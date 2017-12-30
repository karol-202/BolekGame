package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.control.ControlLogic;

public interface InputControlPacket extends InputPacket
{
	void execute(ControlLogic ui);
}
