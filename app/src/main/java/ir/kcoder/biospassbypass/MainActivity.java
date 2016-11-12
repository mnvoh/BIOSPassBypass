package ir.kcoder.biospassbypass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	private EditText hashText;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportActionBar().setTitle( R.string.app_name );
		
		this.hashText = ( EditText )findViewById( R.id.etxHash );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.main_menu, menu );
		return super.onCreateOptionsMenu( menu );
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.menuHelp:
				showHelp();
				return true;
			case R.id.menuAbout:
				showAbout();
				return true;
			case R.id.menuShare:
				shareApp();
				return true;
			default:
				return super.onOptionsItemSelected( item );
		}
	}

	public void generatePasswords( View btnGenerate ) {
		String hash = this.hashText.getText().toString();
		if( hash.length() == 0 ) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder( this );
			alertBuilder.setMessage( R.string.enter_bios_hash )
						.setPositiveButton( R.string.back, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						} );
			alertBuilder.show();
		} else if( hash.length() < 5 ) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder( this );
			alertBuilder.setMessage( R.string.invalid_bios_hash )
						.setPositiveButton( R.string.back, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
			alertBuilder.show();
		} else {
			Intent generateIntent = new Intent( this, GenerateActivity.class );
			generateIntent.putExtra( GenerateActivity.BIOS_HASH_EXTRA, hash );
			startActivity( generateIntent );
		}
	}
	
	public void showMasterPasswords( View view ) {
		startActivity( new Intent( this, MasterPasswordsActivity.class ) );
	}
	
	public void showHelp() {
		startActivity( new Intent( this, HelpActivity.class ) );
	}
	
	public void showAbout() {
		startActivity( new Intent( this, AboutActivity.class ) );
	}
	
	public void shareApp() {
		Intent shareIntent = new Intent( Intent.ACTION_SEND );
		shareIntent.setType( "text/plain" );
		shareIntent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.share_title ) );
		shareIntent.putExtra( Intent.EXTRA_TEXT, getString( R.string.share_text ) );
		startActivity( Intent.createChooser( shareIntent, getString( R.string.share_chooser ) ) );
	}
}
