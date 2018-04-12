package pl.karol202.bolekgame.control;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class ActivityHelp extends AppCompatActivity
{
	private Toolbar toolbar;
	private TextView textHelp;
	
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
		
		textHelp = findViewById(R.id.text_help);
		textHelp.setText(getHelpContent());
	}
	
	private Spanned getHelpContent()
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return getHelpContentNougat();
		else return getHelpContentPreNougat();
	}
	
	private Spanned getHelpContentPreNougat()
	{
		return Html.fromHtml(getString(R.string.help_content));
	}
	
	@TargetApi(Build.VERSION_CODES.N)
	private Spanned getHelpContentNougat()
	{
		return Html.fromHtml(getString(R.string.help_content), Html.FROM_HTML_MODE_COMPACT);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
}
