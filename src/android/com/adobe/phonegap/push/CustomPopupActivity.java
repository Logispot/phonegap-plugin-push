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
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
          String popupTitle = intent.getExtras().getString("POPUP_TITLE");
          String popupMessage = intent.getExtras().getString("POPUP_MESSAGE");
          String positiveButton = intent.getExtras().getString("POSITIVE_BUTTON");
          String negativeButton = intent.getExtras().getString("NEGATIVE_BUTTON");

          String title = (popupTitle != null && !popupTitle.isEmpty()) ? popupTitle : intent.getExtras().getString("TITLE");
          String message = (popupMessage != null && !popupMessage.isEmpty()) ? popupMessage : intent.getExtras().getString("MESSAGE");

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
          .setPositiveButton((positiveButton != null && !positiveButton.isEmpty()) ? positiveButton : "닫기", dialogClickListener)
          .setNegativeButton((negativeButton != null && !negativeButton.isEmpty()) ? negativeButton : "어플열기", dialogClickListener);
          AlertDialog alertDialog = builder.create();

          getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
          alertDialog.show();

          startVibrate();
          startSound();
     }

     private void launchApp() {
         PackageManager pm = getPackageManager();
         Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
         launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(launchIntent);
     }

     private void startVibrate() {
         Vibrator vibrator;
         vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
         } else {
             //deprecated in API 26
             vibrator.vibrate(1000);
         }
     }

     private void startSound() {
         Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
         ringtone.play();
     }

}
