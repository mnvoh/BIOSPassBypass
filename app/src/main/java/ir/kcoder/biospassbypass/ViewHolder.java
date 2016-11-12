package ir.kcoder.biospassbypass;

import android.view.View;
import android.widget.TextView;

public class ViewHolder {
	public TextView txvTitle;
	public TextView txvText;
	
	public ViewHolder( View row ) {
		this.txvText = ( TextView )row.findViewById( R.id.txvText );
		this.txvTitle = ( TextView )row.findViewById( R.id.txvTitle );
	}
}
