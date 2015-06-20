package photran.me.models;

public class VideoYoutube {
	private String mLink;
	private String mImagePreView;
	private String mYoutubeId;
	private String mTitle;

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getYoutubeId() {
		return mYoutubeId;
	}

	public void setYoutubeId(String mYoutubeId) {
		this.mYoutubeId = mYoutubeId;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String mLink) {
		this.mLink = mLink;
	}

	public VideoYoutube(String mLink, String mTitle) {
		super();
		this.mLink = mLink;
		this.mTitle = mTitle;
		this.mYoutubeId = PMHelper.getYoutubeIdFormUrl(mLink);
		this.mImagePreView = PMHelper
				.getImagePreviewFromYoutupeId(this.mYoutubeId);
	}

	public String getImagePreView() {
		return mImagePreView;
	}

	public void setImagePreView(String mImagePreView) {
		this.mImagePreView = mImagePreView;
	}
}
