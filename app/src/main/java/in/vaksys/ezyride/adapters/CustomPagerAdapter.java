package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;

/**
 * Created by dell980 on 4/26/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {

    int[] image;
    String[] name;
    String[] pm;
    String[] rs;
    String[] km;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context, int[] image, String[] km, String[] name, String[] pm, String[] rs) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.image = image;
        this.name = name;
        this.pm = pm;
        this.rs = rs;
        this.km = km;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        Holder holder = new Holder();

        holder.image = (CircleImageView) itemView.findViewById(R.id.image);

        holder.km = (TextView) itemView.findViewById(R.id.km);
        holder.name = (TextView) itemView.findViewById(R.id.name);
        holder.pm = (TextView) itemView.findViewById(R.id.pm);
        holder.rs = (TextView) itemView.findViewById(R.id.rs);

        holder.image.setImageResource(image[position]);
        holder.km.setText(km[position]);
        holder.name.setText(name[position]);
        holder.pm.setText(pm[position]);
        holder.rs.setText(rs[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public class Holder {
        CircleImageView image;
        TextView km, name, pm, rs;
    }
}