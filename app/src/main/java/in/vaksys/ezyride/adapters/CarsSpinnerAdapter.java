package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.vaksys.ezyride.R;
import in.vaksys.ezyride.responces.CarResponse;

/**
 * Created by Harsh on 18-02-2016.
 */
public class CarsSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private static final String TAG = "CarsSpinnerAdapter";
    private final List<CarResponse.CarsEntity> carsEntities;
    private static LayoutInflater inflater = null;

    public CarsSpinnerAdapter(Context context, List<CarResponse.CarsEntity> carsEntities) {
        this.context = context;
        this.carsEntities = carsEntities;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (null != carsEntities ? carsEntities.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        final View row;
        final CarResponse.CarsEntity item = carsEntities.get(position);
        if (convertView == null) {
            row = inflater.inflate(R.layout.spinner_layout_cars, null);
            holder = new Holder(row);
            row.setTag(holder);
        } else {
            row = convertView;
            holder = (Holder) row.getTag();
        }

        holder.mytext.setText(item.getCarModel());
        holder.idtext.setText(String.valueOf(item.getId()));
        return row;
    }

    public class Holder {
        TextView mytext, idtext;

        Holder(View v) {
            mytext = (TextView) v.findViewById(R.id.rowText);
            idtext = (TextView) v.findViewById(R.id.rowid);
        }

    }
}
