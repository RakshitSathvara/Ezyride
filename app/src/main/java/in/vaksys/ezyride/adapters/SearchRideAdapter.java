package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.responces.SearchRideResponse;

/**
 * Created by Harsh on 27-01-2016.
 */
public class SearchRideAdapter extends RecyclerView.Adapter<SearchRideAdapter.ViewHolder> {

    private final Context context;
    private final List<SearchRideResponse.RidesEntity> searchRideResponses;

    public SearchRideAdapter(Context context, List<SearchRideResponse.RidesEntity> searchRideResponses) {
        this.context = context;
        this.searchRideResponses = searchRideResponses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_ride, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SearchRideResponse.RidesEntity ridesEntity = searchRideResponses.get(position);

        holder.ridePrice.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), ridesEntity.getPricePerSeat()));
        holder.riderName.setText(ridesEntity.getUsername());
        holder.rideLocation.setText(String.format("From %s to %s", ridesEntity.getFromMainAddress(), ridesEntity.getToMainAddress()));
        holder.rideTime.setText(ridesEntity.getRideTime());
        holder.rideSeat.setText(ridesEntity.getSeatAvailability());
        Glide.with(context)
                .load(ridesEntity.getUserImage())
                .crossFade()
                .placeholder(R.drawable.large)
                .error(R.drawable.large)
                .centerCrop()
                .dontAnimate()
                .into(holder.riderImg);
    }


    @Override
    public int getItemCount() {
        return (null != searchRideResponses ? searchRideResponses.size() : 0);
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
