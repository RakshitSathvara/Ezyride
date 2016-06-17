package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;

/**
 * Created by Harsh on 27-01-2016.
 */
public class SearchRideAdapter extends RecyclerView.Adapter<SearchRideAdapter.ViewHolder> {


    private final Context context;
    private final String[] kmtext;

    public SearchRideAdapter(Context context, String[] kmtext) {
        this.context = context;
        this.kmtext = kmtext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_ride, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ridePrice.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), kmtext[position]));
    }


    @Override
    public int getItemCount() {
        return (null != kmtext ? kmtext.length : 0);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rider_img)
        CircleImageView riderImg;
        @Bind(R.id.MyratingBar)
        RatingBar MyratingBar;
        @Bind(R.id.shared_km)
        TextView sharedKm;
        @Bind(R.id.rider_name)
        TextView riderName;
        @Bind(R.id.ride_location)
        TextView rideLocation;
        @Bind(R.id.ride_time)
        TextView rideTime;
        @Bind(R.id.ride_seat)
        TextView rideSeat;
        @Bind(R.id.ride_price)
        TextView ridePrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
