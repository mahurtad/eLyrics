package photran.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import photran.me.eLyrics.R;
import photran.me.models.SettingUI;
import photran.me.models.VideoYoutube;

public class YouTubeVideoAdapter extends BaseAdapter {

	private List<VideoYoutube> mModelParses;
	private Context context;
	private LayoutInflater vi;

	public YouTubeVideoAdapter(List<VideoYoutube> modelParses, Context context) {
		super();
		this.mModelParses = modelParses;
		this.context = context;
		vi = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mModelParses.size();
	}

	@Override
	public VideoYoutube getItem(int position) {
		// TODO Auto-generated method stub
		return this.mModelParses.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = vi.inflate(R.layout.adapter_youtupe, parent, false);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.song_title);

			holder.imgPreView = (ImageView) convertView
					.findViewById(R.id.imgeYoutubePreView);
            holder.txtAuth = (TextView)convertView.findViewById(R.id.singers);

			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		VideoYoutube model = getItem(position);
        String title = model.getTitle();
        String [] splitTitle = title.split(":");
        if (splitTitle.length == 2) {
            // title
            holder.txtAuth.setText(splitTitle[0]);
            holder.txtTitle.setText(splitTitle[1]);
        } else {
            // title
            holder.txtTitle.setText(model.getTitle());
        }
		// image
        SettingUI.loadImageView(holder.imgPreView,model.getImagePreView());

		return convertView;
	}

	public static class ViewHolder {
		public TextView txtTitle;
        public TextView txtAuth;
		public ImageView imgPreView;
	}

}
