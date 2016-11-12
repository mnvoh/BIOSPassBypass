package ir.kcoder.biospassbypass;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MasterPasswordsFragment extends ListFragment {
	private Context context;
	private String[] masterPasswords;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.getContext();
		setMasterPasswords();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	 
	public void setMasterPasswords() {
		if( context != null && this.masterPasswords != null ) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, masterPasswords );
			setListAdapter(adapter);
		}
	}
	
	public void setMasterPasswords( String[] masterPasswords ) {
		this.masterPasswords = masterPasswords;
		if( context != null && this.masterPasswords != null ) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, masterPasswords );
			setListAdapter(adapter);
		}
	}
}
