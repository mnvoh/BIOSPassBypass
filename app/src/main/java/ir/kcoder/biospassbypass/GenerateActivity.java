package ir.kcoder.biospassbypass;

import ir.kcoder.biospassbypass.generators.Dell;
import ir.kcoder.biospassbypass.generators.FSI5By4Decimal;
import ir.kcoder.biospassbypass.generators.FSIHex;
import ir.kcoder.biospassbypass.generators.FiveDecimals;
import ir.kcoder.biospassbypass.generators.HPMini;
import ir.kcoder.biospassbypass.generators.Insyde;
import ir.kcoder.biospassbypass.generators.Samsung;
import ir.kcoder.biospassbypass.generators.Sony;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GenerateActivity extends ActionBarActivity implements Runnable {
	public static final String BIOS_HASH_EXTRA = "bios_hash";
	private final short STATUS_GENERATE_RANDOM_STRING = 0;
	private final short STATUS_DONE = 2;
	
	private String hash;
	private String stripedHash;
	private Handler handler;
	private short status = STATUS_GENERATE_RANDOM_STRING;
	private ArrayList<String[]> results;
	
	private TextView txvStatus;
	private ListView lstPasswords;
	
	private Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setTitle( R.string.password_from_hash );
		
		this.hash = getIntent().getExtras().getString( GenerateActivity.BIOS_HASH_EXTRA ).trim();
		this.stripedHash = hash.replace("-", "");
		
		this.txvStatus = ( TextView )findViewById( R.id.txvStatus );
		this.lstPasswords = ( ListView )findViewById( R.id.lstPasswords );
		
		this.handler = new Handler();
		handler.postDelayed( this, 100 );
		
		//startGenerating();
		new Thread( new StartGenerating() ).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	class StartGenerating implements Runnable {
		public void run() {
			results = new ArrayList<String[]>();
			
			try {
				Thread.sleep( 1500L );
			} catch( InterruptedException iex ) {
				Toast.makeText( context, "Not Sleeping!", Toast.LENGTH_SHORT ).show();
			}
			
			try {
				switch( hash.length() ) {
					//FiveDecimals
					case 5:
						FiveDecimals fiveDecimals = new FiveDecimals();
						if( Integer.parseInt( stripedHash ) <= 0x3FFF )
							results.addAll( fiveDecimals.generateBiosPassword( stripedHash ) );
						break;
					//Dell by serial number
					//Old Sony
					case 7:
						Dell dell = new Dell();
						Sony sony = new Sony();
						results.addAll( dell.generateBiosPassword7( stripedHash ) );
						results.addAll( sony.generateBiosPassword( stripedHash ) );
						break;
					//InsydeH2O BIOS (Acer, HP)
					//Fujitsu-Siemens
					case 8:
						Insyde insyde = new Insyde();
						FSIHex fsi = new FSIHex();
						results.addAll( insyde.generateBiosPassword( stripedHash ) );
						results.addAll( fsi.generateBiosPassword( stripedHash ) );
						break;
					//HP/Compaq Mini Netbooks
					case 10:
						HPMini hp = new HPMini();
						results.addAll( hp.generateBiosPassword( stripedHash ) );
						break;
					//Dell by hdd serial number new
					//Dell by hdd serial number old
					case 11:
						Dell dell2 = new Dell();
						results.addAll( dell2.generateBiosPassword11( stripedHash ) );
						break;
					//Dell by serial number
					case 12:
						Dell dell3 = new Dell();
						results.addAll( dell3.generateBiosPassword74( stripedHash ) );
						break;
					//Dell by hdd serial number new
					case 16:
						Dell dell4 = new Dell();
						results.addAll( dell4.generateBiosPassword114( stripedHash ) );
						break;
					//Fujitsu-Siemens new
					//Fujitsu-Siemens old
					//Fujitsu-Siemens
					case 24:
						FSIHex fsiHex = new FSIHex();
						FSI5By4Decimal fsiDec = new FSI5By4Decimal();
						results.addAll( fsiHex.generateBiosPassword( stripedHash ) );
						results.addAll( fsiDec.generateBiosPasswordNew( stripedHash ) );
						results.addAll( fsiDec.generateBiosPasswordOld( stripedHash ) );
						break;
					//Samsung(due to variable hash)
					default:
						Samsung samsung = new Samsung();
						results.addAll( samsung.generateBiosPassword( stripedHash ) );
						break;
				}
			} catch( Exception ex ) { 
				
			} finally {
				status = STATUS_DONE;
			}
		}
	}
	
	public void run() {
		if( this.status == this.STATUS_GENERATE_RANDOM_STRING ) {
			this.txvStatus.setText( getString( R.string.please_wait ) + "\n" + Utilities.randomString( 8 ) );
			this.handler.postDelayed( this, 100 );
		} else if( this.status == this.STATUS_DONE ) {
			BPBListViewItem[] items;
			if( results.size() > 0 ) {
				items = new BPBListViewItem[ results.size() ];
				for( int i = 0; i < results.size(); i++ ) {
					items[ i ] = new BPBListViewItem( results.get( i )[ 0 ], results.get( i )[ 1 ] );
				}
			} else {
				items = new BPBListViewItem[1];
				items[ 0 ] = new BPBListViewItem( getString( R.string.no_passwords_found ), ":-(" );
				
			}
			BPBListViewAdapter adapter = new BPBListViewAdapter( this, R.layout.bpb_listview_row, R.id.txvTitle, items );
			this.lstPasswords.setAdapter( adapter );
			this.txvStatus.setVisibility( android.view.View.GONE );
		}
	}
	
}
