package in.vaksys.ezyride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import in.vaksys.ezyride.R;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ToggleButton seatOne, seatTwo, seatThree;
    int seats = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        seatOne = (ToggleButton) findViewById(R.id.seatOne);
        seatThree = (ToggleButton) findViewById(R.id.seatThree);
        seatTwo = (ToggleButton) findViewById(R.id.seatTwo);

        setSupportActionBar(toolbar);

        setTitle("Details");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seatOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seats += 1;
                    Toast.makeText(DetailsActivity.this, "Seat one is book : " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
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
                    Toast.makeText(DetailsActivity.this, "Seat two is book " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
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
                    Toast.makeText(DetailsActivity.this, "Seat three is book " + seats, Toast.LENGTH_SHORT).show();
                } else {
                    if (seats != 0) {
                        seats -= 1;
                        Toast.makeText(DetailsActivity.this, "Seat three cancel : " + seats, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
