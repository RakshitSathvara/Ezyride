package in.vaksys.ezyride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.MyCarAdapter;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.MyCarResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.DividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;

public class MyCarActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_my_car)
    RecyclerView recyclerViewMyCar;

    private static final String TAG = "MyCarActivity";
    ProgresDialog pDialog;
    Utils utils;
    ApiInterface apiService;
    @Bind(R.id.error_view)
    ErrorView errorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Init();
        NetworkCall();
    }

    private void Init() {
        utils = new Utils(this);
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);

        recyclerViewMyCar.setHasFixedSize(true);
        recyclerViewMyCar.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyCarActivity.this);
//        LinearLayoutManager gridLayoutManager =
//                new LinearLayoutManager(MyCarActivity.this, LinearLayoutManager.VERTICAL, true);
        layoutManager.scrollToPosition(0);
        recyclerViewMyCar.setLayoutManager(layoutManager);
        recyclerViewMyCar.addItemDecoration(new DividerItemDecoration(MyCarActivity.this, LinearLayoutManager.VERTICAL));
    }

    private void NetworkCall() {

        pDialog.DialogMessage("Getting Car list ...");
        pDialog.showDialog();

        Call<MyCarResponse> call = apiService.MY_CAR_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2");

        call.enqueue(new Callback<MyCarResponse>() {
            @Override
            public void onResponse(Call<MyCarResponse> call, Response<MyCarResponse> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
                    recyclerViewMyCar.setVisibility(View.VISIBLE);
                    errorView.setVisibility(View.GONE);

                    MyCarResponse response1 = response.body();

                    if (!response.body().isError()) {

                        List<MyCarResponse.CarsEntity> aa = response.body().getCars();
                        MyCarAdapter myCarAdapter = new MyCarAdapter(MyCarActivity.this, aa);

                        recyclerViewMyCar.setAdapter(myCarAdapter);

                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        setupErrorView(false);
                        Toast.makeText(MyCarActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setupErrorView(true);
                    Toast.makeText(MyCarActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyCarResponse> call, Throwable t) {
                setupErrorView(true);
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void setupErrorView(boolean ConnectionError) {
        recyclerViewMyCar.setVisibility(View.GONE);
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
                    NetworkCall();
                }
            });
        } else {
            errorView.setConfig(ErrorView.Config.create()
                    .image(R.drawable.car_not_found)
                    .title(getString(R.string.error_title_ops))
                    .titleColor(getResources().getColor(R.color.colorPrimary))
                    .subtitle(getString(R.string.error_subtitle_no_car_found))
                    .retryText(getString(R.string.error_view_add_car))
                    .retryTextColor(getResources().getColor(R.color.retryBtnColor))
                    .build());
            errorView.setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    startActivity(new Intent(MyCarActivity.this, CarDetailsActivity.class));
                    finish();
                }
            });
        }
    }

    public void SetErrorVisiblity() {
        if (Objects.equals(String.valueOf(errorView.getVisibility()), "0")) {

        }
    }
}
