package repositories;

import models.Hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HotelRepositoryImpl implements HotelRepository {
    private final Map<String, Hotel> hotels = new HashMap<>();

    @Override
    public void save(Hotel hotel) {
        hotels.put(hotel.getId(), hotel);
    }

    @Override
    public Optional<Hotel> findById(String id) {
        return Optional.ofNullable(hotels.get(id));
    }

    @Override
    public void delete(String id) {
        hotels.remove(id);
    }

    @Override
    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }
}
