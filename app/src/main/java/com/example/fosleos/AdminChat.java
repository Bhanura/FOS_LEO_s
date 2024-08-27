package com.example.fosleos;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class AdminChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        ImageButton button1 = findViewById(R.id.button1);
        ImageButton home = findViewById(R.id.home);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton notification = findViewById(R.id.notication);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminChat.this, AdminWall.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminChat.this, AdminChat.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminChat.this, AdminNotification.class);
                startActivity(intent);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main_menu_admin, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.option1) {
                    // Handle option 1 click
                    Intent intent = new Intent(AdminChat.this, PostActivity.class); // Navigate to login screen
                    startActivity(intent);
                    finish(); // Finish the current activity

                    return true;
                } else if (id == R.id.option2) {
                    // Handle option 1 click
                    Intent intent = new Intent(AdminChat.this, ProfileActivity.class); // Navigate to login screen
                    startActivity(intent);
                    finish(); // Finish the current activity

                    return true;
                } else if (id == R.id.option3) {
                    // Handle option 1 click
                    Intent intent = new Intent(AdminChat.this, MembersListActivity.class); // Navigate to login screen
                    startActivity(intent);
                    finish(); // Finish the current activity

                    return true;
                } else if (id == R.id.option4) {
                    // Handle option 1 click
                    Intent intent = new Intent(AdminChat.this, AboutActivity.class); // Navigate to login screen
                    startActivity(intent);
                    finish(); // Finish the current activity

                    return true;
                } else if (id == R.id.option5) {
                    // Handle option 3 click
                    Toast.makeText(AdminChat.this, "Logging out...", Toast.LENGTH_SHORT).show();

                    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
                    mAuth.signOut(); // Sign out the user

                    Intent intent = new Intent(AdminChat.this, LoginActivity.class); // Navigate to login screen
                    startActivity(intent);
                    finish(); // Finish the current activity

                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}
