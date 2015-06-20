package photran.me.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import photran.me.eLyrics.R;
import photran.me.models.SettingUI;
import photran.me.models.SongInforMp3;

/**
 * Created by ttpho on 25/01/2015.
 */
public class SongsAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SongInforMp3>mp3List = new ArrayList<SongInforMp3>();
    public SongsAdapter(Context context,List<SongInforMp3> _mp3List){
        mContext = context;
        this.mp3List = _mp3List;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return this.mp3List.size();
    }

    @Override
    public SongInforMp3 getItem(int position) {
        return this.mp3List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongViewHolder songViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_song_item, parent, false);
            songViewHolder = new SongViewHolder();
            songViewHolder.imgSongPhoto = (ImageView) convertView.findViewById(R.id.imgSongPhoto);
            songViewHolder.txtSongName = (TextView) convertView.findViewById(R.id.txtSongName);

            convertView.setTag(songViewHolder);
        }

        songViewHolder = (SongViewHolder) convertView.getTag();

        SongInforMp3 songInforMp3 = getItem(position);
        setValues(songViewHolder,songInforMp3);

        return  convertView;
    }
    private Context getContext() {
        return mContext;
    }

    public void setValues(SongViewHolder holder,SongInforMp3 songInforMp3) {

        SettingUI.loadImageView(holder.imgSongPhoto,
                SettingUI.getImagePreView(songInforMp3.getThumbnail()));

        holder.txtSongName.setText(songInforMp3.getTitle());
    }

    public static class SongViewHolder {
        public ImageView imgSongPhoto;
        public TextView txtSongName;
    }
}
