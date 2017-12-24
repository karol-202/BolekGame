package pl.karol202.bolekgame;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Utils
{
	public static int readInt(InputStream inputStream) throws IOException
	{
		byte[] bytes = new byte[4];
		int length = inputStream.read(bytes);
		if(length != 4) return -1;
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	public static byte[] writeInt(int value)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
	}
	
	public static Integer parseInt(String string)
	{
		try
		{
			return Integer.parseInt(string);
		}
		catch(NumberFormatException e)
		{
			return null;
		}
	}
}