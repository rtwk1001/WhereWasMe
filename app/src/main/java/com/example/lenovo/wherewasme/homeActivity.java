package com.example.lenovo.wherewasme;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import static com.example.lenovo.wherewasme.R.color.light;

public class homeActivity extends TabActivity {
  private  static   final int PERMS_REQUEST_CODE=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final TabHost mTabHost = getTabHost();


        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator("SPY").setContent(new Intent(this ,Start.class )));
        mTabHost.addTab(mTabHost.newTabSpec("Current location ").setIndicator("MY LOCATION").setContent(new Intent(this  ,MainActivity.class )));

        mTabHost.setCurrentTab(0);
        mTabHost.setBackgroundColor(getResources().getColor(light));
        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions()){
                                        // our app has permissions.
                                                mTabHost.setCurrentTab(1);
                                   }
                               else {
                    //our app doesn't have permissions, So i m requesting permissions.
                                                requestPerms();
                            }
            }
        });



    }
    private boolean hasPermissions(){
               int res = 0;
                //string array of permissions,
                        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION };

                        for (String perms : permissions){
                        res = checkCallingOrSelfPermission(perms);
                     if (!(res == PackageManager.PERMISSION_GRANTED)){
                                return false;
                           }
                    }
                return true;
            }

                private void requestPerms(){
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(permissions,123);
                    }
            }

               @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                boolean allowed = true;

                       switch (requestCode){
                     case 123:

                                        for (int res : grantResults){
                                 // if user granted all permissions.
                                                allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                                   }

                                     break;
                        default:
                                // if user not granted permissions.
                            allowed = false;
                                break;
                   }

                        if (allowed){
                       //user granted all permissions we can perform our task
                    }
               else {
                        // we will give warning to user that they haven't granted permissions.
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                               if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                                        Toast.makeText(this, "Location Permissions denied.", Toast.LENGTH_SHORT).show();
                                    }
                            }
                 }

                   }



}
