package photran.me.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gc.materialdesign.widgets.SnackBar;
import com.squareup.picasso.Picasso;

import photran.me.eLyrics.R;

/**
 * Created by ttpho on 27/01/2015.
 * http://blog.parse.com/2014/04/30/take-your-app-offline-with-parse-local-datastore/
 */
public class SettingUI {
    public static String URL_PHOTO_24_24 = "http://image.mp3.zdn.vn/thumb/240_240/{0}";

    public static void loadImageView(ImageView imageView,String url) {
        Picasso.with(imageView.getContext()).
                load(url)
                .placeholder(R.color.bg_actionbar_normal)
                .error(R.color.bg_actionbar_normal)
                .into(imageView);
    }

    public static SnackBar getSnackBarDishPlayErrorNetworking(Activity activity,View.OnClickListener onTapClicked) {
        SnackBar snackbar = new SnackBar(activity,
                activity.getResources().getString(R.string.error_network),
                activity.getResources().getString(R.string.tap_to_retry), onTapClicked);
       return snackbar;
    }

    public static String getImagePreView(String path) {
        String url = URL_PHOTO_24_24;
        return url.replace("{0}",path);
    }
    public static  View getFooter(Context context) {
        LayoutInflater  mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View footer = mInflater.inflate(R.layout.layout_footer,
                null);
        return footer;
    }

    public static Drawable getProgressDrawable(Context context){
        int[] colors = new int[4];
        colors[0] = context.getResources().getColor(android.R.color.holo_blue_bright);
        colors[1] = context.getResources().getColor(android.R.color.holo_red_light);
        colors[2] = context.getResources().getColor(android.R.color.holo_green_light);
        colors[3] = context.getResources().getColor(android.R.color.holo_orange_light);

        Drawable progressDrawable = new FoldingCirclesDrawable.Builder(context)
                        .colors(colors)
                        .build();
        return progressDrawable;
    }

    public static void settingProgressbar(ProgressBar googleProgress){
        Rect bounds = googleProgress.getIndeterminateDrawable().getBounds();
        googleProgress.setIndeterminateDrawable(SettingUI.getProgressDrawable(googleProgress.getContext()));
        googleProgress.getIndeterminateDrawable().setBounds(bounds);
    }

}
