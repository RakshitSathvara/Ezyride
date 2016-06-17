package in.vaksys.ezyride.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.ViewPagerAdapter;
import in.vaksys.ezyride.fragments.FragmentDummy;

public class SearchRideActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupViewPager(pager);
//        pager.setPageTransformer(true, new DepthPageTransformer());
        tabs.setupWithViewPager(pager);
        pager.setCurrentItem(1);

    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    void setupViewPager(ViewPager viewPager) {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentDummy(), formattedDate);
        adapter.addFragment(new FragmentDummy(), formattedDate);
        adapter.addFragment(new FragmentDummy(), formattedDate);

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification1) {


            return true;
        }//noinspection SimplifiableIfStatement
        else if (id == R.id.action_map) {


            return true;
        } else if (id == R.id.action_filter) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
