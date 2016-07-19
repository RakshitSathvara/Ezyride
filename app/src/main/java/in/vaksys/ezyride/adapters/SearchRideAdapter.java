package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.activities.DetailsActivity;
import in.vaksys.ezyride.responces.SearchRideResponse;
import in.vaksys.ezyride.utils.AppConfig;

/**
 * Created by Harsh on 27-01-2016.
 */
public class SearchRideAdapter extends RecyclerView.Adapter<SearchRideAdapter.ViewHolder> {

    private final Context context;
    private final List<SearchRideResponse.RidesEntity> searchRideResponses;
    Date newdate;
    Date Todaydate;

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
        final SearchRideResponse.RidesEntity ridesEntity = searchRideResponses.get(position);

        holder.ridePrice.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), String.valueOf(ridesEntity.getPricePerSeat())));
        holder.riderName.setText(ridesEntity.getUsername());
        holder.rideLocation.setText(String.format("From %s to %s", ridesEntity.getFromMainAddress(), ridesEntity.getToMainAddress()));
        holder.rideTime.setText(ridesEntity.getRideTime());
        holder.rideSeat.setText(String.valueOf(ridesEntity.getSeatAvailability()));
        Glide.with(context)
                .load(ridesEntity.getUserImage())
                .crossFade()
                .placeholder(R.drawable.large)
                .error(R.drawable.large)
                .centerCrop()
                .dontAnimate()
                .into(holder.riderImg);
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            newdate = df.parse(ridesEntity.getRideDate());
            Todaydate = df.parse(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.DetailClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(AppConfig.DETAIL_LIST, (ArrayList<SearchRideResponse.RidesEntity>) searchRideResponses);
//                bundle.putInt(AppConfig.DETAIL_POSITION, holder.getAdapterPosition());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConfig.BUNDLE_PRICE_PER_SEAT, ridesEntity.getPricePerSeat());
                bundle.putString(AppConfig.BUNDLE_USER_NAME, ridesEntity.getUsername());
                bundle.putString(AppConfig.BUNDLE_USER_IMAGE_URL, ridesEntity.getUserImage());
                bundle.putInt(AppConfig.BUNDLE_USER_ID, ridesEntity.getUserId());
                bundle.putInt(AppConfig.BUNDLE_CAR_ID, ridesEntity.getCarId());



                Intent intent = new Intent(context, DetailsActivity.class).putExtras(bundle);
                context.startActivity(intent);

            }
        });
        // TODO: 11-07-2016 Date Validation baki 6
        /*if (Todaydate.compareTo(newdate) > 0) {
            Toast.makeText(context, "You cant Proceed", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConfig.BUNDLE_PRICE_PER_SEAT, ridesEntity.getPricePerSeat());
            bundle.putString(AppConfig.BUNDLE_USER_NAME, ridesEntity.getUsername());
        }*/

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
        @Bind(R.id.DetailView)
        View DetailClick;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
