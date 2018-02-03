package pl.karol202.bolekgame;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

public class BolekApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		if(LeakCanary.isInAnalyzerProcess(this)) return;
		LeakCanary.install(this);
	}
}
