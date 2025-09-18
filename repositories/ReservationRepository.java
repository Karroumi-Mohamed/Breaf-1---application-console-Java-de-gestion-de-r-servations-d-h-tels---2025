package repositories;

import models.Reservation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    void save(Reservation reservation);

    Optional<Reservation> findById(UUID id);

    List<Reservation> findByClientId(UUID clientId);

    List<Reservation> findActiveByClientId(UUID clientId);

    List<Reservation> findActiveByHotelId(UUID hotelId);

    List<Reservation> findByHotelId(UUID hotelId);

    List<Reservation> findAll();

    void delete(UUID id);
}