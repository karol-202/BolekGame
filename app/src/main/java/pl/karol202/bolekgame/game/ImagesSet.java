package pl.karol202.bolekgame.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import pl.karol202.bolekgame.game.gameplay.Role;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class ImagesSet
{
	private Map<Role, Bitmap> rolesImages;
	
	ImagesSet(Context context, byte[] imagesCode)
	{
		rolesImages = new HashMap<>();
		if(imagesCode.length == 0) return;
		tryToDecode(context, imagesCode);
	}
	
	private void tryToDecode(Context context, byte[] code)
	{
		try
		{
			Key key = new SecretKeySpec(code, "AES");
			for(Role role : Role.values())
			{
				
				Bitmap source = loadBitmap(context, role.getImage());
				if(source == null) break;
				rolesImages.put(role, decodeBitmap(source, key));
			}
		}
		catch(GeneralSecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	private Bitmap loadBitmap(Context context, int id)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) return loadBitmapKitkat(context, id);
		else return null;
	}
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private Bitmap loadBitmapKitkat(Context context, int id)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPremultiplied = false;
		return BitmapFactory.decodeResource(context.getResources(), id, options);
	}
	
	private Bitmap decodeBitmap(Bitmap source, Key key) throws GeneralSecurityException
	{
		byte[] loadedBytes = convertBitmapToBytes(source);
		int encryptedLength = getIntFromBytes(loadedBytes, loadedBytes.length - 12);
		int width = getIntFromBytes(loadedBytes, loadedBytes.length - 8);
		int height = getIntFromBytes(loadedBytes, loadedBytes.length - 4);
		
		byte[] encrypted = new byte[encryptedLength];
		System.arraycopy(loadedBytes, 0, encrypted, 0, encryptedLength);
		byte[] decrypted = decryptBytes(encrypted, key);
		return createBitmapFromBytes(decrypted, width, height, source.getConfig());
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
		buffer.rewind();
		
		bitmap.copyPixelsFromBuffer(buffer);
		return bitmap;
	}
	
	private int getIntFromBytes(byte[] bytes, int offset)
	{
		ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, 4);
		return buffer.getInt();
	}
	
	public Bitmap getRoleImage(Role role)
	{
		return rolesImages.get(role);
	}
}
