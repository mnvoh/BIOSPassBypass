package ir.kcoder.biospassbypass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MasterPasswordsActivity extends ActionBarActivity implements SystemSelectFragment.OnSystemSelectedListener {
	private ArrayList<String[]> masterPasswords;
	private ArrayList<String> biosTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_passwords);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setTitle( R.string.master_passwords );
		
		parseMasterPasswordsFile();
		
		SystemSelectFragment ssf = ( SystemSelectFragment )getSupportFragmentManager().findFragmentById( R.id.frgSystemSelect );
		ssf.setSystemsList( ( String[] )this.biosTypes.toArray(new String[ biosTypes.size() ]) );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void parseMasterPasswordsFile() {
		this.masterPasswords = new ArrayList<String[]>();
		this.biosTypes = new ArrayList<String>();
		
		InputStream is = getResources().openRawResource(R.raw.master_passwords);
		InputStreamReader isr = new InputStreamReader( is );
		BufferedReader br = new BufferedReader( isr );
		
		String line;
		boolean nextIsModel = false;
		String currentModel = "";
		try {
			while( ( line = br.readLine() ) != null ) {
				if( line.equals( "--------" ) ) {
					nextIsModel = true;
					continue;
				}
				
				if( nextIsModel ) {
					currentModel = line;
					nextIsModel = false;
					biosTypes.add( line );
					continue;
				}
				
				String[] item = { currentModel, line };
				
				this.masterPasswords.add( item );
			}
			
			is.close();
		} catch( IOException ioex ) {}
	}

	@Override
	public void onSystemSelected( String system ) {
		ArrayList<String> results = new ArrayList<String>();
		for( int i = 0; i < this.masterPasswords.size(); i++ ) {
			String[] temp = this.masterPasswords.get( i );
			if( temp[0].equals( system ) ) {
				results.add( temp[1] );
			}
		}
		
		String[] strResults = ( String[] )results.toArray( new String[ results.size() ] );
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right,
								android.R.anim.slide_in_left, android.R.anim.slide_out_right );
		
		MasterPasswordsFragment mpf = new MasterPasswordsFragment();
		mpf.setMasterPasswords( strResults );
		ft.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_CLOSE );
		ft.replace( R.id.mpFragContainer, mpf, "masterPasswordsFrag" );
		ft.addToBackStack( null );
		ft.commit();
	}
}
