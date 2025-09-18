package models;

import java.time.Instant;
import java.util.UUID;

public class Reservation {
    private UUID id;
    private Instant timestamp;
    private UUID hotelID;
    private UUID clientID;
    private int nights;
    private boolean isActive = true;

    public Reservation(UUID hotelID, UUID clientID, int nights) {
        id = UUID.randomUUID();
        timestamp = Instant.now();
        this.hotelID = hotelID;
        this.clientID = clientID;
        this.nights = nights;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public UUID getHotelID() {
        return hotelID;
    }

    public UUID getClientID() {
        return clientID;
    }

    public int getNights() {
        return nights;
    }

    public void setHotelID(UUID hotelID) {
        this.hotelID = hotelID;
    }

    public void setClientID(UUID clientID) {
        this.clientID = clientID;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    public String getStatus() {
        return isActive ? "Active" : "Canceled";
    }
}