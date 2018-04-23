package pl.karol202.bolekgame;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class PermissionGrantingActivity extends AppCompatActivity implements PermissionRequest.PermissionGrantingActivity
{
	private Map<Integer, PermissionRequest.PermissionGrantListener> listeners;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listeners = new HashMap<>();
	}
	
	@Override
	public void registerPermissionGrantListener(int requestCode, PermissionRequest.PermissionGrantListener listener)
	{
		if(listeners.containsKey(requestCode))
			throw new RuntimeException("requestCode is already used: " + requestCode);
		listeners.put(requestCode, listener);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(!listeners.containsKey(requestCode)) return;
		if(grantResults.length != 0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED)
		{
			PermissionRequest.PermissionGrantListener listener = listeners.get(requestCode);
			if(listener != null) listener.onPermissionGrant();
		}
		listeners.remove(requestCode);
	}
}
