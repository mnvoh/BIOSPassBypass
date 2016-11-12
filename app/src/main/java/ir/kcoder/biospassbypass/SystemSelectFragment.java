package ir.kcoder.biospassbypass;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SystemSelectFragment extends Fragment {
	OnSystemSelectedListener mCallback;
	private ListView lstSystems;
	
	public interface OnSystemSelectedListener {
		public void onSystemSelected( String system );
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try {
			mCallback = ( OnSystemSelectedListener )activity;
		} catch( ClassCastException ccex ) {
			throw new ClassCastException(activity.toString()
                    + " must implement OnSystemSelectedListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate( R.layout.system_select_frag, container, false );
	}
	
	public void setSystemsList( String[] systems ) {
		if( lstSystems == null ) {
			lstSystems = ( ListView )getView().findViewById( R.id.lstBios );
		}
		if( lstSystems != null ) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, systems );
			lstSystems.setAdapter( adapter );
			lstSystems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
					if( mCallback != null ) {
						String biosType = parent.getItemAtPosition( position ).toString();
						mCallback.onSystemSelected( biosType );
					}
				}
				
			});
		}
	}
}
