package com.example.fosleos;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout for this activity
        setContentView(R.layout.activity_posting);

        // Initialize buttons for adding events and polls
        Button add_event = findViewById(R.id.add_event);
        Button add_poll = findViewById(R.id.add_poll);

        // Set up click listener for the "Add Event" button
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddEvent activity
                Intent intent = new Intent(PostActivity.this, AddEvent.class);
                startActivity(intent);
            }
        });

        // Set up click listener for the "Add Poll" button
        add_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddPoll activity
                Intent intent = new Intent(PostActivity.this, AddPoll.class);
                startActivity(intent);
            }
        });

        // Initialize other buttons for navigation
        ImageButton button1 = findViewById(R.id.button1);
        ImageButton home = findViewById(R.id.home);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton notification = findViewById(R.id.notication);

        // Set up click listener for the button to show the popup menu
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the popup menu
                showPopupMenu(v);
            }
        });

        // Set up click listener for the "Home" button
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AdminWall activity
                Intent intent = new Intent(PostActivity.this, AdminWall.class);
                startActivity(intent);
            }
        });

        // Set up click listener for the "Chat" button
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AdminChat activity
                Intent intent = new Intent(PostActivity.this, AdminChat.class);
                startActivity(intent);
            }
        });

        // Set up click listener for the "Notification" button
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AdminNotification activity
                Intent intent = new Intent(PostActivity.this, AdminNotification.class);
                startActivity(intent);
            }
        });
    }

    // Method to display a popup menu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        // Inflate the menu resource into the popup menu
        inflater.inflate(R.menu.main_menu_admin, popupMenu.getMenu());
        // Set up a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.option1) {
                    // Handle option 1 click: Navigate to PostActivity
                    Intent intent = new Intent(PostActivity.this, PostActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                } else if (id == R.id.option2) {
                    // Handle option 2 click: Navigate to ProfileActivity
                    Intent intent = new Intent(PostActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                } else if (id == R.id.option3) {
                    // Handle option 3 click: Navigate to MembersListActivity
                    Intent intent = new Intent(PostActivity.this, MembersListActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                } else if (id == R.id.option4) {
                    // Handle option 4 click: Navigate to AboutActivity
                    Intent intent = new Intent(PostActivity.this, AboutActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                } else if (id == R.id.option5) {
                    // Handle option 5 click: Log out the user
                    Toast.makeText(PostActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
                    mAuth.signOut(); // Sign out the user
                    Intent intent = new Intent(PostActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                    return true;
                }
                return false;
            }
        });
        // Display the popup menu
        popupMenu.show();
    }
}
