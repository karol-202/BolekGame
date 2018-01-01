package pl.karol202.bolekgame.settings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import pl.karol202.bolekgame.R;

public class ActivitySettings extends AppCompatActivity
{
	private Toolbar toolbar;
	
	private ActionBar actionBar;
	private String preferenceToEdit;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		loadData();
		attachPreferenceFragment();
		
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		actionBar = getSupportActionBar();
		if(actionBar == null) return;
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void loadData()
	{
		preferenceToEdit = getIntent().getStringExtra("preferenceToEdit");
	}
	
	private void attachPreferenceFragment()
	{
		Fragment fragment = new FragmentSettings();
		Bundle data = new Bundle();
		if(preferenceToEdit != null) data.putString("preferenceToEdit", preferenceToEdit);
		fragment.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.settings_view, fragment);
		transaction.commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
}
