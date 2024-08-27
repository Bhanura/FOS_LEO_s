package com.example.fosleos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText, descriptionEditText;
    private ImageButton uploadImageButton;
    private Button saveButton;
    private Uri imageUri;
    private String eventId, imageUrl;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialize views
        titleEditText = findViewById(R.id.editTitle);
        descriptionEditText = findViewById(R.id.editDescription);
        uploadImageButton = findViewById(R.id.editImage);
        saveButton = findViewById(R.id.updateButton);

        // Get event ID from intent
        eventId = getIntent().getStringExtra("eventId");

        // Initialize Firestore and Storage instances
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("event_images");

        // Fetch event details and populate fields
        fetchEventDetails();

        // Handle image button click to select a new image
        uploadImageButton.setOnClickListener(v -> openFileChooser());

        // Handle save button click
        saveButton.setOnClickListener(v -> handleSaveEvent());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(uploadImageButton);
        }
    }

    private void fetchEventDetails() {
        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    titleEditText.setText(event.getTitle());
                    descriptionEditText.setText(event.getDescription());
                    imageUrl = event.getImageUrl(); // Save current image URL
                    Glide.with(this).load(imageUrl).into(uploadImageButton);
                }
            }
        });
    }

    private void handleSaveEvent() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (imageUri != null) {
            // If a new image is selected, upload it and update Firestore with the new URL
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString(); // Get the new image URL
                        updateEventInFirestore(title, description, imageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } else {
            // If no new image is selected, keep the existing image URL
            updateEventInFirestore(title, description, imageUrl);
        }
    }

    private void updateEventInFirestore(String title, String description, String imageUrl) {
        Map<String, Object> updatedEvent = new HashMap<>();
        updatedEvent.put("title", title);
        updatedEvent.put("description", description);
        updatedEvent.put("imageUrl", imageUrl);

        db.collection("events").document(eventId).update(updatedEvent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
                    Intent editIntent = new Intent(EditEventActivity.this, AdminWall.class);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show());
    }
}
