package in.vaksys.ezyride.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.MyApplication;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.fragments.SearchRideFragment;
import in.vaksys.ezyride.fragments.NavigationDrawerFragment;
import in.vaksys.ezyride.fragments.RegisterDialog;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.NavigationDrawerCallbacks;
import in.vaksys.ezyride.utils.PreferenceHelper;

public class HomeActivity extends AppCompatActivity implements NavigationDrawerCallbacks {


    private static final String TAG = "harsh";
    /*@Bind(R.id.input_user_name)
    EditText inputUserName;
    @Bind(R.id.input_layout_username)
    TextInputLayout inputLayoutUsername;
    @Bind(R.id.input_password)
    EditText inputPassword;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;*/
    /*@Bind(R.id.btn_login)
    Button btnLogin;*/
    private String fid;
    private String fname;
    private String femail;
    private String fgender;
    private String fbirthday;
    @Bind(R.id.btn_facebook_login)
    Button btnFacebookLogin;

    @Bind(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Bind(R.id.btnLinkToRegisterScreen)
    Button btnLinkToRegisterScreen;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    FragmentManager fm;
    FragmentTransaction ft;
    int tempflag = 0;
    Dialog dialog;

    PreferenceHelper helper;
    // TODO: 23-06-2016 Google Place API for Release version is left

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(MyApplication.getInstance());
        setContentView(R.layout.activity_home);
//        ButterKnife.bind(this);
        forFacebook();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        callbackManager = CallbackManager.Factory.create();
        helper = new PreferenceHelper(this, AppConfig.PREF_USER_FILE_NAME);

        tempflag++;
        Log.e(TAG, "onCreate: " + tempflag);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("Dhaval Thakor", "dhaval@vakratunda.com",
                BitmapFactory.decodeResource(getResources(), R.drawable.user));

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.container, new SearchRideFragment());
        ft.commit();

        dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting custom layout to dialog
        dialog.setContentView(R.layout.custom_dialog_layout);
        ButterKnife.bind(this, dialog);

        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_friends", "user_about_me", "public_profile",
                "user_birthday");

        loginButton.setReadPermissions(permissionNeeds);

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        Log.i("LoginActivity", response.toString());

                                        Log.i("LoginActivity", object.toString());
                                        try {
                                            fid = object.getString("id");
                                            try {
                                                URL profile_pic = new URL(
                                                        "http://graph.facebook.com/" + fid + "/picture?type=large");
                                                Log.i("profile_pic", profile_pic + "");

                                                fname = object.getString("name");
                                                femail = object.getString("email");
                                                fgender = object.getString("gender");
                                                fbirthday = object.getString("birthday");

                                                Log.d("LoginActivity", fname + femail + fgender + fbirthday);

                                                helper.initPref();
                                                helper.SaveStringPref(AppConfig.PREF_USER_NAME, fname);
                                                helper.SaveStringPref(AppConfig.PREF_USER_MAIL_ID, femail);
                                                helper.SaveStringPref(AppConfig.PREF_USER_BIRTHDATE, fbirthday);
                                                helper.SaveStringPref(AppConfig.PREF_USER_PROFILR_PIC, profile_pic.toString());
                                                helper.SaveIntPref(AppConfig.PREF_USER_ENTRY_TIME, 1);
                                                helper.SaveBooleanPref(AppConfig.PREF_USER_LOGIN_STATUS, true);
                                                helper.ApplyPref();
                                                Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

    }

    /*public void startAnimation() {
        // Create an animation
        RotateAnimation rotation = new RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotation.setDuration(1200);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatMode(Animation.RESTART);
        rotation.setRepeatCount(Animation.INFINITE);

        // and apply it to your imageview
        findViewById(R.id.myActivityIndicator).startAnimation(rotation);
    }*/
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the create_menu content by replacing fragments

        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                Log.e(TAG, "onNavigationDrawerItemSelected Outside: " + tempflag);

                if (tempflag == 0) {
                    Log.e(TAG, "onNavigationDrawerItemSelected 0 : " + tempflag);
                    fm = getSupportFragmentManager();
                    ft = fm.beginTransaction();
                    ft.add(R.id.container, new SearchRideFragment());
                    ft.commit();
                    tempflag++;
                    break;
                } else if (tempflag > 0) {
                    Log.e(TAG, "onNavigationDrawerItemSelected 1 : " + tempflag);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.container, new SearchRideFragment());
                    ft.commit();
                    break;
                }
            case 1:

//                dialog.setTitle("Custom Dialog");
//
//                //adding button click event
//                Button login = (Button) dialog.findViewById(R.id.btn_login);

               /* btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/
                btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginButton.performClick();
                    }
                });
                btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case 2:
                startActivity(new Intent(HomeActivity.this, SearchRideActivity.class));
                break;
            case 3:
                startActivity(new Intent(HomeActivity.this, CarDetailsActivity.class));
                break;
            case 4:
                Intent profileIntent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivity(profileIntent);
                break;
            case 5:
                Intent myRideIntent = new Intent(HomeActivity.this, MyRideActivity.class);
                startActivity(myRideIntent);
                break;
            case 6:
                Intent details = new Intent(HomeActivity.this, DetailsActivity.class);
                startActivity(details);
                break;
            case 7:
                Intent map = new Intent(HomeActivity.this, SearchLocationActivity.class);
                startActivity(map);
                break;
            case 8:
                android.app.FragmentManager fm11 = getFragmentManager();
                RegisterDialog registerDialog = new RegisterDialog();
                registerDialog.show(fm11, "tag");
                break;
            case 9:
                Intent mycars = new Intent(HomeActivity.this, MyCarActivity.class);
                startActivity(mycars);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void forFacebook() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                Log.d("Harsh",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Utils(this).ExitSnackBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
