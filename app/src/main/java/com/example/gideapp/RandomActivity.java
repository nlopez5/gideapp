package com.example.gideapp;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomActivity extends Activity {

    private final ThreadLocal<Handler> handler = ThreadLocal.withInitial(() -> new Handler(Looper.getMainLooper()));
    private List<String> taskList;
    private List<TextView> textViews;
    private int spin;
    private int taskWordIndex;
    private String currentTaskWord;

    // Create a copy of the original task list
    private final List<String> originalTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        // Initialize the button and set the click listeners
        Button randomButton = findViewById(R.id.randomButton);
        randomButton.setOnClickListener(this::onRandomClick);

        originalTaskList.add("Mop");
        originalTaskList.add("Sweep");
        originalTaskList.add("Vacuum");
        originalTaskList.add("Wash dishes");
        originalTaskList.add("Do laundry");
        originalTaskList.add("Take out trash");
        originalTaskList.add("Make dinner");
        originalTaskList.add("Exercise");
        originalTaskList.add("Take a break");
        originalTaskList.add("Review budget");
        originalTaskList.add("Clean one room");
        originalTaskList.add("Volunteer");
        originalTaskList.add("Family time");
        originalTaskList.add("Social time");

        // Initialize the list of TextViews
        textViews = new ArrayList<>();
        for (int i : new int[]{R.id.TEXTVIEW1, R.id.TEXTVIEW2, R.id.TEXTVIEW3, R.id.TEXTVIEW4}) {
            textViews.add(findViewById(i));
        }

        // Populate the task list
        taskList = new ArrayList<>(originalTaskList);
    }


    // Handle the click event for the random button
    public void onRandomClick(View view) {
        if (!taskList.isEmpty()) {
            spin = 0;
            taskWordIndex = 0;
            currentTaskWord = "";
            // Make TEXTVIEW2 visible during the animation
            textViews.get(1).setVisibility(View.VISIBLE);
            // Set an onClickListener for TEXTVIEW2 after the spinning stops
            textViews.get(1).setOnClickListener(v -> {
                finish(); // Close the activity when TEXTVIEW2 is clicked
            });
            scrollForward(0);
        }
    }

    // Scroll the text forward in the text views
    private void scrollForward(int currentTaskView) {
        int maxSpin = 6;
        if (spin < maxSpin) {
            updateCurrentTaskWord(currentTaskView);

            for (int i = 0; i < textViews.size(); i++) {
                textViews.get(i).setText((i == currentTaskView) ? currentTaskWord : "");
            }

            startAnimation(textViews.get(currentTaskView));

            // Delay the next scroll forward action
            Objects.requireNonNull(handler.get()).postDelayed(() -> scrollForward((currentTaskView + 1) % textViews.size()), 42);

            // Update the spin count
            if (currentTaskView == 1) {
                spin++;
            }
        } else {
            // Display a random task word
            displayRandomTaskWord();

            // Make TEXTVIEW2 visible and clickable
            textViews.get(1).setVisibility(View.VISIBLE);
            textViews.get(1).setClickable(true);
        }
    }

    // Update the current task word based on the current view index
    private void updateCurrentTaskWord(int currentViewIndex) {
        if (currentViewIndex == 0) {
            currentTaskWord = taskList.get(taskWordIndex);
            taskWordIndex = (taskWordIndex + 1) % taskList.size();
        }
    }

    // Start the animation to move the text view
    private void startAnimation(TextView textView) {
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(textView, "translationY", 70, 100);
        translationAnimator.setDuration(1200);
        translationAnimator.setInterpolator(new DecelerateInterpolator());
        translationAnimator.start();
    }

    // Display a random task word in the second text view
    private void displayRandomTaskWord() {
        if (taskList.isEmpty()) {
            // Reset the task list when it becomes empty
            taskList = new ArrayList<>(originalTaskList);
        }
        Random randomizer = new Random();
        int randomTaskWord = randomizer.nextInt(taskList.size());
        String randomTask = taskList.get(randomTaskWord);
        textViews.get(1).setText(randomTask);
        taskList.remove(randomTask);
    }
}

