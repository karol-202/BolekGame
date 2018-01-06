package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public interface InputPacket
{
	void readData(DataBundle bundle);
	
	void execute(ClientListener clientListener);
}
