package in.vaksys.ezyride.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.activities.CarDetailsActivity;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.CarRegisterResponse;
import in.vaksys.ezyride.responces.MyCarResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.PreferenceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harsh on 01-07-2016.
 */
public class MyCarAdapter extends RecyclerView.Adapter<MyCarAdapter.ViewHolder> {

    private static final String TAG = "MyCarAdapter";
    private final Context context;
    private final List<MyCarResponse.CarsEntity> carsEntities;
    Utils utils;
    private ProgresDialog pDialog;
    ApiInterface apiService;


    public MyCarAdapter(Context context, List<MyCarResponse.CarsEntity> carsEntities) {
        this.context = context;
        this.carsEntities = carsEntities;
        utils = new Utils((Activity) context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_my_car, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyCarResponse.CarsEntity entity = carsEntities.get(position);

        holder.HiddenCarId.setText(String.valueOf(entity.getId()));
        holder.mCarName.setText(entity.getCarModel());
        holder.mCarNumber.setText(entity.getCarNo());
        // TODO: 01-07-2016 We have to fetch seat numbers from car layout id
        holder.mCarSeats.setText(String.format("%s Seats", String.valueOf(entity.getCarLayout())));
        // TODO: 01-07-2016 Error and PlceHolder ICon Remaining
        Glide.with(context)
                .load(entity.getCarImage())
                .priority(Priority.HIGH)
                .crossFade()
                .into(holder.mCarImage);

        if (entity.getAcAvailability() == 1) {
            Glide.with(context)
                    .load(R.drawable.ic_air_conditioner_on)
                    .crossFade()
                    .into(holder.mAcStatus);
        }
        if (entity.getMusicSystem() == 1) {
            Glide.with(context)
                    .load(R.drawable.ic_music_on)
                    .crossFade()
                    .into(holder.mMusicStatus);
        }
        if (entity.getAirBag() == 1) {
            Glide.with(context)
                    .load(R.drawable.ic_car_air_bag_on)
                    .crossFade()
                    .into(holder.mAirBagStatus);
        }
        if (entity.getSeatBelt() == 1) {
            Glide.with(context)
                    .load(R.drawable.ic_car_seat_belt_on)
                    .crossFade()
                    .into(holder.mSeatBeltStatus);
        }
        final int MyPos = holder.getAdapterPosition();
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCarResponse.CarsEntity entity = carsEntities.get(MyPos);
                PreferenceHelper helper = new PreferenceHelper(context, AppConfig.PREF_CAR_FILE_NAME);
                helper.initPref();
                helper.SaveStringPref(AppConfig.PREF_CAR_MODEL, entity.getCarModel());
                helper.SaveStringPref(AppConfig.PREF_CAR_NUMBER, entity.getCarNo());
                helper.SaveIntPref(AppConfig.PREF_CAR_LAYOUT, entity.getCarLayout());
                helper.SaveStringPref(AppConfig.PREF_CAR_IMG_URL, entity.getCarImage());
                helper.SaveIntPref(AppConfig.PREF_CAR_ID, entity.getId());
                helper.SaveIntPref(AppConfig.PREF_CAR_AC, entity.getAcAvailability());
                helper.SaveIntPref(AppConfig.PREF_CAR_SEAT_BELT, entity.getSeatBelt());
                helper.SaveIntPref(AppConfig.PREF_CAR_SEAT_AIR_BAG, entity.getAirBag());
                helper.SaveIntPref(AppConfig.PREF_CAR_MUSIC, entity.getMusicSystem());
                helper.SaveBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, true);
                helper.ApplyPref();
                context.startActivity(new Intent(context, CarDetailsActivity.class));
                ((Activity) context).finish();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCarResponse.CarsEntity entity = carsEntities.get(MyPos);
                int carid = entity.getId();
                DeleteCar(carid,MyPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != carsEntities ? carsEntities.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.mCar_image)
        CircleImageView mCarImage;
        @Bind(R.id.mCar_name)
        TextView mCarName;
        @Bind(R.id.mCar_number)
        TextView mCarNumber;
        @Bind(R.id.mCar_seats)
        TextView mCarSeats;
        @Bind(R.id.btn_edit)
        ImageButton btnEdit;
        @Bind(R.id.btn_delete)
        ImageButton btnDelete;
        @Bind(R.id.mMusic_status)
        ImageView mMusicStatus;
        @Bind(R.id.mAc_status)
        ImageView mAcStatus;
        @Bind(R.id.mSeatBelt_status)
        ImageView mSeatBeltStatus;
        @Bind(R.id.mAirBag_status)
        ImageView mAirBagStatus;
        @Bind(R.id.mCar_Id)
        TextView HiddenCarId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void DeleteCar(int carid, final int myPos) {
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(context);
        pDialog.createDialog(false);

        pDialog.DialogMessage("Deleting Car ...");
        pDialog.showDialog();

        Call<CarRegisterResponse> carRegisterResponseCall = apiService.CAR_DELETE_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2", carid);

        carRegisterResponseCall.enqueue(new Callback<CarRegisterResponse>() {
            @Override
            public void onResponse(Call<CarRegisterResponse> call, Response<CarRegisterResponse> response) {
                pDialog.hideDialog();
                utils.showLog(TAG, String.valueOf(response.code()));
                if (response.code() == 200) {
                    CarRegisterResponse response1 = response.body();
                    utils.showLog(TAG, String.valueOf(response1.isError()));
                    if (!response.body().isError()) {

                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(context, "Car SuccessFully Deleted ...", Toast.LENGTH_SHORT).show();
                        carsEntities.remove(myPos);
                        notifyDataSetChanged();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarRegisterResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }
}
