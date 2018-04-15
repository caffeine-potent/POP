package com.example.conic.enlargedlauncher;

import android.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AppListActivity extends Activity {
    private PackageManager manager;
    private List<AppDetail> apps;
    private ListView mList;
    private GridView mGrid;

    private Time mTime;
    private Runnable mRunnable;
    private Handler mHandler;
    private TextView mTimerValue;

    private long startTime = 0L;
    long timeInMilliseconds = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        startClock();
        loadApps();
        loadGridView();
        addClickListener();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }

    private Integer count = 0;
    Runnable updateTimerThread = new Runnable() {
        public void run() {
            String oof;
            if (Calendar.getInstance().getInstance().getTime().getHours() > 11){
                oof = "pm";
            }
            else{
                oof = "am";
            }
            SimpleDateFormat df = new SimpleDateFormat("HH:mm" );
            String formattedDate = df.format(Calendar.getInstance().getTime()) +" " + oof;

            mTimerValue.setText(formattedDate);
            mHandler.postDelayed(this, 1000);

        }

    };

    private void startClock(){

        mTimerValue = (TextView) findViewById(R.id.clocktext);
        startTime = SystemClock.uptimeMillis();
        mHandler = new Handler();




        mHandler.postDelayed(updateTimerThread, 0);




        //
//
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                Date currentTime = Calendar.getInstance().getTime();
//                Log.d("timer_test", currentTime.toString());
//            }
//        };
//
//        mHandler = new Handler();
//        mHandler.postDelayed(mRunnable, 1000);
    }



    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){

            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);

            HashMap<String, Drawable> appsAndAssets = new HashMap<>();
            appsAndAssets.put("Contacts", getResources().getDrawable( R.drawable.phonebook ));
            appsAndAssets.put("Facebook", getResources().getDrawable( R.drawable.facebook ));
            appsAndAssets.put("Messages", getResources().getDrawable( R.drawable.message));
            appsAndAssets.put("Camera",   getResources().getDrawable( R.drawable.camera));
            appsAndAssets.put("Gallery",  getResources().getDrawable( R.drawable.polaroids));
            appsAndAssets.put("Phone",    getResources().getDrawable( R.drawable.phone));
            appsAndAssets.put("Clock",    getResources().getDrawable( R.drawable.clock));
            appsAndAssets.put("Calendar", getResources().getDrawable( R.drawable.date));

            String[] keyArray = appsAndAssets.keySet().toArray(new String[appsAndAssets.keySet().size()]);
            Log.d("preif_label_name:", (String) app.label);
            Log.d("preif_label_name:", (String) app.name);

            List listOfAppNames = Arrays.asList(keyArray);
            if (listOfAppNames.contains(app.label)){

//                if (app.label == "Phone") {
//
//
//                }
//
                Log.d("postif_label_name:", (String) app.label);
                Log.d("postif_label_name:", (String) app.name);
                app.icon = appsAndAssets.get(app.label);
                apps.add(app);
            }
        }
    }



    private void loadGridView(){
        mGrid = (GridView)findViewById(R.id.gridView1);

        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this,
                R.layout.list_item,
                apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }
                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);
                return convertView;
            }
        };

        mGrid.setAdapter(adapter);
    }

    private void addClickListener(){
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {

                AppDetail app = apps.get(pos);
                Log.d("app_detail", app.name.toString());
                Log.d("app_detail", app.label.toString());
                if (app.label.equals("Phone")){

                    Log.d("Triggered!", "");
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    startActivity(intent);
                }
                else{
                    Intent i = manager.getLaunchIntentForPackage(apps.get(pos).name.toString());
                    AppListActivity.this.startActivity(i);

                }
            }
        });
    }


}
