package com.support.android.musicindia.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.support.android.musicindia.R;
import com.support.android.musicindia.activities.MainActivity;
import com.support.android.musicindia.activities.SplashScreenActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerGCMListener extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i("TAG", "Notification received");

        Log.e("InMEsRec", "In Rec");
        Log.e("InMEsRec","In Rec");
        Log.e("InMEsRec","In Rec");

        String message = data.getString("message");
        Log.e("InMEss",message);
        Log.e("InMEss", message);
        Log.e("InMEss",message);
        String title = data.getString("title");
                //"TITLE HARD CODE";

        Log.e("INttile",title);
        Log.e("INttile", title);
        String urlBollyWood = data.getString("urlBollyWood");
                //"TICKER TEXT HARD CODE";
                //data.getString("tickerText");

        String tikerText = data.getString("tikerText");
        /*String smallIconURL = data.getString("smallIcon");
        String LargeIconURL = data.getString("largeIcon");*/

        Bitmap smalliconbitmap = getBitmapFromURL(urlBollyWood);

        //ShowSimpleNotifications(message,title,tikerText,smalliconbitmap);
        //Bitmap largeiconbitmap = getBitmapFromURL(LargeIconURL);
        showBigPictureStyleNotifications(message,title,tikerText,smalliconbitmap,smalliconbitmap);
    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showBigPictureStyleNotifications(String message, String title, String tickertext, Bitmap smallIcon,
                                                 Bitmap largeIcon)
    {
        //Create notification object and set the content.
        NotificationCompat.Builder nb= new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.ic_stat_mi);

        nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        nb.setContentTitle(title);
        nb.setContentText(message);
        nb.setTicker(tickertext);

        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(largeIcon);
        s.setSummaryText(message);
        nb.setStyle(s);

        Intent resultIntent = new Intent(this, SplashScreenActivity.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());
    }

}
