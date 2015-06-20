package photran.me.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import photran.me.eLyrics.R;
import photran.me.models.SlideMenuItem;

public class SlideMenuAdapter extends BaseAdapter {


    public static ArrayList<SlideMenuItem>getSlideMenuItemList(Context context) {
        ArrayList<SlideMenuItem>slideMenuItems = new ArrayList<SlideMenuItem>();
        //TODO change icon
        slideMenuItems.add(new SlideMenuItem(context.getString(R.string.title_section1),R.drawable.ic_launcher,0));
        slideMenuItems.add(new SlideMenuItem(context.getString(R.string.title_section2),R.drawable.ic_launcher,0));

        return  slideMenuItems;
    }

	private ArrayList<SlideMenuItem> lstSlideMenuItems;
	private LayoutInflater mInflater;
	private int mIndexTemplate;

	public SlideMenuAdapter(Context context, ArrayList<SlideMenuItem> list) {
		mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		lstSlideMenuItems = list;
		initDataAdapter(context);
	}

	private void initDataAdapter(Context context) {

	}

	@Override
	public int getCount() {
		return lstSlideMenuItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lstSlideMenuItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		SlideMenuItem item = lstSlideMenuItems.get(pos);

		SlideMenuItemHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.adapter_menu_item, parent, false);
			holder = new SlideMenuItemHolder(v);
			v.setTag(holder);
		}
		holder = (SlideMenuItemHolder) v.getTag();
		holder.setValues(item);

		return v;
	}

	private static class SlideMenuItemHolder {
		private TextView txtTitle;


		public SlideMenuItemHolder(View v) {
			txtTitle = (TextView) v.findViewById(R.id.h2_item_title);
		}

		public void setValues(SlideMenuItem item) {
			txtTitle.setText(item.getText());
		}
	}
}
