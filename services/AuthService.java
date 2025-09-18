package services;

import models.Client;
import models.Role;
import repositories.ClientRepository;

public class AuthService {
    private final ClientRepository clientRepo;

    public  AuthService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public Client register(String fullName, String email, String password, Role role) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 charachters");
        }
        if (clientRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Client client = new Client(fullName, email, password, role);
        clientRepo.save(client);
        return client;
    }

    public Client login(String email, String password) {
        Client client = clientRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!client.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return client;
    }

    public void updateEmail(Client client, String newEmail) {
        if (clientRepo.findByEmail(newEmail).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        client.setEmail(newEmail);
        clientRepo.save(client);
    }

    public void updatePassword(Client client, String newPassword) {
        if (!client.setPassword(newPassword)) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        clientRepo.save(client);
    }
}