package repositories;

import models.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {
    private final Map<UUID, Client> clients = new HashMap<>();

    @Override
    public void save(Client client) {
        this.clients.put(client.getId(), client);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clients.values().stream().filter(c -> c.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void delete(UUID id) {
        clients.remove(id);
    }
}
