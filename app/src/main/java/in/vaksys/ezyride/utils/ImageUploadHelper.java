package in.vaksys.ezyride.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import in.vaksys.ezyride.R;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.ImageUploadResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harsh on 04-07-2016.
 */
public class ImageUploadHelper implements ProgressRequestBody.UploadCallbacks {

    private final Context context;
    Call<ImageUploadResponse> responseCall;
    ApiInterface apiService;
    Dialog confirm;
    ArcProgress arcProgress;
    Utils utils;
    private static final String TAG = "ImageUploadHelper";
    private String ImageUrl;

    public ImageUploadHelper(Context context) {
        this.context = context;
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        utils = new Utils((Activity) context);
        ImageUploadDialog();
    }

    public void Upload(File files, String apiKey, final boolean ImageType, String NamePrefix) {
        utils.showLog(TAG, files.toString());
        confirm.show();

        ProgressRequestBody requestBody = new ProgressRequestBody(files, this);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("car_image", NamePrefix + String.valueOf((System.currentTimeMillis() / 1000)) + ".jpg", requestBody);

        responseCall = apiService.IMAGE_UPLOAD_RESPONSE_CALL(apiKey, body);

        responseCall.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                confirm.dismiss();
//                pDialog.hideDialog();
                utils.showLog(TAG, String.valueOf(response.code()));
                if (response.code() == 200) {
                    ImageUploadResponse response1 = response.body();
                    utils.showLog(TAG, String.valueOf(response.body().isError()));
                    if (!response.body().isError()) {
                        ImageUrl = response.body().getUrl();
                        utils.showLog(TAG, ImageUrl);
                        Toast.makeText(context, "Image Uploaded...", Toast.LENGTH_SHORT).show();
                        if (!ImageUrl.isEmpty()) {
                            EventBus.getDefault().post(new UploadImageCallBack(ImageUrl, ImageType));
                        } else
                            Toast.makeText(context, "There are some error. Please Upload Image Again.", Toast.LENGTH_SHORT).show();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                confirm.dismiss();
                if (call.isCanceled()) {
                    Toast.makeText(context, "Image Upload Canceled.", Toast.LENGTH_SHORT).show();
                } else {
                    utils.ShowError();
                }
                Log.e(TAG, "onFailure: Error");

            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {
        arcProgress.setProgress(percentage);
    }

    @Override
    public void onError() {
        confirm.dismiss();
    }

    @Override
    public void onFinish() {
        confirm.dismiss();
    }

    private void ImageUploadDialog() {
        confirm = new Dialog(context);
        confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirm.setContentView(R.layout.confirm_dialog);
        confirm.setCancelable(false);

        arcProgress = (ArcProgress) confirm.findViewById(R.id.arc_progress);
        arcProgress.setMax(100);

        Button cancelBtn = (Button) confirm.findViewById(R.id.et_context_cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseCall.isExecuted()) {
                    responseCall.cancel();
                }
                confirm.dismiss();
            }
        });

    }
}
