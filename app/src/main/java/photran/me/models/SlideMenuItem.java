package photran.me.models;

public class SlideMenuItem {
	private String mTitle;
	private int mIcon;
	private int mCount;
	private String mHeader;

	public SlideMenuItem(String header) {
		this(null, -1, 0);
		mHeader = header;
	}

	public SlideMenuItem(String mText, int mIcon, int mCount) {
		super();
		this.mTitle = mText;
		this.mIcon = mIcon;
		this.mCount = mCount;
		mHeader = null;
	}

	public String getHeader() {
		return mHeader;
	}

	public void setHeader(String mHeader) {
		this.mHeader = mHeader;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int mCount) {
		this.mCount = mCount;
	}

	public String getText() {
		return mTitle;
	}

	public void setText(String mText) {
		this.mTitle = mText;
	}

	public int getIcon() {
		return mIcon;
	}

	public void setIcon(int mIcon) {
		this.mIcon = mIcon;
	}

}
