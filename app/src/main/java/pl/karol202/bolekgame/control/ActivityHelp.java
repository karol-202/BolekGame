package pl.karol202.bolekgame.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import pl.karol202.bolekgame.R;

public class ActivityHelp extends AppCompatActivity
{
	private Toolbar toolbar;
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(@Nullable Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.activity_help);
		
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		actionBar = getSupportActionBar();
		if(actionBar == null) return;
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
}
