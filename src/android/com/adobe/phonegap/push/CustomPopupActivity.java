package com.adobe.phonegap.push;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Window;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.util.Log;

public class CustomPopupActivity extends Activity {
    private static final String LOG_TAG = "PushPlugin_CustomPopupActivity";

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          Context applicationContext = getApplicationContext();
          int iconId = 0;
          iconId = applicationContext.getApplicationInfo().icon;

          Intent intent = getIntent();
          String title = intent.getExtras().getString("TITLE");
          String message = intent.getExtras().getString("MESSAGE");
          Log.d(LOG_TAG, title);
          Log.d(LOG_TAG, message);

          requestWindowFeature(Window.FEATURE_NO_TITLE);

          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  switch (which) {
                      case DialogInterface.BUTTON_POSITIVE:
                      //Yes Button Clicked
                        finish();
                      break;

                      case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        launchApp();
                        finish();
                      break;
                  }
              }
          };

          AlertDialog.Builder builder = new AlertDialog.Builder(CustomPopupActivity.this);
          builder.setMessage(message)
          .setTitle(title)
          .setIcon(iconId)
          .setCancelable(false)
          .setPositiveButton("닫기", dialogClickListener)
          .setNegativeButton("어플열기", dialogClickListener);
          AlertDialog alertDialog = builder.create();

          getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
          alertDialog.show();
     }

     private void launchApp() {
         PackageManager pm = getPackageManager();
         Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
         launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(launchIntent);
     }

}
