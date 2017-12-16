package pl.karol202.bolekgame.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

class Utils
{
	static int readInt(InputStream inputStream) throws IOException
	{
		byte[] bytes = new byte[4];
		int length = inputStream.read(bytes);
		if(length != 4) return -1;
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	static byte[] writeInt(int value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
	}
}