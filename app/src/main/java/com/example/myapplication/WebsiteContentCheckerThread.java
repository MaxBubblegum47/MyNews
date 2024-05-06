package com.example.myapplication;
import android.content.Context;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.content.Intent;
import android.app.PendingIntent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class WebsiteContentCheckerThread extends Thread{
    private static final String TAG = "WebsiteContentChecker";
    private static final String CHANNEL_ID = "WebsiteContentNotification";

    private static final long CHECK_INTERVAL = 5000; // 5 seconds interval
    private final Context context;
    private final String websiteUrl;
    private String lastContent;

    public WebsiteContentCheckerThread(Context context, String websiteUrl) {
        this.context = context;
        this.websiteUrl = websiteUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                URL url = new URL(websiteUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    String newContent = content.toString();
                    if (lastContent == null || !lastContent.equals(newContent)) {
                        // Content has changed, send notification
                        sendNotification();
                    }
                    lastContent = newContent;
                } finally {
                    urlConnection.disconnect();
                }
                // Sleep for the specified interval before checking again
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                // Thread interrupted, stop checking
                return;
            } catch (IOException e) {
                // Error occurred while fetching website content
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification() {
        // Create notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "WebsiteContentNotification";
            CharSequence channelName = "Website Content Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Create and for when the notification is clicked. This is a test
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "WebsiteContentNotification")
                .setSmallIcon(R.drawable.bbc_logo_2021)
                .setContentTitle("Website Content Checker")
                .setContentText("New content detected on the website!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the intent to be triggered when the notification is clicked
                .setAutoCancel(true); // Automatically dismiss the notification when clicked

        // Show notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, builder.build());
    }
}













