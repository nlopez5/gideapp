package com.example.gideapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DismissActivity extends AppCompatActivity {

    private final HandlerThread thread;

    // Default constructor
    public DismissActivity() {
        this.thread = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss);

        // Change the status bar color dynamically
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dismissStatusBarColor));

        // Retrieve the TextView and update the time
        TextView timeTextView = findViewById(R.id.timeTextView);
        updateTime(timeTextView);

        // Set the dismiss button click listener
        Button dismissButton = findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(v -> {
            startRandomActivity();
            finish(); // Finish the current activity to dismiss it
        });

        // Get the notification task from the user
        Intent user = getIntent();
        if (user.hasExtra("NOTIFICATION_TASK")) {
            String notificationTask = user.getStringExtra("NOTIFICATION_TASK");
            // Display the reminder message
            Toast.makeText(this, "Reminder: " + notificationTask, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTime(TextView timeTextView) {
        try {
            // Get the current time
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String currentTime = dateFormat.format(new Date());

            // Set the current time to the timeTextView
            timeTextView.setText(currentTime);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Start the RandomActivity
    private void startRandomActivity() {
        Intent user = new Intent(DismissActivity.this, RandomActivity.class);
        startActivity(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Quit the handler thread safely when the activity is destroyed
        if (thread != null) {
            try {
                thread.quitSafely();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}