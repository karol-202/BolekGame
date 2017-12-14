package pl.karol202.bolekgame.client.outputpacket;

import pl.karol202.bolekgame.client.DataBundle;

public interface OutputPacket
{
	void saveData(DataBundle bundle);
	
	String getName();
}
