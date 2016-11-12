package ir.kcoder.biospassbypass;

public class BPBListViewItem {
	private String title;
	private String text;
	private String visibleText;
	
	//how many times randomize the invisible portion of the text
	//before adding another character to the visible part
	private int ticksPerCharacter = 10;
	private int tickCounter = 0;
	
	public BPBListViewItem( String title, String text ) {
		this.title = title;
		this.text = text;
		this.visibleText = "";
	}
	
	public boolean tick() {
		if( visibleText.length() < text.length() ) {
			if( ++tickCounter > ticksPerCharacter ) {
				tickCounter = 0;
				visibleText = text.substring( text.length() - visibleText.length() - 1, text.length() );
			}
			return true;
		}
		return false;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getText() {
		if( this.text.length() == this.visibleText.length() )
			return this.visibleText;
		return(
			fillStringWithChar('-', this.text.length() - this.visibleText.length() - 1) + 
			Utilities.randomString( 1 ) + this.visibleText
		);
	}
	
	private String fillStringWithChar(char chr, int count) {
		String retval = "";
		for( int i = 0; i < count; i++ ) {
			retval += Character.toString( chr );
		}
		return retval;
	}
}
