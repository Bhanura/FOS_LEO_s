package com.example.fosleos;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminWall extends AppCompatActivity {

    private ListView listView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    public void onBackPressed() {
        // Use finishAffinity() to close all activities and exit the app
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_wall);

        listView = findViewById(R.id.postView);

        // Initialize the event list and adapter
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList);

        // Set the adapter to the ListView
        listView.setAdapter(eventAdapter);

        // Retrieve events from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            event.setId(document.getId());  // Set the event ID manually
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                });



        //menu buttons...

        ImageButton menu = findViewById(R.id.button1);
        ImageButton home = findViewById(R.id.home);
        ImageButton chat = findViewById(R.id.chat);
        ImageButton notification = findViewById(R.id.notication);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminWall.this, AdminChat.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminWall.this, AdminNotification.class);
                startActivity(intent);
            }
        });

    }

    // popup-menu...

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main_menu_admin, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.option1) {
                    Intent intent = new Intent(AdminWall.this, PostActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id == R.id.option2) {
                    Intent intent = new Intent(AdminWall.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id == R.id.option3) {
                    Intent intent = new Intent(AdminWall.this, MembersListActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id == R.id.option4) {
                    Intent intent = new Intent(AdminWall.this, AboutActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (id == R.id.option5) {
                    Toast.makeText(AdminWall.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(AdminWall.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
