package in.vaksys.ezyride.activities;

import android.os.Bundle;
import android.support.percent.PercentFrameLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.responces.SearchRideResponse;

public class DetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.MyratingBar)
    RatingBar MyratingBar;
    @Bind(R.id.nav_header_container)
    LinearLayout navHeaderContainer;
    @Bind(R.id.seatOne)
    ToggleButton seatOne;
    @Bind(R.id.seatTwo)
    ToggleButton seatTwo;
    @Bind(R.id.seatThree)
    ToggleButton seatThree;
    @Bind(R.id.ThreeSeatFrame)
    PercentFrameLayout ThreeSeatFrame;
    @Bind(R.id.FiveseatOne)
    ToggleButton FiveseatOne;
    @Bind(R.id.FiveseatTwo)
    ToggleButton FiveseatTwo;
    @Bind(R.id.FiveseatThree)
    ToggleButton FiveseatThree;
    @Bind(R.id.FiveseatFour)
    ToggleButton FiveseatFour;
    @Bind(R.id.FiveseatFive)
    ToggleButton FiveseatFive;
    @Bind(R.id.FiveSeatFrame)
    PercentFrameLayout FiveSeatFrame;
    @Bind(R.id.BtnPayNow)
    Button BtnPayNow;
    int seats = 0;
    @Bind(R.id.bMusic_status)
    ImageView bMusicStatus;
    @Bind(R.id.bAc_status)
    ImageView bAcStatus;
    @Bind(R.id.bSeatBelt_status)
    ImageView bSeatBeltStatus;
    @Bind(R.id.bAirBag_status)
    ImageView bAirBagStatus;

    List<SearchRideResponse.RidesEntity> DetailList;
    @Bind(R.id.RiderName)
    TextView RiderName;
    @Bind(R.id.RiderAge)
    TextView RiderAge;
    @Bind(R.id.RiderGender)
    TextView RiderGender;
    @Bind(R.id.RiderImage)
    CircleImageView RiderImage;
    private int PircePerSeat;
    private int totalAmountSeat;
    private String Rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Rs = getResources().getString(R.string.Rs);
        setTitle("Details");

        /*Bundle getBundle = this.getIntent().getExtras();
        if (!getBundle.isEmpty()) {
            DetailList = getBundle.getParcelableArrayList(AppConfig.DETAIL_LIST);
            int position = getBundle.getInt(AppConfig.DETAIL_POSITION);
            loadData(DetailList, position);
        } else {
            Toast.makeText(DetailsActivity.this, "Empty Parameters ", Toast.LENGTH_SHORT).show();
            // TODO: 11-07-2016 handle this
        }*/


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seatOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "Seat one is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "Seat one cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        seatTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "Seat two is book " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "Seat two cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        seatThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "Seat three is book " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "Seat three cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        FiveseatOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "Five Seat one is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "Five Seat one cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FiveseatTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "FiveseatTwo is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "FiveseatTwo cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FiveseatThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "FiveseatThree is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "FiveseatThree cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FiveseatFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "FiveseatFour is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "FiveseatFour cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FiveseatFive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    CountTotalSeatMoney();
                    Toast.makeText(DetailsActivity.this, "FiveseatFive is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        CountTotalSeatMoney();
                        Toast.makeText(DetailsActivity.this, "FiveseatFive cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadData(List<SearchRideResponse.RidesEntity> detailList, int position) {
        SearchRideResponse.RidesEntity a = detailList.get(position);
        PircePerSeat = a.getPricePerSeat();
        RiderName.setText(a.getUsername());

        Glide.with(this)
                .load(a.getUserImage())
                .crossFade()
                .placeholder(R.drawable.large)
                .error(R.drawable.large)
                .centerCrop()
                .dontAnimate()
                .into(RiderImage);

        if (a.getSeatAvailability() == 1) {
            ThreeSeatFrame.setVisibility(View.VISIBLE);
        } else {
            FiveSeatFrame.setVisibility(View.VISIBLE);
        }
        /*if (entity.getAcAvailability() == 1) {
            Glide.with(this)
                    .load(R.drawable.ic_air_conditioner_on)
                    .crossFade()
                    .into(bAcStatus);
        }
        if (entity.getMusicSystem() == 1) {
            Glide.with(this)
                    .load(R.drawable.ic_music_on)
                    .crossFade()
                    .into(bMusicStatus);
        }
        if (entity.getAirBag() == 1) {
            Glide.with(this)
                    .load(R.drawable.ic_car_air_bag_on)
                    .crossFade()
                    .into(bAirBagStatus);
        }
        if (entity.getSeatBelt() == 1) {
            Glide.with(this)
                    .load(R.drawable.ic_car_seat_belt_on)
                    .crossFade()
                    .into(bSeatBeltStatus);
        }*/
    }

    @OnClick(R.id.BtnPayNow)
    public void onClick() {
        if (seats > 1) {
            Toast.makeText(DetailsActivity.this, "You can't Proceed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetailsActivity.this, "You can Proceed", Toast.LENGTH_SHORT).show();
            CountTotalSeatMoney();
        }
    }

    private void CountTotalSeatMoney() {
        totalAmountSeat = seats * PircePerSeat;
        BtnPayNow.setText(String.format(Rs, "Pay %s %d", totalAmountSeat));
    }

}
