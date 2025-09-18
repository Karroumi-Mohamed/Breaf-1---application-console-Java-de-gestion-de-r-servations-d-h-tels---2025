package models;

import java.util.UUID;

public class Hotel {
    private String name;
    private String address;
    private int availableRooms;
    private double rating;
    private UUID id;

    public Hotel(String name, String address, int rooms, double rating) {
        id = UUID.randomUUID();
        this.name = name;
        this.address = address;
        availableRooms = rooms;
        this.rating = rating;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be in between 0-5");
        }
        this.rating = rating;
    }

    public void setAvailableRooms(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("Available rooms cannot be negative");
        }
        this.availableRooms = num;
    }

    public void reserveRoom() {
        if (this.availableRooms <= 0) {
            throw new IllegalStateException("No available rooms");
        }
        this.availableRooms--;

    }

    public void cancelReservation() {
        this.availableRooms++;
    }
}