package com.example.fosleos;

// Class representing an Event
public class Event {
    private String id;  // Unique identifier for the event
    private String title;  // Title of the event
    private String description;  // Description of the event
    private String imageUrl;  // URL of the event image
    private com.google.firebase.Timestamp timestamp;  // Timestamp of the event

    // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    public Event() {}

    // Parameterized constructor for creating an Event object with specified values
    public Event(String id, String title, String description, String imageUrl, com.google.firebase.Timestamp timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    // Getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter method for the event title
    public String getTitle() {
        return title;
    }

    // Getter method for the event description
    public String getDescription() {
        return description;
    }

    // Getter method for the event image URL
    public String getImageUrl() {
        return imageUrl;
    }

    // Getter method for the event timestamp
    public com.google.firebase.Timestamp getTimestamp() {
        return timestamp;
    }
}
