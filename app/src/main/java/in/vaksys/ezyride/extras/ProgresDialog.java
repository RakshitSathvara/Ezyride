package in.vaksys.ezyride.extras;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Harsh on 22-06-2016.
 */
public class ProgresDialog {

    private ProgressDialog pDialog;
    private Context activity;

    public ProgresDialog(Context activity) {
        this.activity = activity;
    }

    public void createDialog(boolean cancelable) {
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(cancelable);

    }

    public void DialogMessage(String message) {
        pDialog.setMessage(message);
    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
