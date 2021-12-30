package com.aet.app.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "Firebase Messaging Service";
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("My token: ", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager.getInstance(getApplicationContext())
                .DisplayNotification(title, body);
    }
}
