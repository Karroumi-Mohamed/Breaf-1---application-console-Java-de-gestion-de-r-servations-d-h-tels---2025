package models;

import java.util.UUID;

public class Client {
    private String fullName;
    private String email;
    private String password;
    private Role role;
    private UUID id;

    public Client(String fName, String email, String password, Role role) {
        id = UUID.randomUUID();
        fullName = fName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Role getRole() {
        return this.role;
    }

    public void setFullName(String fName) {
        this.fullName = fName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean setPassword(String password) {
        if (password.length() < 6) {
            return false;
        }
        this.password = password;
        return true;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
