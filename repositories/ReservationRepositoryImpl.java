package repositories;

import models.Reservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ReservationRepositoryImpl implements ReservationRepository {
    private final Map<UUID, Reservation> reservations = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findByClientId(UUID clientId) {
        return reservations.values().stream().filter(r -> r.getClientID().equals(clientId)).toList();
    }



    @Override
    public List<Reservation> findByHotelId(UUID hotelId) {
        return reservations.values().stream().filter(r -> r.getHotelID().equals(hotelId)).toList();
    }

    @Override
    public List<Reservation> findActiveByClientId(UUID clientId) {
        return reservations.values().stream()
                .filter(r -> r.getClientID().equals(clientId) && r.isActive()).toList();
    }
    public List<Reservation> findActiveByHotelId(UUID hotelId) {
        return reservations.values().stream()
                .filter(r -> r.getHotelID().equals(hotelId) && r.isActive()).toList();
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public void delete(UUID id) {
        reservations.remove(id);
    }
}