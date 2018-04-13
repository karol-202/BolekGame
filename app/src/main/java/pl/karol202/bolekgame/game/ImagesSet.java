package pl.karol202.bolekgame.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import pl.karol202.bolekgame.game.gameplay.Role;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

class ImagesSet
{
	private Map<Role, Bitmap> rolesImages;
	
	ImagesSet(Context context, int imagesCode)
	{
		rolesImages = new HashMap<>();
		
		Key key = new SecretKeySpec(intToBytes(imagesCode), "AES");
		
		for(Role role : Role.values())
		{
			Bitmap source = BitmapFactory.decodeResource(context.getResources(), role.getImage());
			rolesImages.put(role, decodeBitmap(source, key));
		}
	}
	
	private byte[] intToBytes(int value)
	{
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (value & 0xFF);
		bytes[1] = (byte) ((value >> 8) & 0xFF);
		bytes[2] = (byte) ((value >> 16) & 0xFF);
		bytes[3] = (byte) ((value >> 24) & 0xFF);
		return bytes;
	}
	
	private Bitmap decodeBitmap(Bitmap source, Key key)
	{
		try
		{
			byte[] bytes = convertBitmapToBytes(source);
			bytes = decryptBytes(bytes, key);
			return createBitmapFromBytes(bytes, source.getWidth(), source.getHeight(), source.getConfig());
		}
		catch(GeneralSecurityException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	private byte[] convertBitmapToBytes(Bitmap bitmap)
	{
		ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
		bitmap.copyPixelsToBuffer(buffer);
		return buffer.array();
	}
	
	@SuppressLint("GetInstance")
	private byte[] decryptBytes(byte[] encrypted, Key key) throws GeneralSecurityException
	{
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encrypted);
	}
	
	private Bitmap createBitmapFromBytes(byte[] bytes, int width, int height, Bitmap.Config config)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.flip();
		
		bitmap.copyPixelsFromBuffer(buffer);
		return bitmap;
	}
}
