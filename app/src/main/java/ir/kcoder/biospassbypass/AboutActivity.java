package ir.kcoder.biospassbypass;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends ActionBarActivity {
	private TextView txvVersion;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setTitle( R.string.about );
		
		this.txvVersion = ( TextView )findViewById( R.id.txvVersion );
		String version = "";
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo( getPackageName(), 0 );
			version = packageInfo.versionName;
		} catch( NameNotFoundException nnfex ) {
			version = "?.??";
		}
		this.txvVersion.setText( getString( R.string.version ) + " " + version );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask( this );
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	public void loadKcoderWebsite( View v ) {
		String url = "http://kcoder.ir";
		Intent i = new Intent( Intent.ACTION_VIEW );
		Uri u = Uri.parse( url );
		i.setData( u );
		
		try {
			startActivity( i );
		} catch( ActivityNotFoundException ex ) {
			Toast.makeText( this, R.string.browser_not_found, Toast.LENGTH_LONG ).show();
		}
	}
}
