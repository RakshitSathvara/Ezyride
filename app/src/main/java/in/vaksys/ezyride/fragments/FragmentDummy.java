package in.vaksys.ezyride.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.SearchRideAdapter;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.SearchRideResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.DividerItemDecoration;
import in.vaksys.ezyride.utils.PreferenceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDummy extends Fragment {
    @Bind(R.id.recycler_view_search_ride)
    RecyclerView recyclerViewSearchRide;
    @Bind(R.id.error_view_search)
    ErrorView errorView;
    SearchRideAdapter searchRideAdapter;
//    private String[] MyPrice = {"200", "200", "200", "200", "200", "200", "200", "200", "200", "200"};
    private static final String TAG = "FragmentDummy";

    ProgresDialog pDialog;
    PreferenceHelper helper;
    Utils utils;
    ApiInterface apiService;
    private String to_lat;
    private String to_long;
    private String from_lat;
    private String from_long;
    static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    static Calendar c = Calendar.getInstance();
    private static String TodayDate = df.format(c.getTime());

    public static FragmentDummy getInstance(String Date) {
        FragmentDummy fragmentDummy = new FragmentDummy();
        Bundle args = new Bundle();
        args.putString("position", Date);
        TodayDate = Date;
        fragmentDummy.setArguments(args);
        return fragmentDummy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, layout);

        utils = new Utils(getActivity());
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(getActivity());
        pDialog.createDialog(false);
        helper = new PreferenceHelper(getActivity(), AppConfig.PREF_SEARCH_FILE_NAME);

        recyclerViewSearchRide.setHasFixedSize(true);
        recyclerViewSearchRide.setItemAnimator(new DefaultItemAnimator());
//        searchRideAdapter = new SearchRideAdapter(getActivity(), MyPrice);
        RecyclerView.LayoutManager gridLayoutManager =
                new LinearLayoutManager(getActivity());
//        gridLayoutManager.scrollToPosition(0);
        recyclerViewSearchRide.setLayoutManager(gridLayoutManager);
        recyclerViewSearchRide.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        recyclerViewSearchRide.setAdapter(searchRideAdapter);

        to_lat = helper.LoadStringPref(AppConfig.PREF_SEARCH_TO_LAT, "0.0");
        to_long = helper.LoadStringPref(AppConfig.PREF_SEARCH_TO_LONG, "0.0");
        from_lat = helper.LoadStringPref(AppConfig.PREF_SEARCH_FROM_LAT, "0.0");
        from_long = helper.LoadStringPref(AppConfig.PREF_SEARCH_FROM_LONG, "0.0");


//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

//        TodayDate = df.format(c.getTime());
        NetworkCall(to_lat, to_long, from_lat, from_long, TodayDate);
//        NetworkCall("19.0759837", "72.8776559", "21.170240099999997", "72.83106070000001", TodayDate);

        return layout;

    }

    private void NetworkCall(String to_lat, String to_long, String from_lat, String from_long, String todayDate) {

        pDialog.DialogMessage("Fetching Available Rides ...");
        pDialog.showDialog();

        Call<SearchRideResponse> call = apiService.SEARCH_RIDE_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2"
                , to_lat, to_long, from_lat, from_long, todayDate);

        call.enqueue(new Callback<SearchRideResponse>() {
            @Override
            public void onResponse(Call<SearchRideResponse> call, Response<SearchRideResponse> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
                    if (recyclerViewSearchRide.getVisibility() == View.GONE) {
                        recyclerViewSearchRide.setVisibility(View.VISIBLE);
                        errorView.setVisibility(View.GONE);
                    }
                    SearchRideResponse response1 = response.body();

                    if (!response.body().isError()) {

                        List<SearchRideResponse.RidesEntity> aa = response.body().getRides();
//                        MyCarAdapter myCarAdapter = new MyCarAdapter(getActivity(), aa);
                        searchRideAdapter = new SearchRideAdapter(getActivity(), aa);
                        recyclerViewSearchRide.setAdapter(searchRideAdapter);

                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        setupErrorView(false);
                        Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setupErrorView(true);
                    Toast.makeText(getActivity(), "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchRideResponse> call, Throwable t) {
                setupErrorView(true);
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void setupErrorView(boolean ConnectionError) {
        recyclerViewSearchRide.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);

        if (ConnectionError) {
            errorView.setConfig(ErrorView.Config.create()
                    .image(R.drawable.errorimage)
                    .title(getString(R.string.error_title_ops))
                    .titleColor(getResources().getColor(R.color.colorPrimary))
                    .subtitle(getString(R.string.error_subtitle_no_connection))
                    .retryText(getString(R.string.error_view_retry))
                    .retryTextColor(getResources().getColor(R.color.retryBtnColor))
                    .build());

            errorView.setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    NetworkCall(to_lat, to_long, from_lat, from_long, TodayDate);
                }
            });
        } else {
            errorView.setConfig(ErrorView.Config.create()
                    .image(R.drawable.errorimage)
                    .title(getString(R.string.error_title_ops))
                    .titleColor(getResources().getColor(R.color.colorPrimary))
                    .subtitle(getString(R.string.error_subtitle_no_ride_found))
                    .retryText(getString(R.string.error_view_find_ride))
                    .retryTextColor(getResources().getColor(R.color.retryBtnColor))
                    .build());
            errorView.setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
