package com.example.fosleos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

// Custom ArrayAdapter for displaying events
public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;  // Context to access application-specific resources
    private List<Event> events;  // List of events to display

    // Constructor
    public EventAdapter(@NonNull Context context, @NonNull List<Event> events) {
        super(context, 0, events);  // Call the superclass constructor
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Reuse an existing view if possible, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_event_adapter, parent, false);
        }

        // Get the event at the specified position
        Event event = getItem(position);

        // Find and set up UI elements
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView dateAndTimeTextView = convertView.findViewById(R.id.dateAndTime);
        TextView descriptionTextView = convertView.findViewById(R.id.description);
        ImageView imageView = convertView.findViewById(R.id.image);
        ImageButton postMenuButton = convertView.findViewById(R.id.postMenu);

        // Set the event data into the UI elements
        titleTextView.setText(event.getTitle());
        dateAndTimeTextView.setText(event.getTimestamp().toDate().toString()); // Convert timestamp to a readable date
        descriptionTextView.setText(event.getDescription());

        // Load image using Glide library
        Glide.with(context).load(event.getImageUrl()).into(imageView);

        // Set up the menu button for actions
        postMenuButton.setOnClickListener(v -> {
            // Create and show a popup menu
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.post_menu_admin, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                String eventId = event.getId();  // Get the event ID

                // Handle menu item clicks
                if (itemId == R.id.edit_event) {
                    if (eventId != null) {
                        // Start EditEventActivity to edit the event
                        Intent editIntent = new Intent(context, EditEventActivity.class);
                        editIntent.putExtra("eventId", eventId);
                        context.startActivity(editIntent);
                    } else {
                        // Log error and show a toast if event ID is null
                        Log.e("EventAdapter", "Event ID is null, cannot edit event");
                        Toast.makeText(context, "Error: Unable to edit event. Event ID is missing.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (itemId == R.id.delete_event) {
                    if (eventId != null) {
                        // Call method to delete the event
                        deleteEvent(eventId, position);
                    } else {
                        // Log error and show a toast if event ID is null
                        Log.e("EventAdapter", "Event ID is null, cannot delete event");
                        Toast.makeText(context, "Error: Unable to delete event. Event ID is missing.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            });
            popupMenu.show();  // Show the popup menu
        });

        return convertView;  // Return the modified view
    }

    // Method to delete an event from Firestore
    public void deleteEvent(String eventId, int position) {
        if (eventId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("events").document(eventId)
                    .delete()  // Delete the event document from Firestore
                    .addOnSuccessListener(aVoid -> {
                        // Remove the event from the list and notify the adapter
                        events.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Show a toast message if the deletion fails
                        Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Log error if event ID is null
            Log.e("EventAdapter", "Event ID is null, cannot delete event");
        }
    }
}
