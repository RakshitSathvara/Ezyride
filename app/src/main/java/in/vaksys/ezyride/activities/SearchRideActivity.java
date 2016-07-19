package in.vaksys.ezyride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.ViewPagerAdapter;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.fragments.SearchRideListFragment;

public class SearchRideActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    Date myDate;
    private int Posi;

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

    public static String addDays(Date date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return df.format(cal.getTime());
    }

    void setupViewPager(ViewPager viewPager) {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());
        try {
            myDate = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(SearchRideListFragment.getInstance(Utils.addDays(myDate, -1)), Utils.addDays(myDate, -1));
        adapter.addFragment(SearchRideListFragment.getInstance(formattedDate), formattedDate);
        adapter.addFragment(SearchRideListFragment.getInstance(Utils.addDays(myDate, 1)), Utils.addDays(myDate, 1));

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
            Log.e("Search", "onOptionsItemSelected: ");
            Posi = pager.getCurrentItem();
            Intent intent = new Intent(this, SearchLocationActivity.class).putExtra("posi", Posi);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_filter) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
