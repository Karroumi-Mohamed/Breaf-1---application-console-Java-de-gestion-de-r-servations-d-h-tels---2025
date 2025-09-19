package services;

import models.Client;
import models.Hotel;
import models.Reservation;
import repositories.HotelRepository;
import repositories.ReservationRepository;
import java.util.List;
import java.util.UUID;

public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final HotelRepository hotelRepo;

    public ReservationService(ReservationRepository reservationRepo, HotelRepository hotelRepo) {
        this.reservationRepo = reservationRepo;
        this.hotelRepo = hotelRepo;
    }

    public Reservation reserveRoom(Client client, String hotelId, int nights) {
        if (nights <= 0)
            throw new IllegalArgumentException("Nights must be > 0");

        Hotel hotel = hotelRepo.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        hotel.reserveRoom();
        hotelRepo.save(hotel);

        Reservation reservation = new Reservation(hotelId, client.getId(), nights);
        reservationRepo.save(reservation);
        return reservation;
    }

    public void cancelReservation(Client client, UUID reservationId) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!reservation.getClientID().equals(client.getId())) {
            throw new SecurityException("You can only cancel your reservations");
        }

        Hotel hotel = hotelRepo.findById(reservation.getHotelID())
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        hotel.cancelReservation();
        hotelRepo.save(hotel);
        reservation.cancel();
        reservationRepo.save(reservation);
    }

    public List<Reservation> getHistory(Client client) {
        return reservationRepo.findByClientId(client.getId()).stream()
                .sorted((r1, r2) -> r1.getTimestamp().compareTo(r2.getTimestamp()))
                .toList();
    }

    public List<Reservation> getActiveReservations(Client client) {
        return reservationRepo.findActiveByClientId(client.getId());
    }
}
