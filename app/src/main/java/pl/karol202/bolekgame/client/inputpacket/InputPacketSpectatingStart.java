package pl.karol202.bolekgame.client.inputpacket;

import pl.karol202.bolekgame.client.ClientListener;
import pl.karol202.bolekgame.client.DataBundle;

public class InputPacketSpectatingStart implements InputPacket
{
	private byte[] imagesCode;
	
	@Override
	public void readData(DataBundle bundle)
	{
		int codeLength = bundle.getInt("imagesCodeLength", 0);
		imagesCode = new byte[codeLength];
		for(int i = 0; i < codeLength; i++)
			imagesCode[i] = (byte) bundle.getInt("imagesCode" + i, 0);
	}
	
	@Override
	public void execute(ClientListener clientListener)
	{
		clientListener.onSpectatingStart(imagesCode);
	}
}
