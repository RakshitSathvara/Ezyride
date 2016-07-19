package in.vaksys.ezyride.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.responces.SearchRideResponse;
import in.vaksys.ezyride.utils.MapCallback;

/**
 * Created by Harsh on 08-07-2016.
 */
public class MapListAdapter extends RecyclerView.Adapter<MapListAdapter.ViewHolder> {

    private final Context context;
    private final List<SearchRideResponse.RidesEntity> ridesEntities;

    public MapListAdapter(Context context, List<SearchRideResponse.RidesEntity> ridesEntities) {
        this.context = context;
        this.ridesEntities = ridesEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchRideResponse.RidesEntity ridesEntity = ridesEntities.get(position);

        holder.RidePrice.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), ridesEntity.getPricePerSeat()));
        holder.Drivername.setText(ridesEntity.getUsername());
        holder.RideTime.setText(ridesEntity.getRideTime());

        Glide.with(context)
                .load(ridesEntity.getUserImage())
                .crossFade()
                .placeholder(R.drawable.large)
                .error(R.drawable.large)
                .centerCrop()
                .dontAnimate()
                .into(holder.UserImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MapCallback(ridesEntity.getFromLat(), ridesEntity.getFromLong()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != ridesEntities ? ridesEntities.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.Card)
        CardView cardView;
        @Bind(R.id.User_image)
        CircleImageView UserImage;
        @Bind(R.id.MyratingBar)
        RatingBar MyratingBar;
        @Bind(R.id.SharedKm)
        TextView SharedKm;
        @Bind(R.id.Drivername)
        TextView Drivername;
        @Bind(R.id.RideTime)
        TextView RideTime;
        @Bind(R.id.RidePrice)
        TextView RidePrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
