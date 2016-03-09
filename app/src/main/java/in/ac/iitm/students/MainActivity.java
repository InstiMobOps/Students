package in.ac.iitm.students;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import in.ac.iitm.students.Fragments.AcademicCalendarFragment;
import in.ac.iitm.students.Fragments.EventsFragment;
import in.ac.iitm.students.Fragments.FeedbackFragment;
import in.ac.iitm.students.Fragments.GalleryFragment;
import in.ac.iitm.students.Fragments.MapFragment;
import in.ac.iitm.students.Fragments.ImportantContacts;
import in.ac.iitm.students.Fragments.TheFifthEstateFragment;
import in.ac.iitm.students.Gcm.QuickstartPreferences;
import in.ac.iitm.students.Gcm.RegistrationIntentService;
import in.ac.iitm.students.Utils.Strings;
import in.ac.iitm.students.Utils.Utils;
import in.ac.iitm.students.Views.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RelativeLayout profilePic ;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);
        View headerView = navigationView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.dispname) ;
        TextView rollno = (TextView) headerView.findViewById(R.id.navrollno) ;
        name.setText(Utils.getprefString(Strings.NAME,this));
        rollno.setText(Utils.getprefString(Strings.ROLLNO,this));

        ImageView circleImageView = (ImageView) headerView.findViewById(R.id.profile_image);
        Glide.with(this)
                .load(getString(R.string.urlImage)+ Utils.getprefString(Strings.ROLLNO,this))
                .centerCrop()
                .crossFade()
                .into(circleImageView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });



        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new MapFragment());
        fragmentTransaction.commit();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            Log.d("hai there", "hai");

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                Log.d("arun message to you", "token sent to server");
                } else {
                    Log.d("arun message to you", "some error couldn't  sent token to server");
                }
            }
        };
  /*      profilePic =(RelativeLayout) findViewById(R.id.header);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivity = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(startActivity);
            }
        });*/

    }
    public void openDrawer()
    {
        drawer.openDrawer(Gravity.LEFT);
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        showViews();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_contacts) {

            fragmentTransaction.replace(R.id.fragment_container, new ImportantContacts());
        } else if (id == R.id.nav_map) {
            hideViews();
            fragmentTransaction.replace(R.id.fragment_container, new MapFragment());
        } else if  (id == R.id.nav_gallery) {
            fragmentTransaction.replace(R.id.fragment_container, new GalleryFragment());
        }else if (id==R.id.nav_fifthestate){
            fragmentTransaction.replace(R.id.fragment_container, new TheFifthEstateFragment());

        } else if(id==R.id.nav_feedback){
            fragmentTransaction.replace(R.id.fragment_container, new FeedbackFragment());

        }else if(id==R.id.nav_academiccalender){
            fragmentTransaction.replace(R.id.fragment_container, new AcademicCalendarFragment());

        }else if(id==R.id.nav_reportbug){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "institutewebops@gmail.com", null));

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            String debug_info = "\n\n\n Device information \n -------------------------------";
            try{
                debug_info += "\n Netaccess App version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            }catch (PackageManager.NameNotFoundException nne){
                Log.e("About", "Name not found exception");
            }
            debug_info += "\n Android Version: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT+ ") \n Model (and product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ") \n Device: " + android.os.Build.DEVICE;

            emailIntent.putExtra(Intent.EXTRA_TEXT, debug_info);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IITM Students App: Feedback / bug report");
            startActivity(Intent.createChooser(emailIntent, "Send feedback / bug report"));
        }
        else if (id == R.id.nav_netaccess) {
            Intent intent = new Intent(this,NetaccessActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_share) {
            String string ="<a ref=\"https://play.google.com/store/apps/details?id="+getPackageName()+"\">IITM Students App</a>" ;
            Intent s = new Intent(android.content.Intent.ACTION_SEND);

            s.setType("text/plain");
            s.putExtra(Intent.EXTRA_SUBJECT, "SAmple");
            s.putExtra(Intent.EXTRA_TEXT, string);

            startActivity(Intent.createChooser(s, "Quote"));
        } else if (id == R.id.web_site) {
            String url="https://students.iitm.ac.in/";
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
               startActivity(intent);
            }
        }else if (id == R.id.nav_logout){
            Utils.clearpref(this);
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideViews() {
        getSupportActionBar().hide();

    }

    public void showViews() {
        getSupportActionBar().show();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
