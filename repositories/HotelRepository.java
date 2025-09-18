package repositories;

import models.Hotel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelRepository {
    void save(Hotel hotel);

    Optional<Hotel> findById(UUID id);

    List<Hotel> findAll();

    void delete(UUID id);
}