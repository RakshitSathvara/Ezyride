package in.vaksys.ezyride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;

import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;

public class Temp extends AppCompatActivity {

    @Bind(R.id.b1)
    Button b1;
    private Effects effect;
    String msg = "Today we’d like to share a couple of simple styles and effects for android notifications.";
    Configuration cfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);

        cfg = new Configuration.Builder().setAnimDuration(700)
                .setDispalyDuration(1500)
                .setBackgroundColor("#FFBDC3C7")
                .setTextColor("#FF444444")
                .setIconBackgroundColor("#FFFFFFFF")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                .setTextGravity(Gravity.BOTTOM)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();
    }

    @OnClick(R.id.b1)
    public void onClick() {
        effect = Effects.thumbSlider;

        NiftyNotificationView.build(this, msg, effect, R.id.mLyout, cfg)
                .setIcon(R.drawable.com_facebook_button_icon_blue)         //You must call this method if you use ThumbSlider effect
                .show();
    }

}
