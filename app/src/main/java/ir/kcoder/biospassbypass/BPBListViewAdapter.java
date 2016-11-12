package ir.kcoder.biospassbypass;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class BPBListViewAdapter extends ArrayAdapter<BPBListViewItem> implements Runnable {
	
	private final int TICK_DELAY = 50;
	
	private BPBListViewItem[] items;
	private Handler handler;
	
	public BPBListViewAdapter( Context context, int layoutId, int textViewResourceId, BPBListViewItem[] objects ) {
		super( context, layoutId, textViewResourceId, objects );
		this.items = objects;
		handler = new Handler();
		handler.postDelayed( this, TICK_DELAY );
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		View row = super.getView(position, convertView, parent);
		ViewHolder viewHolder = ( ViewHolder )row.getTag();
		if( null == viewHolder ) {
			viewHolder = new ViewHolder( row );
			row.setTag( viewHolder );
		}
		
		viewHolder.txvText.setText( items[ position ].getText() );
		viewHolder.txvTitle.setText( items[ position ].getTitle() );
		
		return row;
	}
	
//	@Override
//	public void add(BPBListViewItem object) {
//		super.add( object );
//		BPBListViewItem[] newItems = new BPBListViewItem[ items.length + 1 ];
//		System.arraycopy( items, 0, newItems, 0, items.length );
//		newItems[ items.length ] = object;
//		this.items = newItems;
//		run();
//	}
	
	@Override
	public void run() {
		boolean anyChanges = false;
		
		for( int i = 0; i < items.length; i++ ) {
			if( items[ i ].tick() ) {
				anyChanges = true;
			}
		}
		
		if( anyChanges ) {
			handler.postDelayed( this, TICK_DELAY );
			this.notifyDataSetChanged();
		}
	}

}
