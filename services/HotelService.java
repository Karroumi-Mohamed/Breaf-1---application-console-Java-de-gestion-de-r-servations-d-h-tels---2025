package services;

import models.Client;
import models.Hotel;
import models.Role;
import repositories.HotelRepository;
import repositories.ReservationRepository;
import java.util.List;
import java.util.UUID;

class HotelService {
    private final HotelRepository hotelRepo;
    private final ReservationRepository reservationRepo;

    public HotelService(HotelRepository hotelRepo, ReservationRepository reservationRepo) {
        this.hotelRepo = hotelRepo;
        this.reservationRepo = reservationRepo;
    }

    public Hotel createHotel(Client user, String name, String address, int rooms, double rating) {
        if (user.getRole() != Role.Admin) {
            throw new SecurityException("Only admins can create hotels");
        }

        if (rooms < 0)
            throw new IllegalArgumentException("Rooms must be >= 0");
        if (rating < 0 || rating > 5)
            throw new IllegalArgumentException("Rating must be between 0–5");

        Hotel hotel = new Hotel(name, address, rooms, rating);
        hotelRepo.save(hotel);
        return hotel;
    }

    public List<Hotel> listAllHotels() {
        return hotelRepo.findAll();
    }

    public List<Hotel> listAvailableHotels() {
        return hotelRepo.findAll().stream()
                .filter(h -> h.getAvailableRooms() > 0).toList();
    }

    public void updateHotel(Client user, UUID hotelId, String newName, String newAddress, Integer newRooms,
            Double newRating) {
        if (user.getRole() != Role.Admin) {
            throw new SecurityException("Only admins can update hotels");
        }

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        if (newName != null)
            hotel.setName(newName);
        if (newAddress != null)
            hotel.setAddress(newAddress);
        if (newRooms != null)
            hotel.setAvailableRooms(newRooms);
        if (newRating != null)
            hotel.setRating(newRating);
    }

    public void deleteHotel(Client user, UUID id) {
        if (user.getRole() != Role.Admin) {
            throw new SecurityException("Only Admin can delete hotels");
        }
        boolean hasActiveReservations = !reservationRepo.findActiveByHotelId(id).isEmpty();
        if (hasActiveReservations) {
            throw new IllegalStateException("Can't delete hotel with active reservations");
        }

        hotelRepo.delete(id);
    }
}