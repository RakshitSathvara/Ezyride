package in.vaksys.ezyride.extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * Created by Harsh on 22-06-2016.
 */
public class Utils {
    Activity activity;

    public Utils(Activity activity) {
        this.activity = activity;
    }

    public void showLog(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view1 = activity.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    public void ShowError() {
        Snackbar.with(activity)
                .type(SnackbarType.MULTI_LINE)
                .text("Check Internet Connection")
                .actionLabel("Done")
                .actionColor(Color.CYAN)
                .actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                .swipeToDismiss(true)
                .show(activity);
    }

    public void ExitSnackBar() {
        Snackbar.with(activity)
                .type(SnackbarType.MULTI_LINE)
                .text("Press BACK again to exit")
                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                .swipeToDismiss(true)
                .show(activity);
    }

}
