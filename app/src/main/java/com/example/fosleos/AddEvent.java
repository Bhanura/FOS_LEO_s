package com.example.fosleos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEvent extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Define the request code for image picking

    private TextInputEditText titleEditText, descriptionEditText;
    private ImageView uploadImageButton;
    private Button uploadButton;
    private Uri imageUri; // Store the image URI when selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Generate a new unique ID
        String eventId = UUID.randomUUID().toString();

        // Initialize Firestore and Storage
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Link UI elements
        titleEditText = findViewById(R.id.uploadTitle);
        descriptionEditText = findViewById(R.id.uploadDescription);
        uploadImageButton = findViewById(R.id.uploadImage);
        uploadButton = findViewById(R.id.uploadButton);

        // Handle image selection
        uploadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle the upload button click
        uploadButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (imageUri != null && !title.isEmpty() && !description.isEmpty()) {
                // Upload image to Firebase Storage
                StorageReference imageRef = storage.getReference().child("images/" + UUID.randomUUID().toString());
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Create a map to store the event data
                            Map<String, Object> event = new HashMap<>();
                            event.put("id", eventId);
                            event.put("title", title);
                            event.put("description", description);
                            event.put("imageUrl", uri.toString());
                            event.put("timestamp", FieldValue.serverTimestamp());

                            // Save the event data to Firestore
                            db.collection("events")
                                    .add(event)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Event uploaded successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddEvent.this, AdminWall.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to upload event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Please fill out all fields and select an image.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageButton.setImageURI(imageUri);
        }
    }
}
